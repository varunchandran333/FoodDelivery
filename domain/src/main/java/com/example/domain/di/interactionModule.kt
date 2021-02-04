package com.example.domain.di

import com.example.domain.usecases.GetAllDataUseCaseImpl
import com.example.domain.usecases.GetAllDataUseCase
import org.koin.dsl.module

val interactionModule = module {
    factory<GetAllDataUseCase> { GetAllDataUseCaseImpl(get()) }
}