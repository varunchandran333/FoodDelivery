package com.example.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.database.dao.ProductDao
import com.example.data.database.entity.Product

@Database(
    version = 1,
    entities = [Product::class],
    exportSchema = false
)
abstract class OperationsDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    companion object {
        const val DB_NAME = "operations.db"
    }
}