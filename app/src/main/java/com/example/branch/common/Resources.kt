package com.example.branch.common

sealed class Resources<T>(var data: T? = null, val message: String? = null) {

    class Success<T>(data: T) : Resources<T>(data)

    class Error<T>(message: String, data: T? = null) : Resources<T>(data, message)

    class IsLoading<T>(data: T? = null) : Resources<T>(data)
}