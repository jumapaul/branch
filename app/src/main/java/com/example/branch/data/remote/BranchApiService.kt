package com.example.branch.data.remote

import com.example.branch.domain.model.LoginCredential
import com.example.branch.domain.model.LoginResponse
import com.example.branch.domain.model.MessagesDtoItem
import com.example.branch.domain.model.ThreadDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface BranchApiService {
    @POST("api/login")
    suspend fun login(
        @Body post: LoginCredential
    ): LoginResponse

    @GET("api/messages")
    suspend fun getMessages(): ArrayList<MessagesDtoItem>

    @POST("api/messages")
    suspend fun createMessage(
        @Body threadDto: ThreadDto
    ): MessagesDtoItem
}