package com.solo4.millionerquiz.ui.screens.menu

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.solo4.millionerquiz.App
import com.solo4.millionerquiz.R
import com.solo4.millionerquiz.data.MediaManager
import com.solo4.millionerquiz.model.auth.User
import com.solo4.millionerquiz.ui.components.AdBannerBottomSpacer
import com.solo4.millionerquiz.ui.navigation.Routes
import com.solo4.millionerquiz.ui.theme.Blue80
import com.solo4.millionerquiz.ui.theme.GreenPickAnswer
import com.solo4.millionerquiz.ui.theme.GreenRightAnswer
import com.solo4.millionerquiz.ui.theme.QuizGameTheme
import com.solo4.millionerquiz.ui.theme.bgBrush
import com.solo4.millionerquiz.ui.theme.buttonColors
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
                .background(bgBrush, RoundedCornerShape(20.dp))
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(360.dp))
                    .background(Color.LightGray, RoundedCornerShape(360.dp))
                    .clickable {
                        Toast
                            .makeText(
                                App.app,
                                App.app.getString(R.string.meaagse_change_profile_photo),
                                Toast.LENGTH_SHORT
                            )
                            .show()
                    },
                model = viewModel.getUserImage(),
                contentScale = ContentScale.Crop,
                contentDescription = stringResource(R.string.user_profile_image),
                error = painterResource(id = R.drawable.placeholder_avatar),
                placeholder = painterResource(id = R.drawable.placeholder_avatar)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = authState.user.name)
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                colors = buttonColors(),
                shape = RoundedCornerShape(10.dp),
                onClick = {
                    MediaManager.playClick()
                    navHostController.navigate(
                        Routes.PickLevelRoute.nameWithArgs(User.lastCompletedLevel.value.toString())
                    )
                }
            ) {
                Text(text = stringResource(R.string.play))
            }
            Spacer(modifier = Modifier.height(50.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = buttonColors(),
                    onClick = {
                        MediaManager.playClick()
                        navHostController.navigate(Routes.ProfileScreenRoute.name)
                    }
                ) {
                    Text(text = stringResource(R.string.profile), color = Color.Black)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = buttonColors(),
                    onClick = {
                        MediaManager.playClick()
                        navHostController.navigate(Routes.ScoreScreenRoute.name)
                    }
                ) {
                    Text(text = stringResource(R.string.score), color = Color.Black)
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = buttonColors(),
                    onClick = {
                        MediaManager.playClick()
                        navHostController.navigate(Routes.SettingsScreenRoute.name)
                    }
                ) {
                    Text(text = stringResource(R.string.settings), color = Color.Black)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = buttonColors(),
                    onClick = {
                        MediaManager.playClick()
                        navHostController.navigate(Routes.AboutScreenRoute.name)
                    }
                ) {
                    Text(text = stringResource(R.string.about), color = Color.Black)
                }
            }
            AdBannerBottomSpacer()
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
