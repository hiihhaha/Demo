package com.mglb.demoadmob

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError

class BannerAdActivity : AppCompatActivity() {
    private var mAdView2: AdView? = null

    private var mAdView: AdView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_banner_ad)
        setupAds()
    }

    private fun setupAds() {
        mAdView2 = findViewById(R.id.adView2)
        mAdView = findViewById(R.id.adView)

        val adRequest = AdRequest.Builder().build()
        mAdView?.let { adView ->
            adView.loadAd(adRequest)
            setAdModListener(adView)
        }

        mAdView2?.let { adView ->
            adView.loadAd(adRequest)
            setAdModListener(adView)
        }
    }

    private fun setAdModListener(adView: AdView) {
        adView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                Log.e("======> ", "onAdLoaded: ")
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.e("======> ", "onAdFailedToLoad: $adError ")
            }

            override fun onAdOpened() {
                Log.e("======> ", "onAdOpened")
            }

            override fun onAdClicked() {
                Log.e("======> ", "onAdClicked")
            }

            override fun onAdClosed() {
                Log.e("======> ", "onAdClosed")
            }
        }
    }
}