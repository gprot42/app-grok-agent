package com.tinygrok.client.data

import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

data class ChatRequest(
    val model: String = "grok-3",
    val messages: List<ChatMessage>,
    val temperature: Double = 0.7,
    val max_tokens: Int = 1024
)

data class ChatMessage(
    val role: String,
    val content: String
)

data class ChatResponse(
    val id: String,
    val choices: List<Choice>,
    val usage: Usage? = null
)

data class Choice(
    val index: Int,
    val message: ChatMessage,
    @SerializedName("finish_reason") val finishReason: String
)

data class Usage(
    @SerializedName("prompt_tokens") val promptTokens: Int,
    @SerializedName("completion_tokens") val completionTokens: Int,
    @SerializedName("total_tokens") val totalTokens: Int
)

interface GrokApiService {
    @POST("v1/chat/completions")
    suspend fun chatCompletions(
        @Header("Authorization") auth: String,
        @Body request: ChatRequest
    ): ChatResponse
}
