package com.example.data.di

import androidx.room.Room
import com.example.data.database.OperationsDatabase
import com.example.data.database.entity.Product
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private const val DB = "database"

val databaseModule = module {
    single {
        Room.databaseBuilder(androidContext(), OperationsDatabase::class.java, DB)
            .addMigrations( Product.MIGRATION_1_2).build()
    }
    factory { get<OperationsDatabase>().productDao() }
}