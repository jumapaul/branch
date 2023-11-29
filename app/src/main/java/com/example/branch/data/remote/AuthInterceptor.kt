package com.example.branch.data.remote

import com.example.branch.common.TokenManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenManger: TokenManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking {
            tokenManger.getToken().first()
        }
        val request = chain.request().newBuilder()
        request.addHeader("X-Branch-Auth-Token", "$token")
        return chain.proceed(request.build())
    }
}