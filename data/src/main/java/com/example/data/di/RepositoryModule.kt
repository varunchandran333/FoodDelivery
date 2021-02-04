package com.example.data.di

import com.example.data.repo.GetAllDataRepositoryImpl
import com.example.domain.repositories.GetAllDataRepository
import com.example.data.common.utils.Connectivity
import com.example.data.common.utils.ConnectivityImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
  factory<GetAllDataRepository> { GetAllDataRepositoryImpl(get(), get()) }
  factory<Connectivity> { ConnectivityImpl(androidContext()) }
}