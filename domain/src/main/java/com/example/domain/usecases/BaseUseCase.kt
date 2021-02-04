package com.example.domain.usecases

import com.example.domain.models.Result


interface BaseUseCase<T : Any, R : Any> {
    suspend operator fun invoke(request: T): Result<R>
}