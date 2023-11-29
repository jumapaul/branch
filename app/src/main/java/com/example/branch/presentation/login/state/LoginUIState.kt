package com.example.branch.presentation.login.state

import com.example.branch.domain.model.LoginResponse

data class LoginUIState(
    val isLoading: Boolean = false,
    val authKey: LoginResponse? = null,
    val error: String? = null
)
