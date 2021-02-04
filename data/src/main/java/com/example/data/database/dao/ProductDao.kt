package com.example.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.database.entity.Product

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecord(product: Product?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllRecords(records: List<Product>)

    @Query("SELECT * FROM  Product")
    fun getAllRecords(): List<Product>

    @Query("SELECT * FROM  Product WHERE count > 0")
    fun getAllCartRecordsData(): List<Product>

    @Query("DELETE FROM Product")
    fun clearAll()
}