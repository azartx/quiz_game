package com.solo4.millionerquiz.data.di

import com.solo4.millionerquiz.MainActivityViewModel
import com.solo4.millionerquiz.debug.AddLevelViewModel
import com.solo4.millionerquiz.ui.screens.auth.AuthViewModel
import com.solo4.millionerquiz.ui.screens.game.GameViewModel
import com.solo4.millionerquiz.ui.screens.menu.MenuViewModel
import com.solo4.millionerquiz.ui.screens.picklevel.PickLevelViewModel
import com.solo4.millionerquiz.ui.screens.profile.ProfileViewModel
import com.solo4.millionerquiz.ui.screens.score.ScoreViewModel
import com.solo4.millionerquiz.ui.screens.settings.SettingsViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { GameViewModel(get(), get(), get(), get()) }
    viewModel { AuthViewModel(androidApplication(), get(), get()) }
    viewModel { MainActivityViewModel(get()) }
    viewModel { PickLevelViewModel(get()) }
    viewModel { AddLevelViewModel() }
    viewModel { MenuViewModel(get()) }
    viewModel { SettingsViewModel(get()) }
    viewModel { ProfileViewModel(androidApplication(), get()) }
    viewModel { ScoreViewModel(get(), get()) }
}
