package com.example.data.base

import com.example.domain.models.*
import com.google.gson.Gson
import retrofit2.Response
import java.io.IOException

interface DomainMapper<T : Any> {
    fun mapToDomainModel(): T
}
interface RoomMapper<out T : Any> {
    fun mapToRoomEntity(): T
}

inline fun <T : Any> Response<T>.onSuccess(action: (T) -> Unit): Response<T> {
    if (isSuccessful) body()?.run(action)
    return this
}

inline fun <T : Any> Response<T>.onFailure(action: (HttpError) -> Unit) {
    if (!isSuccessful)
        errorBody()?.run {
            action(HttpError(Throwable(this.string()), code()))
        }
}

/**
 * Use this if you need to cache data after fetching it from the api, or retrieve something from cache
 */

inline fun <T : RoomMapper<R>, R : DomainMapper<U>, U : Any> Response<T>.getData(
    cacheAction: (R) -> Unit,
    fetchFromCacheAction: () -> R
): Result<U> {
    try {
        onSuccess {
            val databaseEntity = it.mapToRoomEntity()
            cacheAction(databaseEntity)
            return Success(databaseEntity.mapToDomainModel())
        }
        onFailure {
            val cachedModel = fetchFromCacheAction()
            return Success(cachedModel.mapToDomainModel())
        }
        return Failure(ApiErrorResponse())
    } catch (e: IOException) {
        return Failure(ApiErrorResponse())
    }
}

/**
 * Use this when communicating only with the api service
 */
fun <T : DomainMapper<R>, R : Any> Response<T>.getData(): Result<R> {
    var apiErrorResponse = ApiErrorResponse()
    try {
        onSuccess { return Success(it.mapToDomainModel()) }
        onFailure {
            if (!it.throwable.message.isNullOrEmpty()) {
                apiErrorResponse = Gson().fromJson(
                    it.throwable.message!!,
                    ApiErrorResponse::class.java
                )
            }
            return Failure(apiErrorResponse)
        }
        return Failure(apiErrorResponse)
    } catch (e: IOException) {
        return Failure(apiErrorResponse)
    }
}

