package com.example.compositiongame.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.compositiongame.R

class MainActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        launchWelcomeFragment()
    }

    private fun launchWelcomeFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.main_container, WelcomeFragment())
            .commit()
    }

}