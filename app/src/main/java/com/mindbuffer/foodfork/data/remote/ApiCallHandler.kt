package com.mindbuffer.foodfork.data.remote

import com.mindbuffer.foodfork.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber

abstract class ApiCallHandler {

    suspend fun <T> safeApiCall(
        apiCall: suspend () -> T
    ): NetworkResult<T> {
        return withContext(Dispatchers.IO) {
            try {
                NetworkResult.Success(apiCall.invoke())
            }catch (throwable: Throwable){
                when(throwable){
                    is HttpException ->{
                        Timber.d("throwable HttpException")
                        NetworkResult.Failure(false, throwable.code(), throwable.response()?.errorBody()?.string().toString())
                    }
                    else -> {
                        Timber.d("throwable else")
                        NetworkResult.Failure(true, null, null)
                    }
                }
            }
        }
    }
}