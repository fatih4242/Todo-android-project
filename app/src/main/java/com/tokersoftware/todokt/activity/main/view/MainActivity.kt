package com.tokersoftware.todokt.activity.main.view

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.*
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener
import com.tokersoftware.todokt.R
import com.tokersoftware.todokt.databinding.ActivityMainBinding
import com.tokersoftware.todokt.fragment.home.HomeFragment
import com.tokersoftware.todokt.fragment.settings.SettingsFragment

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    lateinit var sharedPref : SharedPreferences



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init(){
        sharedPref = getSharedPreferences(getString(R.string.sharedpreferences_key), Context.MODE_PRIVATE)
        if(!sharedPref.getBoolean(getString(R.string.sharedpreferences_close_ads), false)){
            println("show ads")
            //Init ads
            MobileAds.initialize(this){}

            //init banners
            val adRequest = AdRequest.Builder().build()

            //Show Banner
            binding.adView.loadAd(adRequest)
        }


        //Check If is First Start
        if (sharedPref.getBoolean(getString(R.string.sharedpreferences_firstStart), true)) {
            val editor =  sharedPref.edit()
            editor.putBoolean(getString(R.string.sharedpreferences_notification), true)
            editor.putBoolean(getString(R.string.sharedpreferences_firstStart), false)
            editor.apply()
        }

        //Init the bottom Nav
        binding.bottomNav.setOnItemSelectedListener {
            if(it.itemId == R.id.home){
                loadFragment(HomeFragment())
                true
            } else {
                loadFragment(SettingsFragment())
                true
            }

        }

        //Load First the HomeFragment
        loadFragment(HomeFragment())
    }

    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer,fragment)
        transaction.commit()
    }
}