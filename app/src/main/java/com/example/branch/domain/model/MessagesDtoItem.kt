package com.example.branch.domain.model

data class MessagesDtoItem(
    val agent_id: String,
    val body: String,
    val id: Int,
    val thread_id: Int,
    val timestamp: String,
    val user_id: String
)