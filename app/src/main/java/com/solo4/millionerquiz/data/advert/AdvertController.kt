package com.solo4.millionerquiz.data.advert

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.solo4.millionerquiz.App
import com.solo4.millionerquiz.BuildConfig
import com.solo4.millionerquiz.data.advert.AdvertController.Companion.ROUTE_WITHOUT_AD_BANNER_IDENTIFIER
import com.solo4.millionerquiz.data.analytics.Event
import com.solo4.millionerquiz.data.analytics.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AdvertController {

    val showAdBannerState = MutableStateFlow(true)

    var fullScreenAd: InterstitialAd? = null

    var showFullScreenAdCount = 0

    suspend fun handleAdBannerVisibility(route: String) {
        showAdBannerState.emit(!route.contains(ROUTE_WITHOUT_AD_BANNER_IDENTIFIER))
    }

    suspend fun loadFullscreenAdvert() = suspendCoroutine { cont ->
        InterstitialAd.load(
            App.app,
            BuildConfig.ad_interstitial_id,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    Logger.e(TAG, "Failed loading fullscreen AD: $error", Event.advert.name)
                    cont.resume(false)
                }

                override fun onAdLoaded(ad: InterstitialAd) {
                    fullScreenAd = ad
                    cont.resume(true)
                }
            })
    }

    suspend fun showFullscreenAd(activity: Activity) = suspendCoroutine { cont ->
        ++showFullScreenAdCount
        if (!BuildConfig.showAd) {
            Toast.makeText(App.app, "Fullscreen AD here!", Toast.LENGTH_SHORT).show()
            return@suspendCoroutine
        }
        if (fullScreenAd == null) {
            Log.e(TAG, "fullScreenAd is null")
            cont.resume(false)
            return@suspendCoroutine
        }
        if ((showFullScreenAdCount % 2) == 0) {
            Log.e(TAG, "Ad skipped by ad queue")
            cont.resume(true)
            return@suspendCoroutine
        }
        Log.e(TAG, "Start showing fullscreen AD")
        fullScreenAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdClicked() {
                Logger.e(TAG, "Ad was clicked.", Event.advert.name)
            }

            override fun onAdDismissedFullScreenContent() {
                Logger.e(TAG, "Fullscreen AD was dismissed.", Event.advert.name)
                cont.resume(true)
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                // Called when ad fails to show.
                Log.e(TAG, "Ad failed to show fullscreen content: $adError")
                cont.resume(false)
            }

            override fun onAdImpression() {
                // Called when an impression is recorded for an ad.
                Log.d(TAG, "Ad recorded an impression.")
            }

            override fun onAdShowedFullScreenContent() {
                // Called when ad is shown.
                Log.d(TAG, "Ad showed fullscreen content.")
            }
        }
        fullScreenAd?.show(activity)
    }

    companion object {
        const val TAG = "AdvertController"

        //const val banner_id = "ca-app-pub-2425331811935876/4874112919"
        //const val banner_id = "ca-app-pub-3940256099942544/1033173712"

        const val ROUTE_WITHOUT_AD_BANNER_IDENTIFIER = "-WITHOUT_AD_BANNER"
    }
}

fun String.routeWithoutAD() = this.plus(ROUTE_WITHOUT_AD_BANNER_IDENTIFIER)
