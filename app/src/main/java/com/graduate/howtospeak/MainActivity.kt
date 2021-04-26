package com.graduate.howtospeak

import android.content.Intent
import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setContentView(R.layout.activity_main)

        learn_con1.setOnClickListener {
            val intent = Intent(this, Learn_Consonant::class.java)
            startActivity(intent) }

        learn_vowel1.setOnClickListener {
            val intent = Intent(this, Learn_Vowel::class.java)
            startActivity(intent) }

        practice_con1.setOnClickListener {
            val intent = Intent(this, Practice_Consonant::class.java)
            startActivity(intent) }

        practice_vowel1.setOnClickListener {
            val intent = Intent(this, Practice_Vowel::class.java)
            startActivity(intent) }
    }
}