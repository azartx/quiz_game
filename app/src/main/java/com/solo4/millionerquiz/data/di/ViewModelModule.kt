package com.solo4.millionerquiz.data.di

import com.solo4.millionerquiz.MainActivityViewModel
import com.solo4.millionerquiz.ui.screens.auth.AuthViewModel
import com.solo4.millionerquiz.ui.screens.game.GameViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { GameViewModel(get()) }
    viewModel { AuthViewModel(get()) }
    viewModel { MainActivityViewModel(get()) }
}
