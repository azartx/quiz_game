package com.solo4.millionerquiz.ui.screens.menu

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.solo4.millionerquiz.R
import com.solo4.millionerquiz.model.auth.User
import com.solo4.millionerquiz.ui.navigation.Routes
import com.solo4.millionerquiz.ui.theme.QuizGameTheme
import com.solo4.millionerquiz.ui.theme.contentPadding
import org.koin.androidx.compose.navigation.koinNavViewModel

@Composable
fun MenuScreen(navHostController: NavHostController = rememberNavController()) {

    val viewModel: MenuViewModel = koinNavViewModel()

    val authState by viewModel.authState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background, RoundedCornerShape(20.dp))
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .background(
                        MaterialTheme.colorScheme.onBackground,
                        RoundedCornerShape(360.dp)
                    )
            ) {
                AsyncImage(
                    modifier = Modifier
                        .size(120.dp)
                        .align(Alignment.Center),
                    model = viewModel.getUserImage(),
                    contentDescription = "User profile image",
                    error = painterResource(id = R.drawable.ic_user_placeholder),
                    placeholder = painterResource(id = R.drawable.ic_user_placeholder)
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = authState.user.name)
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    navHostController.navigate(
                        Routes.PickLevelRoute.nameWithArgs(User.currentLevel.toString())
                    )
                }
            ) {
                Text(text = "Играть")
            }
            Spacer(modifier = Modifier.height(50.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp),
                    shape = RoundedCornerShape(20.dp),
                    onClick = {

                    }
                ) {
                    Text(text = "Settings", color = Color.Black)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp),
                    shape = RoundedCornerShape(20.dp),
                    onClick = {

                    }
                ) {
                    Text(text = "About", color = Color.Black)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    QuizGameTheme {
        MenuScreen()
    }
}
