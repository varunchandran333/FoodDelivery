package com.example.fooddelivery.ui.main.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.data.constants.CATEGORY.BAKERY
import com.example.data.constants.CATEGORY.FRUITS
import com.example.data.constants.CATEGORY.NON_VEG
import com.example.data.constants.CATEGORY.VEG
import com.example.data.database.OperationsDatabase
import com.example.data.database.entity.Product
import com.example.domain.models.Response
import com.example.domain.models.onFailure
import com.example.domain.models.onSuccess
import com.example.domain.usecases.GetAllDataUseCase
import com.example.fooddelivery.R
import com.example.fooddelivery.ui.main.base.BaseViewModel
import com.example.fooddelivery.ui.main.base.Error
import com.example.fooddelivery.ui.main.base.NoInternetState
import com.example.fooddelivery.ui.main.base.Success
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.core.inject
import java.util.concurrent.Callable
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executors
import kotlin.random.Random

open class MainViewModel(private val usecase: GetAllDataUseCase) : BaseViewModel<Response>() {

    private val database by inject<OperationsDatabase>()
    private var cartList = ArrayList<Product>()
    private val _productListLiveData = MutableLiveData<List<Product>>()
    val productListLiveData: LiveData<List<Product>>
        get() = _productListLiveData

    fun getAllData(token: String) = executeUseCase(
        {
            usecase.invoke(token)
                .onSuccess {
                    if (it is Response) {
                        viewState.value = Success(it)
                    } else
                        viewState.value = Error("Corrupted data received")
                }
                .onFailure {
                    viewState.value = Error(it.reason)
                }
        },
        {
            viewState.value = NoInternetState()
        }
    )

    val allRecords: List<Product>?
        get() = try {
            Executors.newSingleThreadExecutor()
                .submit(Callable {
                    database.productDao()
                        .getAllCartRecordsData()
                }).get()
        } catch (e: InterruptedException) {
            e.printStackTrace()
            null
        } catch (e: ExecutionException) {
            e.printStackTrace()
            null
        }

    fun updateCart(product: Product) {
        if (cartList.any { it.productCode == product.productCode }) {
            cartList.map {
                if (it.productCode == product.productCode) {
                    it.count += 1
                }
            }
        } else {
            product.count += 1
            cartList.add(product)
        }
        _productListLiveData.value = cartList
        GlobalScope.launch(Dispatchers.IO) {
            database.productDao().insertAllRecords(cartList)
        }
    }

    fun updateCartWithList(products: List<Product>) {
        cartList = ArrayList()
        cartList.addAll(products.filter { it.count > 0 })
        _productListLiveData.value = cartList
    }

    fun deleteCartItem(product: Product) {
        product.count = 0
        GlobalScope.launch(Dispatchers.IO) {
            database.productDao().insertRecord(product)
        }
    }

    fun insertData() {
        val dbList = allRecords
        if (dbList?.isEmpty() == true) {
            val productList = ArrayList<Product>()
            var product =
                Product(
                    "${Random.nextInt()}${Random.nextInt()}B",
                    "Chicken Fry",
                    299,
                    350,
                    R.drawable.chicken,
                    NON_VEG,
                    "Southern fried chicken, also simply known as fried chicken, is a dish consisting of chicken pieces"
                )
            productList.add(product)
            product =
                Product(
                    "${Random.nextInt()}${Random.nextInt()}C",
                    "Steak",
                    99,
                    150,
                    R.drawable.steak,
                    NON_VEG,
                    "A steak is a meat generally sliced across the muscle fibers, potentially including a bone"
                )
            productList.add(product)
            product =
                Product(
                    "${Random.nextInt()}${Random.nextInt()}C",
                    "Pizza",
                    99,
                    150,
                    R.drawable.pizza,
                    VEG,
                    "Pizza chips (British and Commonwealth English, Hiberno-English), finger chips (Indian English), or French-fried potatoes, are batonnet or allumette-cut deep-fried potatoes"
                )
            productList.add(product)
            product = Product(
                "${Random.nextInt()}${Random.nextInt()}A",
                "Apples",
                199,
                250,
                R.drawable.apple,
                FRUITS,
                "An apple is an edible fruit produced by an apple tree. Apple trees are cultivated worldwide and are the most widely grown species in the genus Malus."
            )
            productList.add(product)
            product =
                Product(
                    "${Random.nextInt()}${Random.nextInt()}B",
                    "Egg Biscuit",
                    299,
                    350,
                    R.drawable.biscuit,
                    BAKERY,
                    "Egg Biscuit is the base upon which we created our classic bird food fishing bait ingredients Red Factor and Nectarblend."
                )
            productList.add(product)

            product =
                Product(
                    "${Random.nextInt()}${Random.nextInt()}C",
                    "Egg BullsEye",
                    99,
                    150,
                    R.drawable.bullseye,
                    NON_VEG,
                    "Sunny Side Up Egg, Half Boiled Egg, Egg Half Fry, Single-Fried Egg, Half Fried Egg"
                )
            productList.add(product)
            product =
                Product(
                    "${Random.nextInt()}${Random.nextInt()}C",
                    "French Fries",
                    99,
                    150,
                    R.drawable.french,
                    VEG,
                    "French fries, or simply fries (North American English), chips (British and Commonwealth English, Hiberno-English), finger chips (Indian English), or French-fried potatoes, are batonnet or allumette-cut deep-fried potatoes"
                )
            productList.add(product)
            product = Product(
                "${Random.nextInt()}${Random.nextInt()}A",
                "Orange",
                199,
                250,
                R.drawable.orange,
                FRUITS,
                "An Orange is an edible fruit produced by an orange tree. Orange trees are cultivated worldwide and are the most widely grown species in the genus Malus."
            )
            productList.add(product)

            GlobalScope.launch(Dispatchers.IO) {
                database.productDao().insertAllRecords(productList)
            }
        }
    }

    fun getTotalPrice(products: List<Product>): Int {
        var total = 0
        val list = products.filter { it.count > 0 }
        list.forEach {
            total += (it.count * it.sellingPrice)
        }
        return total
    }
}