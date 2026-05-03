package com.aicoder.grokclient.data.repository

import com.aicoder.grokclient.data.api.XaiApiService
import com.aicoder.grokclient.data.model.ChatRequest
import com.aicoder.grokclient.data.model.Message
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepository @Inject constructor(
    private val apiService: XaiApiService
) {
    suspend fun sendMessage(apiKey: String, userMessage: String, history: List<Message>): Result<String> {
        return try {
            val messages = history + Message(role = "user", content = userMessage)
            val request = ChatRequest(messages = messages)
            val response = apiService.chatCompletions(
                auth = "Bearer $apiKey",
                request = request
            )
            val assistantMessage = response.choices.firstOrNull()?.message?.content ?: "No response"
            Result.success(assistantMessage)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
