package com.example.domain.repositories

import com.example.domain.models.Response
import com.example.domain.models.Result

interface GetAllDataRepository {
    suspend fun getAllData(token: String): Result<Any>
}