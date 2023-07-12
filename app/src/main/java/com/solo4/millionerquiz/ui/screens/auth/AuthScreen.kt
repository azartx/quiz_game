package com.solo4.millionerquiz.ui.screens.auth

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.solo4.millionerquiz.App
import com.solo4.millionerquiz.BuildConfig
import com.solo4.millionerquiz.R
import com.solo4.millionerquiz.data.auth.AuthState
import com.solo4.millionerquiz.model.database.PreferredLevelLang
import com.solo4.millionerquiz.ui.navigation.Routes
import com.solo4.millionerquiz.ui.theme.contentPadding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(navHostController: NavHostController = rememberNavController()) {

    val viewModel: AuthViewModel = koinViewModel()

    var emailFieldText by remember { mutableStateOf("") }
    var passwordFieldText by remember { mutableStateOf("") }

    DisposableEffect(key1 = "", effect = {
        val scope = CoroutineScope(Dispatchers.Main).launch {
            viewModel.authState.collectLatest {
                if (it !is AuthState.None) {
                    navHostController.navigate(
                        if (BuildConfig.isFullApp)
                            Routes.MenuScreenRoute.name else Routes.DevScreenRoute.name
                    )
                }
            }
            viewModel.validationState.collectLatest {
                Toast.makeText(App.app, it, Toast.LENGTH_SHORT).show()
            }
        }
        onDispose { scope.cancel() }
    })

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background, RoundedCornerShape(20.dp))
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(50.dp))
            Text(
                text = "Welcome to Quiz App!",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(70.dp))
            Text(text = "Pick Questions Language: ", style = TextStyle(fontSize = 20.sp))
            Spacer(modifier = Modifier.height(18.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Image(
                    painter = painterResource(id = R.drawable.ic_flag_us),
                    contentDescription = "US",
                    modifier = Modifier
                        .size(48.dp, 38.dp)
                        .clickable {
                            viewModel.changePickedLanguage(PreferredLevelLang.en)
                        }
                        .border(
                            if (viewModel.pickedLanguage.value == PreferredLevelLang.en) BorderStroke(
                                3.dp,
                                Color.Blue
                            ) else BorderStroke(0.dp, Color.Blue)
                        )
                )
                Spacer(modifier = Modifier.width(20.dp))
                Image(
                    painter = painterResource(id = R.drawable.ic_flag_ru),
                    contentDescription = "RU",
                    modifier = Modifier
                        .size(48.dp, 38.dp)
                        .clickable {
                            viewModel.changePickedLanguage(PreferredLevelLang.ru)
                        }
                        .border(
                            if (viewModel.pickedLanguage.value == PreferredLevelLang.ru) BorderStroke(
                                3.dp,
                                Color.Blue
                            ) else BorderStroke(0.dp, Color.Blue)
                        )
                )
            }

            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = "Do you want to sign in?",
                style = TextStyle(fontSize = 20.sp)
            )
            Spacer(modifier = Modifier.height(20.dp))

            TextField(
                value = emailFieldText,
                onValueChange = { emailFieldText = it },
                placeholder = { Text(text = "Email") })
            TextField(
                value = passwordFieldText,
                onValueChange = { passwordFieldText = it },
                placeholder = { Text(text = "Password") })
            Spacer(modifier = Modifier.height(10.dp))
            Button(modifier = Modifier
                .padding(horizontal = 30.dp)
                .fillMaxWidth(), shape = RoundedCornerShape(10.dp), onClick = {
                viewModel.signInByEmail(emailFieldText.trim(), passwordFieldText.trim())
            }) {
                Text(text = "Sign in with email")
            }

            Spacer(modifier = Modifier.height(15.dp))
            Text(text = "Or", style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold))
            Spacer(modifier = Modifier.height(15.dp))
            Button(modifier = Modifier
                .padding(horizontal = 30.dp)
                .fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                onClick = { viewModel.signInAsAnon() }) {
                Text(text = "Continue as Anonymous")
            }
            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}
