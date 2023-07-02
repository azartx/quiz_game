package com.solo4.millionerquiz.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.solo4.millionerquiz.data.auth.AuthState
import com.solo4.millionerquiz.data.auth.Authenticated
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(navHostController: NavHostController = rememberNavController()) {

    val viewModel: AuthViewModel = koinViewModel()

    var showCredentialFields by remember { mutableStateOf(false) }
    var emailFieldText by remember { mutableStateOf("") }
    var passwordFieldText by remember { mutableStateOf("") }

    val screenState by viewModel.authState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (screenState is Authenticated) {
            Text(text = (screenState as? Authenticated)?.currentUser?.email ?: "Unknown email")
            Text(text = if ((screenState as? Authenticated)?.currentUser?.isAnonymous == true) "Anonymous" else "Signed user")
        }

        if (screenState !is AuthState.ByEmail) {
            Button(onClick = { viewModel.signInAsAnon() }) {
                Text(text = "Sign in as Anonymous")
            }

            Button(onClick = { showCredentialFields = showCredentialFields.not() }) {
                Text(text = "Sign in by email")
            }

            if (showCredentialFields) {
                Spacer(modifier = Modifier.height(30.dp))
                TextField(value = emailFieldText, onValueChange = { emailFieldText = it })
                TextField(value = passwordFieldText, onValueChange = { passwordFieldText = it })
                Spacer(modifier = Modifier.height(10.dp))
                Button(onClick = {
                    viewModel.signInByEmail(
                        emailFieldText.trim(),
                        passwordFieldText.trim()
                    )
                }) {
                    Text(text = "Sign in")
                }
            }
        } else {
            Button(onClick = { viewModel.logOut() }) {
                Text(text = "Log out")
            }
        }
    }
}
