package com.example.domain.usecases

import com.example.domain.models.Response
import com.example.domain.models.Result
import com.example.domain.repositories.GetAllDataRepository

class GetAllDataUseCaseImpl(private val repository: GetAllDataRepository) :
    GetAllDataUseCase {

    override suspend fun invoke(request: String): Result<Any> =
        repository.getAllData(request)

}