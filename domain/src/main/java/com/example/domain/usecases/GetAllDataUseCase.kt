package com.example.domain.usecases

import com.example.domain.models.Response
import com.example.domain.models.Result


interface GetAllDataUseCase : BaseUseCase<String, Any> {

    override suspend fun invoke(request: String): Result<Any>
}