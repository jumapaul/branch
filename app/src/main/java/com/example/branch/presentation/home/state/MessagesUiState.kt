package com.example.branch.presentation.home.state

import com.example.branch.domain.model.MessagesEntity

data class MessagesUiState(
    val isLoading: Boolean = false,
    val messages: List<MessagesEntity> = emptyList(),
    val error: String = ""
)
