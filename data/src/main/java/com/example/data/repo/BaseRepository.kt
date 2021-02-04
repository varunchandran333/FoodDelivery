package com.example.data.repo

import com.example.data.base.DomainMapper
import com.example.data.common.utils.Connectivity
import com.example.domain.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import org.koin.core.inject

abstract class BaseRepository<T : Any, R : DomainMapper<T>> : KoinComponent {
    private val connectivity: Connectivity by inject()

    /**
     * Use this if you need to cache data after fetching it from the api, or retrieve something from cache
     */
    protected suspend fun fetchData(
        apiDataProvider: suspend () -> Result<T>,
        dbDataProvider: suspend () -> R
    ): Result<T> {
        return if (connectivity.hasNetworkAccess()) {
            withContext(Dispatchers.IO) {
                apiDataProvider()
            }
        } else {
            withContext(Dispatchers.IO) {
                val dbResult = dbDataProvider()
                if (dbResult != null) Success(dbResult.mapToDomainModel()) else Failure(
                    ApiErrorResponse()
                )
            }
        }
    }

    /**
     * Use this when communicating only with the api service
     */
    protected suspend fun fetchData(dataProvider: suspend () -> Result<T>): Result<T> {
        return if (connectivity.hasNetworkAccess()) {
            withContext(Dispatchers.IO) {
                dataProvider()
            }
        } else {
            Failure(ApiErrorResponse())
        }
    }

    /**
     * Use this when communicating only with the database
     */
    protected suspend fun fetchDbData(dataProvider: suspend () -> T): Result<T> {
        val success= withContext(Dispatchers.IO) {
            val dbResult = dataProvider()
            Success(dbResult)
        }
        return success

    }
}