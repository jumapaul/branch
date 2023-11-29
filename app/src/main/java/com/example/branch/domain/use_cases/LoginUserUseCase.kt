package com.example.branch.domain.use_cases

import com.example.branch.common.Resources
import com.example.branch.domain.model.LoginCredential
import com.example.branch.domain.model.LoginResponse
import com.example.branch.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import javax.inject.Inject

class LoginUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {

    suspend operator fun invoke(user: LoginCredential): Flow<Resources<LoginResponse>> = flow {
        try {
            emit(Resources.IsLoading())
            val apiUser = repository.login(user)
            repository.saveAuthToken(apiUser.auth_token)
            emit(Resources.Success(apiUser))
        } catch (e: IOException) {
            emit(Resources.Error("Please check internet connection"))
        } catch (e: Exception) {
            emit(Resources.Error(message = "Something went wrong"))
        }
    }
}