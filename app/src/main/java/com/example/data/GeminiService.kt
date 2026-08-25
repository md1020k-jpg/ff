package com.example.data

import android.util.Log
import com.example.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface GeminiApiEndpoint {
    @POST("v1beta/models/{model}:generateContent")
    suspend fun generateContent(
        @Path("model") model: String,
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}

object GeminiClient {
    private const val TAG = "GeminiClient"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val api: GeminiApiEndpoint = retrofit.create(GeminiApiEndpoint::class.java)

    /**
     * Executes a multi-turn chat request with system instructions and optional Google Search grounding.
     */
    suspend fun sendChatMessage(
        conversationHistory: List<ChatMessage>,
        userMessage: String,
        modelChoice: GeminiModelChoice,
        enableSearchGrounding: Boolean = true
    ): Result<ChatMessage> = withContext(Dispatchers.IO) {
        val apiKey = GeminiApiKeyProvider.getApiKey()

        if (apiKey.isBlank() || !GeminiApiKeyProvider.isKeyConfigured()) {
            return@withContext Result.failure(
                IllegalStateException("Gemini API key is not configured. Please add your key in the AI Studio Secrets panel or .env file.")
            )
        }

        val systemInstructionText = """
            You are the "Hyperbolic Mathematics, Calculus & Catenary Physics Specialist" in the Hyperbolic & Catenary Studio Android app.
            Your role is to explain hyperbolic functions (cosh, sinh, tanh, sech, csch, coth), calculus derivations (derivatives, definite/indefinite integrals, Taylor series), and real-world catenary curve physics (suspension cables, overhead power transmission lines, Gateway Arch, Antoni Gaudí's hanging catenary models).
            
            When Search Grounding is enabled, use Google Search to fetch up-to-date and accurate real-world engineering standards, architectural projects, historical facts, bridge measurements, and physics literature.
            
            Format your responses clearly with formatted mathematical expressions, bullet points, step-by-step explanations, and real-world engineering insights.
        """.trimIndent()

        val contents = mutableListOf<GeminiContent>()

        // Append past conversation history (up to last 10 messages for context)
        val recentHistory = conversationHistory.takeLast(10)
        for (msg in recentHistory) {
            val role = if (msg.sender == MessageSender.USER) "user" else "model"
            if (msg.text.isNotBlank() && !msg.isLoading && !msg.isError) {
                contents.add(
                    GeminiContent(
                        role = role,
                        parts = listOf(GeminiPart(text = msg.text))
                    )
                )
            }
        }

        // Add current user message
        contents.add(
            GeminiContent(
                role = "user",
                parts = listOf(GeminiPart(text = userMessage))
            )
        )

        // Setup tools for Search Grounding if enabled
        val tools = if (enableSearchGrounding) {
            listOf(mapOf("googleSearch" to emptyMap<String, String>()))
        } else {
            null
        }

        val request = GeminiRequest(
            contents = contents,
            systemInstruction = GeminiContent(
                parts = listOf(GeminiPart(text = systemInstructionText))
            ),
            generationConfig = GeminiGenerationConfig(
                temperature = 0.7f,
                topP = 0.95f,
                topK = 40,
                maxOutputTokens = 2048
            ),
            tools = tools
        )

        try {
            val response = api.generateContent(
                model = modelChoice.modelId,
                apiKey = apiKey,
                request = request
            )

            val candidate = response.candidates?.firstOrNull()
            val textPart = candidate?.content?.parts?.firstOrNull()?.text

            if (textPart.isNullOrBlank()) {
                val errorMsg = response.error?.message ?: "Received empty response from Gemini."
                return@withContext Result.failure(RuntimeException(errorMsg))
            }

            // Extract Google Search grounding metadata
            val grounding = candidate.groundingMetadata
            val searchQueries = grounding?.webSearchQueries ?: emptyList()
            val webSources = grounding?.groundingChunks?.mapNotNull { chunk ->
                val uri = chunk.web?.uri
                val title = chunk.web?.title ?: uri
                if (uri != null && title != null) {
                    WebSource(title = title, url = uri)
                } else null
            }?.distinctBy { it.url } ?: emptyList()

            val aiMessage = ChatMessage(
                sender = MessageSender.AI,
                text = textPart,
                modelName = modelChoice.displayName,
                isSearchGrounded = enableSearchGrounding && (searchQueries.isNotEmpty() || webSources.isNotEmpty()),
                searchQueries = searchQueries,
                sources = webSources
            )

            Result.success(aiMessage)
        } catch (e: Exception) {
            Log.e(TAG, "Error invoking Gemini API: ${e.message}", e)
            Result.failure(e)
        }
    }
}
