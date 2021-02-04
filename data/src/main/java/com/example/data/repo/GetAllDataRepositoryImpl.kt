package com.example.data.repo

import com.example.data.apiServices.GetAllDataService
import com.example.data.base.DomainMapper
import com.example.data.database.dao.ProductDao
import com.example.data.database.entity.ProductEntity
import com.example.domain.models.Result
import com.example.domain.repositories.GetAllDataRepository

class GetAllDataRepositoryImpl(
    private val service: GetAllDataService,
    private val productDao: ProductDao
) :
    BaseRepository<Any, DomainMapper<Any>>(), GetAllDataRepository {


    override suspend fun getAllData(token: String): Result<Any> {


//        val result= fetchData(
//            apiDataProvider = {
//                val data=service.getAllData(token)
//                data.getData(
//                    fetchFromCacheAction = {
//                        ProductEntity(productDao.getAllRecords(), "success")
//                    },
//                    cacheAction = { productEntity ->
//                        productEntity.products.let { products ->
//                            run {
//                                productDao.insertAllRecords(products)
//                            }
//                        }
//                    })
//            },
//            dbDataProvider = { productDao.getAllRecords() as DomainMapper<Any> }
//        ) as Result<Response>


        return fetchDbData {
            ProductEntity(productDao.getAllRecords(), "success").mapToDomainModel()
        }
    }

}
//Unchecked cast: Result<Any> to Result<BlockResponse> --> to make it generic