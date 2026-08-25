package com.example.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Gemini API Request and Response Models
 */

@JsonClass(generateAdapter = true)
data class GeminiRequest(
    val contents: List<GeminiContent>,
    val systemInstruction: GeminiContent? = null,
    val generationConfig: GeminiGenerationConfig? = null,
    val tools: List<Map<String, Map<String, String>>>? = null
)

@JsonClass(generateAdapter = true)
data class GeminiContent(
    val role: String? = null,
    val parts: List<GeminiPart>
)

@JsonClass(generateAdapter = true)
data class GeminiPart(
    val text: String? = null
)

@JsonClass(generateAdapter = true)
data class GeminiGenerationConfig(
    val temperature: Float? = 0.7f,
    val topP: Float? = 0.95f,
    val topK: Int? = 40,
    val maxOutputTokens: Int? = 2048
)

@JsonClass(generateAdapter = true)
data class GeminiResponse(
    val candidates: List<GeminiCandidate>? = null,
    val error: GeminiErrorDetails? = null
)

@JsonClass(generateAdapter = true)
data class GeminiErrorDetails(
    val code: Int? = null,
    val message: String? = null,
    val status: String? = null
)

@JsonClass(generateAdapter = true)
data class GeminiCandidate(
    val content: GeminiContent? = null,
    val finishReason: String? = null,
    val groundingMetadata: GroundingMetadata? = null
)

@JsonClass(generateAdapter = true)
data class GroundingMetadata(
    val webSearchQueries: List<String>? = null,
    val groundingChunks: List<GroundingChunk>? = null,
    val searchEntryPoint: SearchEntryPoint? = null
)

@JsonClass(generateAdapter = true)
data class GroundingChunk(
    val web: GroundingWebSource? = null
)

@JsonClass(generateAdapter = true)
data class GroundingWebSource(
    val uri: String? = null,
    val title: String? = null
)

@JsonClass(generateAdapter = true)
data class SearchEntryPoint(
    val renderedContent: String? = null
)

/**
 * Domain Models for Chat UI
 */
enum class MessageSender {
    USER,
    AI,
    SYSTEM
}

data class WebSource(
    val title: String,
    val url: String
)

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val sender: MessageSender,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val modelName: String? = null,
    val isSearchGrounded: Boolean = false,
    val searchQueries: List<String> = emptyList(),
    val sources: List<WebSource> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false
)

enum class GeminiModelChoice(
    val modelId: String,
    val displayName: String,
    val shortDesc: String,
    val isDefaultSearch: Boolean
) {
    FLASH_SEARCH(
        modelId = "gemini-3.5-flash",
        displayName = "Gemini 3.5 Flash + Search",
        shortDesc = "Fast, multimodal & Google Search grounded",
        isDefaultSearch = true
    ),
    PRO_MATH(
        modelId = "gemini-3.1-pro-preview",
        displayName = "Gemini 3.1 Pro (Deep Math)",
        shortDesc = "Advanced calculus reasoning & proofs",
        isDefaultSearch = false
    ),
    FLASH_LITE(
        modelId = "gemini-3.1-flash-lite-preview",
        displayName = "Gemini 3.1 Flash Lite",
        shortDesc = "Ultra-fast instant physics answers",
        isDefaultSearch = false
    )
}
