package com.example.branch.presentation.message.state

import com.example.branch.domain.model.MessagesEntity


data class ChatUIState(
    val isLoading: Boolean = false,
    val messages: List<MessagesEntity> = emptyList(),
    val error: String = ""
)