package com.solo4.millionerquiz.ui.screens.settings

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.solo4.millionerquiz.R
import com.solo4.millionerquiz.model.database.PreferredLevelLang
import com.solo4.millionerquiz.ui.components.AdBannerBottomSpacer
import com.solo4.millionerquiz.ui.theme.contentPadding
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreen(navHostController: NavHostController) {
    val viewModel: SettingsViewModel = koinViewModel()
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
                text = "Settings",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(70.dp))
            Text(text = stringResource(R.string.change_questions_language), style = TextStyle(fontSize = 20.sp))
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
            AdBannerBottomSpacer()
        }
    }
}
