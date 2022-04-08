package com.mglb.demoadmob

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import kotlinx.android.synthetic.main.activity_secon.*
import javax.security.auth.login.LoginException

class InterstitialActivity : AppCompatActivity() {
    private var mInterstitialAd: InterstitialAd? = null
    private  var TAG = "InterstitialActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_secon)
        setFullScreen ()
        var adRequest = AdRequest.Builder().build()

        InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.e(TAG, "onAdFailedToLoad: ", )
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Log.e(TAG, "onAdLoaded: ")
                mInterstitialAd = interstitialAd
            }
        })
        btn_show.setOnClickListener {
            show()
        }
    }
   private fun setFullScreen (){
       mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
           override fun onAdDismissedFullScreenContent() {
               Log.e(TAG, "onAdDismissedFullScreenContent: " )
           }

           override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
               Log.e(TAG, "onAdFailedToShowFullScreenContent: " )
           }

           override fun onAdShowedFullScreenContent() {
               Log.e(TAG, "onAdShowedFullScreenContent: " )
               mInterstitialAd = null
           }
       }

    }
    private fun show(){
        if (mInterstitialAd != null) {
            mInterstitialAd?.show(this)
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.")
        }
    }

}