package com.mindbuffer.foodfork.utils

sealed class NetworkResult<out T> {

    data class Success<out T>(val data: T): NetworkResult<T>()
    data class Failure(
        val isNetworkError: Boolean,
        val errorCode: Int?,
        val errorMsg: String?
    ): NetworkResult<Nothing>()
    class Loading<T> : NetworkResult<T>()

}
