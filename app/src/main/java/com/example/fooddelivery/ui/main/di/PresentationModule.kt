package com.example.fooddelivery.ui.main.di

import com.example.fooddelivery.ui.main.ui.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel { MainViewModel(get()) }
}
