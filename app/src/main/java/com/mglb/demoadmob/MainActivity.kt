package com.mglb.demoadmob

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn_banner_ads).setOnClickListener {
            startActivity(Intent(this, BannerAdActivity::class.java))
        }
        findViewById<Button>(R.id.btn_interstitial_ads).setOnClickListener {
            startActivity(Intent(this, InterstitialActivity::class.java))
        }
        findViewById<Button>(R.id.btn_native_ads).setOnClickListener {
            startActivity(Intent(this, NativeAdActivity::class.java))
        }
    }

}