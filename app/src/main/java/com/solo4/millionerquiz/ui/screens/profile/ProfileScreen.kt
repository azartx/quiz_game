package com.solo4.millionerquiz.ui.screens.profile

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import coil.compose.AsyncImage
import com.solo4.millionerquiz.App
import com.solo4.millionerquiz.R
import com.solo4.millionerquiz.data.MediaManager
import com.solo4.millionerquiz.ui.navigation.Routes
import com.solo4.millionerquiz.ui.theme.contentPadding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navHostController: NavHostController) {
    val viewModel: ProfileViewModel = koinViewModel()

    val authState by viewModel.authState.collectAsState()

    var emailFieldText by remember { mutableStateOf("") }
    var passwordFieldText by remember { mutableStateOf("") }

    val filePickerCallback = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val documentsUri = result.data?.let { data ->
                    data.clipData?.let { clipData ->
                        val itemCount = clipData.itemCount
                        0.until(itemCount).map { clipData.getItemAt(it).uri }
                    } ?: data.data?.let { listOf(it) } ?: emptyList()
                } ?: emptyList()

                if (documentsUri.isNotEmpty()) {
                    viewModel.updateUserPhoto(documentsUri.first())
                }
            }
        }
    )

    DisposableEffect(key1 = "", effect = {
        val scope = CoroutineScope(Dispatchers.Main).launch {
            launch {
                viewModel.logOutEvent.collectLatest {
                    navHostController.navigate(
                        route = Routes.AuthScreenRoute.name,
                        navOptions = NavOptions.Builder()
                            .setPopUpTo(0, true)
                            .build()
                    )
                }
            }
            launch {
                viewModel.validationState.collectLatest {
                    Toast.makeText(App.app, it, Toast.LENGTH_SHORT).show()
                }
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Image(
                    modifier = Modifier.clickable {
                        navHostController.popBackStack()
                    },
                    painter = painterResource(id = R.drawable.ic_arrow_bask),
                    contentDescription = "Back"
                )
            }
            Text(
                text = stringResource(id = R.string.profile),
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(38.dp))

            AsyncImage(
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(360.dp))
                    .background(Color.LightGray, RoundedCornerShape(360.dp))
                    .clickable {
                        MediaManager.playClick()
                        val intent = Intent()
                            .setType("image/*")
                            .setAction(Intent.ACTION_GET_CONTENT)
                            .putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
                        filePickerCallback.launch(intent)
                    },
                model = viewModel.getUserImage(),
                contentScale = ContentScale.Crop,
                contentDescription = stringResource(id = R.string.user_profile_image),
                error = painterResource(id = R.drawable.ic_user_placeholder),
                placeholder = painterResource(id = R.drawable.ic_user_placeholder)
            )
            Spacer(modifier = Modifier.height(20.dp))

            TextField(
                value = viewModel.usernameTextField.value,
                onValueChange = { viewModel.usernameTextField.value = it },
                placeholder = { Text(text = stringResource(R.string.username)) })
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                modifier = Modifier
                    .padding(horizontal = 30.dp)
                    .fillMaxWidth(),
                onClick = {
                    MediaManager.playClick()
                    viewModel.setNewUsername(viewModel.usernameTextField.value)
                },
                shape = RoundedCornerShape(3.dp)
            ) {
                Text(text = stringResource(R.string.submit_new_name))
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (authState.user.isAnonymous) {
                TextField(
                    value = viewModel.usernameTextField.value,
                    onValueChange = { viewModel.usernameTextField.value = it },
                    placeholder = { Text(text = stringResource(id = R.string.username)) })
                TextField(
                    value = emailFieldText,
                    onValueChange = { emailFieldText = it },
                    placeholder = { Text(text = stringResource(R.string.email)) })
                TextField(
                    value = passwordFieldText,
                    onValueChange = { passwordFieldText = it },
                    placeholder = { Text(text = stringResource(R.string.password)) })
                Spacer(modifier = Modifier.height(10.dp))
                Button(modifier = Modifier
                    .padding(horizontal = 30.dp)
                    .fillMaxWidth()
                    .fillMaxWidth(),
                    shape = RoundedCornerShape(3.dp),
                    onClick = {
                        MediaManager.playClick()
                        viewModel.continueWithEmail(
                            emailFieldText.trim(), passwordFieldText.trim()
                        )
                    }) {
                    Text(text = stringResource(R.string.sign_in_with_email))
                }
            } else {
                Button(
                    modifier = Modifier
                        .padding(horizontal = 30.dp)
                        .fillMaxWidth(),
                    onClick = {
                        MediaManager.playClick()
                        viewModel.loginAsAnon()
                    },
                    shape = RoundedCornerShape(3.dp)
                ) {
                    Text(text = stringResource(R.string.continue_as_anonymous))
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                modifier = Modifier
                    .padding(horizontal = 30.dp)
                    .fillMaxWidth(),
                onClick = {
                    MediaManager.playClick()
                    viewModel.logOut()
                },
                shape = RoundedCornerShape(3.dp)
            ) {
                Text(text = stringResource(R.string.log_out))
            }
        }
    }
}
