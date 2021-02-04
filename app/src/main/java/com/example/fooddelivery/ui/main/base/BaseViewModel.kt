package com.example.fooddelivery.ui.main.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.common.utils.Connectivity
import com.example.data.database.OperationsDatabase
import com.example.data.database.entity.Product
import com.example.fooddelivery.ui.main.utils.launch
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.net.UnknownHostException

abstract class BaseViewModel<T : Any> : ViewModel(), KoinComponent {

    private val connectivity: Connectivity by inject()

    val viewState = MutableLiveData<ViewState<T>>()
    val view_state: LiveData<ViewState<T>>
        get() = viewState
//
//    protected val _viewEffects = MutableLiveData<E>()
//    val view_effects: LiveData<E>
//        get() = _viewEffects

    protected fun executeUseCase(action: suspend () -> Unit, noInternetAction: () -> Unit) {
        val handler = CoroutineExceptionHandler { context, exception ->
            println("Caught $exception")
            if (exception is UnknownHostException) {
                launch {
                    noInternetAction()
                }
            }

        }
        viewState.value = Loading()
        if (connectivity.hasNetworkAccess()) {
            viewModelScope.launch(handler) {
                launch(Dispatchers.Main) {
                    action()
                }
            }
        } else {
            noInternetAction()
        }
    }

    protected fun executeUseCase(action: suspend () -> Unit) {
        viewState.value = Loading()
        launch { action() }
    }
}