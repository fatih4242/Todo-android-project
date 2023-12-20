package com.tokersoftware.todokt.activity.splash

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.android.gms.ads.*
import com.google.android.gms.ads.appopen.AppOpenAd
import com.tokersoftware.todokt.R
import com.tokersoftware.todokt.activity.main.view.MainActivity

class SplashActivity : AppCompatActivity() {

    lateinit var sharedPref : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar!!.hide()

        val handler = Handler()
        handler.postDelayed({
            init()
        }, 1000)

    }

    private fun init() {

        sharedPref = getSharedPreferences(getString(R.string.sharedpreferences_key), Context.MODE_PRIVATE)
        if (!sharedPref.getBoolean(getString(R.string.sharedpreferences_close_ads), false)) {
            println("show ads")
            //Init ads
            MobileAds.initialize(this) {}

            //init adRequest
            val adRequest = AdRequest.Builder().build()

            //Show OpenApp
            AppOpenAd.load(
                this,
                getString(R.string.admob_openapp_id),
                adRequest,
                appOpenAdLoadCallback
            )

        } else {
            goToMain()
        }




    }

    private fun goToMain(){
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private var appOpenAd: AppOpenAd? = null
    private val appOpenAdLoadCallback = object : AppOpenAd.AppOpenAdLoadCallback() {
        override fun onAdLoaded(add: AppOpenAd) {
            super.onAdLoaded(add)
            appOpenAd = add
            appOpenAd!!.show(this@SplashActivity)

            val fullScreenContentCallback: FullScreenContentCallback =
                object : FullScreenContentCallback() {
                    override fun onAdClicked() {
                        super.onAdClicked()
                    }

                    override fun onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent()
                        goToMain()
                    }

                    override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                        super.onAdFailedToShowFullScreenContent(p0)
                        goToMain()
                    }

                    override fun onAdImpression() {
                        super.onAdImpression()
                        //WHile showing
                    }

                    override fun onAdShowedFullScreenContent() {
                        super.onAdShowedFullScreenContent()
                        //goToMain()
                    }
                }

            appOpenAd!!.fullScreenContentCallback = fullScreenContentCallback

        }

        override fun onAdFailedToLoad(err: LoadAdError) {
            super.onAdFailedToLoad(err)
            println(err.message)
            goToMain()
        }
    }
}