package com.jeovanimartinez.androidutils.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.jeovanimartinez.androidutils.Test
import com.jeovanimartinez.androidutils.reviews.RateApp


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val a = Test.test(null)


        RateApp.rateInGooglePlay(this@MainActivity)

        Test.mensaje(this@MainActivity)
    }
}