package com.aicoder.grokclient.data.model

data class ChatRequest(
    val model: String = "grok-4.3",
    val messages: List<Message>,
    val stream: Boolean = false,
    val temperature: Double = 0.7,
    val max_tokens: Int = 1024
)

data class Message(
    val role: String,
    val content: String
)
