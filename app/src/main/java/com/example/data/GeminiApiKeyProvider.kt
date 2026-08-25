package com.example.data

import android.util.Log
import com.example.BuildConfig

/**
 * Secure utility to manage and validate the Gemini API Key.
 * Fetches the compile-time injected key from BuildConfig (populated via Secrets plugin / environment variables).
 */
object GeminiApiKeyProvider {
    private const val TAG = "GeminiApiKeyProvider"
    private const val PLACEHOLDER_KEY = "MY_GEMINI_API_KEY"

    /**
     * Retrieves the sanitized Gemini API key.
     * Returns empty string if the key is missing, blank, or placeholder.
     */
    fun getApiKey(): String {
        return try {
            val key = BuildConfig.GEMINI_API_KEY.trim().removeSurrounding("\"").removeSurrounding("'")
            if (key.isBlank() || key == PLACEHOLDER_KEY || key.equals("null", ignoreCase = true)) {
                ""
            } else {
                key
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error accessing BuildConfig.GEMINI_API_KEY", e)
            ""
        }
    }

    /**
     * Checks whether a valid Gemini API key is present and configured.
     */
    fun isKeyConfigured(): Boolean {
        val key = getApiKey()
        return key.isNotBlank() && key.length >= 10
    }

    /**
     * Returns a safely masked version of the API key for UI indicators (e.g. "AIzaSy...7hQ").
     */
    fun getMaskedKey(): String {
        val key = getApiKey()
        if (key.isBlank()) return "Not Configured"
        return if (key.length > 8) {
            "${key.take(6)}...${key.takeLast(4)}"
        } else {
            "Configured (Protected)"
        }
    }

    /**
     * Formats a human-readable status for diagnostics and UI tooltips.
     */
    fun getStatusDescription(): String {
        return if (isKeyConfigured()) {
            "API Key Active (${getMaskedKey()})"
        } else {
            "API Key Missing (Configure in AI Studio Secrets)"
        }
    }
}
