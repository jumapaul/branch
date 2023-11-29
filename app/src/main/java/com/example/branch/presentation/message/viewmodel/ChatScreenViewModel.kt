package com.example.branch.presentation.message.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.branch.common.Resources
import com.example.branch.domain.model.ThreadDto
import com.example.branch.domain.use_cases.ChatUseCase
import com.example.branch.domain.use_cases.CreateThreadMessageUseCase
import com.example.branch.presentation.message.state.ChatUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ChatScreenViewModel @Inject constructor(
    private val chatUseCase: ChatUseCase,
    private val createThreadMessageUseCase: CreateThreadMessageUseCase
) : ViewModel() {
    private var _chatState = mutableStateOf(ChatUIState())
    val chatState: State<ChatUIState> = _chatState

    fun getThreadMessages(threadId: Int) {
        chatUseCase(threadId).onEach { result ->
            when (result) {
                is Resources.IsLoading -> {
                    _chatState.value = ChatUIState(isLoading = true)
                }

                is Resources.Success -> {
                    _chatState.value =
                        ChatUIState(isLoading = false, messages = result.data ?: emptyList())
                }

                is Resources.Error -> {
                    _chatState.value = ChatUIState(
                        isLoading = false,
                        error = result.message ?: "Unexpected error occurred"
                    )
                }
            }
        }.launchIn(viewModelScope)
    }


    fun createThreadMessage(threadDto: ThreadDto) {
        createThreadMessageUseCase(threadDto).onEach { result ->
            when (result) {
                is Resources.IsLoading -> {
                    _chatState.value = ChatUIState(isLoading = true)
                }

                is Resources.Success -> {
                    _chatState.value =
                        ChatUIState(isLoading = false, messages = result.data ?: emptyList())
                }

                is Resources.Error -> {
                    _chatState.value = ChatUIState(
                        isLoading = false,
                        error = result.message ?: "Unexpected error occurred"
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}