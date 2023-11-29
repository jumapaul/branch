package com.example.branch.presentation.login.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.branch.common.Resources
import com.example.branch.domain.model.LoginCredential
import com.example.branch.domain.use_cases.LoginUserUseCase
import com.example.branch.presentation.login.state.LoginUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val addUserUseCase: LoginUserUseCase
) : ViewModel() {

    private val _loginState = mutableStateOf(LoginUIState())
    val loginState: State<LoginUIState> = _loginState

    suspend fun login(user: LoginCredential) {
        addUserUseCase(user).onEach { result ->
            when (result) {
                is Resources.IsLoading -> {
                    _loginState.value = LoginUIState(isLoading = true)
                }

                is Resources.Success -> {
                    _loginState.value = LoginUIState(isLoading = false, authKey = result.data)
                }

                is Resources.Error -> {
                    _loginState.value = LoginUIState(
                        isLoading = false,
                        error = result.message ?: "Unexpected error occurred"
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}