package com.solo4.millionerquiz.data.di

import com.solo4.millionerquiz.MainActivityViewModel
import com.solo4.millionerquiz.debug.AddLevelViewModel
import com.solo4.millionerquiz.ui.screens.auth.AuthViewModel
import com.solo4.millionerquiz.ui.screens.game.GameViewModel
import com.solo4.millionerquiz.ui.screens.menu.MenuViewModel
import com.solo4.millionerquiz.ui.screens.picklevel.PickLevelViewModel
import org.koin.android.BuildConfig
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { GameViewModel(get(), get()) }
    viewModel { AuthViewModel(get()) }
    viewModel { MainActivityViewModel(get()) }
    viewModel { PickLevelViewModel(get(), get()) }
    viewModel { AddLevelViewModel() }
    viewModel { MenuViewModel(get()) }
}
