package com.mglb.demoadmob

import android.annotation.SuppressLint
import android.app.Application
import android.provider.Settings
import android.util.Log
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration

class AppDemoAds : Application() {

    @SuppressLint("HardwareIds")
    override fun onCreate() {
        super.onCreate()
        // bắt đầu cài đặt quảng cáo
        MobileAds.initialize(this) {
            Log.e("======> ", "initialize: $it")
        }

        // lấy id của điện thoại
        val testDeviceId =  Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        Log.e("======> ", "testDeviceId: $testDeviceId")

        //Enable test devices
        val configuration = RequestConfiguration.Builder().setTestDeviceIds(arrayListOf(testDeviceId)).build()
        MobileAds.setRequestConfiguration(configuration)
    }
}