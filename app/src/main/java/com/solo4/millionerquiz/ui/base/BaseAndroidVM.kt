package com.solo4.millionerquiz.ui.base

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.solo4.millionerquiz.model.ScreenState
import org.koin.java.KoinJavaComponent.get

open class BaseAndroidVM : AndroidViewModel(get(Application::class.java)) {
    val screenState = mutableStateOf<ScreenState>(ScreenState.None)
}
