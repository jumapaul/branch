package com.example.branch.data.repository

import com.example.branch.common.TokenManager
import com.example.branch.data.remote.BranchApiService
import com.example.branch.domain.model.LoginCredential
import com.example.branch.domain.model.LoginResponse
import com.example.branch.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApiService: BranchApiService,
    private val tokenManager: TokenManager
) : AuthRepository {

    override suspend fun login(login: LoginCredential): LoginResponse {
        return authApiService.login(login)
    }

    override suspend fun saveAuthToken(token: String) {
        tokenManager.saveToken(token)
    }

    override suspend fun getAuthToken(): Flow<String> {
        return tokenManager.getToken().map { it.orEmpty() }
    }
}