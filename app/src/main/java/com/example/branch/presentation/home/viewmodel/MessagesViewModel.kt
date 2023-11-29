package com.example.branch.presentation.home.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.branch.common.Resources
import com.example.branch.domain.use_cases.MessagesUseCase
import com.example.branch.presentation.home.state.MessagesUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MessagesViewModel @Inject constructor(
    private val messagesUseCase: MessagesUseCase
) : ViewModel() {

    private val _messages = mutableStateOf(MessagesUiState())
    val messages: State<MessagesUiState> = _messages

    init {
        getAllMessages()
    }

    private fun getAllMessages() {
        messagesUseCase().onEach { results ->
            when (results) {
                is Resources.IsLoading -> {
                    _messages.value = MessagesUiState(isLoading = true)
                }

                is Resources.Success -> {
                    _messages.value =
                        MessagesUiState(messages = results.data ?: emptyList(), isLoading = false)
                }

                is Resources.Error -> {
                    _messages.value =
                        MessagesUiState(
                            error = results.message ?: "Unexpected error occurred",
                            isLoading = false
                        )
                }
            }
        }.launchIn(viewModelScope)
    }
}