package com.solo4.millionerquiz.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.ads.AdSize

@Composable
fun AdBannerBottomSpacer() {
    Spacer(modifier = Modifier.height(AdSize.BANNER.height.dp + 20.dp))
}
