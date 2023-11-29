package com.example.branch.domain.repository

import com.example.branch.domain.model.LoginCredential
import com.example.branch.domain.model.LoginResponse
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(login: LoginCredential): LoginResponse

    suspend fun saveAuthToken(token: String)

    suspend fun getAuthToken(): Flow<String>
}