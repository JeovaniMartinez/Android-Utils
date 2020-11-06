package com.jeovanimartinez.androidutils.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jeovanimartinez.androidutils.Test

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val a = Test.test()

        if (true){

        }
    }
}