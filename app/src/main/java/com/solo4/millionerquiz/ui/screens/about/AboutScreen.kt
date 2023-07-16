package com.solo4.millionerquiz.ui.screens.about

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.solo4.millionerquiz.BuildConfig
import com.solo4.millionerquiz.R
import com.solo4.millionerquiz.data.MediaManager
import com.solo4.millionerquiz.ui.components.AdBannerBottomSpacer
import com.solo4.millionerquiz.ui.theme.bgBrush
import com.solo4.millionerquiz.ui.theme.contentPadding

@Composable
fun AboutScreen(navHostController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(bgBrush, RoundedCornerShape(20.dp))
                .padding(20.dp),
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
                        MediaManager.playClick()
                        navHostController.popBackStack()
                    },
                    painter = painterResource(id = R.drawable.ic_arrow_bask),
                    contentDescription = "Back"
                )
            }
            Text(
                text = stringResource(R.string.about_app_title),
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(38.dp))

            Text(text = "${stringResource(R.string.about_app_body)} ${BuildConfig.VERSION_NAME}_${BuildConfig.VERSION_CODE}")
            AdBannerBottomSpacer()
        }
    }
}
