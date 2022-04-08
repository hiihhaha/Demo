package com.mglb.demoadmob
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.android.gms.ads.*
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import kotlinx.android.synthetic.main.activity_native_ad.*
const val ADMOB_AD_UNIT_ID = "ca-app-pub-3940256099942544/2247696110"
var currentNativeAd: NativeAd? = null

class NativeAdActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_native_ad)
        refresh_button.setOnClickListener {
            refreshAd()
        }

    }
    private fun populateNativeAdView(nativeAd: NativeAd, adView: NativeAdView) {
        // set mediaView
        adView.mediaView = adView.findViewById<MediaView>(R.id.ad_media)
        // ánh xạ
        adView.headlineView = adView.findViewById(R.id.ad_headline)
        adView.bodyView = adView.findViewById(R.id.ad_body)
        adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
        adView.iconView = adView.findViewById(R.id.ad_app_icon)
        adView.priceView = adView.findViewById(R.id.ad_price)
        adView.storeView = adView.findViewById(R.id.ad_store)
        adView.starRatingView = adView.findViewById(R.id.ad_stars)
        adView.advertiserView = adView.findViewById(R.id.ad_advertiser)

        //set data
        (adView.headlineView as TextView).text = nativeAd.headline
        adView.mediaView.setMediaContent(nativeAd.mediaContent)
        //kiểm tra trước khi cố gắng hiển thị

        if (nativeAd.body == null) {
            adView.bodyView.visibility = View.INVISIBLE
        } else {
            adView.bodyView.visibility = View.VISIBLE
            (adView.bodyView as TextView).text = nativeAd.body
        }
        if (nativeAd.callToAction == null) {
            adView.callToActionView.visibility = View.INVISIBLE
        } else {
            adView.callToActionView.visibility = View.VISIBLE
            (adView.callToActionView as Button).text = nativeAd.callToAction

        }
        if (nativeAd.icon == null) {
            adView.iconView.visibility = View.INVISIBLE
        } else {
            (adView.iconView as ImageView).setImageDrawable(
                nativeAd.icon.drawable
            )
            adView.iconView.visibility = View.VISIBLE

        }
        if (nativeAd.price == null) {
            adView.priceView.visibility = View.INVISIBLE
        } else {
            adView.priceView.visibility = View.VISIBLE
            (adView.priceView as TextView).text = nativeAd.price
        }
        if (nativeAd.starRating == null) {
            adView.starRatingView.visibility = View.VISIBLE
        } else {
            (adView.starRatingView as RatingBar).rating = nativeAd.starRating!!.toFloat()
            adView.starRatingView.visibility = View.VISIBLE
        }
        if (nativeAd.store == null) {
            adView.storeView.visibility = View.INVISIBLE
        } else {
            adView.storeView.visibility = View.VISIBLE
            (adView.storeView as TextView).text = nativeAd.store
        }
        if (nativeAd.advertiser == null) {
            adView.advertiserView.visibility = View.INVISIBLE
        } else {
            (adView.advertiserView as TextView).text = nativeAd.advertiser
            adView.advertiserView.visibility = View.VISIBLE
        }
//  cho Google biết rằng bạn đã hoàn thành việc điền vào chế độ xem quảng cáo gốc của mình với quảng cáo gốc này.
        adView.setNativeAd(nativeAd)
        val vc = nativeAd.mediaContent.videoController

        // Updates the UI to say whether or not this ad has a video asset.
        if (vc.hasVideoContent()) {
            vc.videoLifecycleCallbacks = object : VideoController.VideoLifecycleCallbacks() {
                override fun onVideoEnd() {
                    refresh_button.isEnabled = true
                    videostatus_text.text = "Video status: Video playback has ended."
                    super.onVideoEnd()
                }
            }
        } else {
            videostatus_text.text = "Video status: Ad does not contain a video asset."
            refresh_button.isEnabled = true
        }
    }
    private fun refreshAd() {
        refresh_button.isEnabled = false
        val builder = AdLoader.Builder(this, ADMOB_AD_UNIT_ID)

        builder.forNativeAd { nativeAd ->

            //Triển khai OnUnifiedNativeAdLoadedListener. Nếu lệnh gọi lại này xảy ra sau khi hoạt động bị hủy,
            // bạn phải gọi hủy và quay lại, nếu không bạn có thể bị rò rỉ bộ nhớ.
            var activityDestroyed = false
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                activityDestroyed = isDestroyed
            }
            if (activityDestroyed || isFinishing || isChangingConfigurations) {
                nativeAd.destroy()
                return@forNativeAd
            }
            //Bạn phải gọi tiêu diệt trên các quảng cáo cũ khi bạn hoàn thành chúng,
            // nếu không bạn sẽ bị rò rỉ bộ nhớ.
            currentNativeAd?.destroy()
            currentNativeAd = nativeAd
            val adView = layoutInflater
                .inflate(R.layout.item_native, null) as NativeAdView
            populateNativeAdView(nativeAd, adView)
            ad_frame.removeAllViews()
            ad_frame.addView(adView)
        }
        val videoOptions = VideoOptions.Builder()
            .setStartMuted(start_muted_checkbox.isChecked)
            .build()
        val adOptions = NativeAdOptions.Builder()
            .setVideoOptions(videoOptions)
            .build()


        builder.withNativeAdOptions(adOptions)

        val adLoader = builder.withAdListener(object : AdListener(){
            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                val error =
                    """
           domain: ${loadAdError.domain}, code: ${loadAdError.code}, message: ${loadAdError.message}
          """"
                refresh_button.isEnabled = true
                Toast.makeText(
                   this@NativeAdActivity, "Failed to load native ad with error $error",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }).build()
        adLoader.loadAd(AdRequest.Builder().build())
        videostatus_text.text = ""
    }

    override fun onDestroy() {
        currentNativeAd?.destroy()
        super.onDestroy()
    }
}