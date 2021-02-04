package com.example.data.database.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "Product")
data class Product(
    @JvmField
    @SerializedName("productCode")
    @PrimaryKey
    @ColumnInfo(name = "productCode")
    var productCode: String,

    @SerializedName("product_name")
    @ColumnInfo(name = "product_name")

    var productName: String? = null,

    @SerializedName("market_price")
    @ColumnInfo(name = "market_price")

    var marketPrice: Int = 0,

    @SerializedName("selling_price")
    @ColumnInfo(name = "selling_price")

    var sellingPrice: Int = 0,

    @SerializedName("product_style_image")
    @ColumnInfo(name = "product_style_image")

    var productStyleImage: Int = 0,

    @SerializedName("product_category")
    @ColumnInfo(name = "product_category")

    var productCategory: Int = 0,

    @SerializedName("product_description")
    @ColumnInfo(name = "product_description")
    var productDescription: String? = null,

    @ColumnInfo(name = "count")
    var count: Int=0

) : Parcelable {
    companion object {

        @JvmField
        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {

            }
        }
    }
}