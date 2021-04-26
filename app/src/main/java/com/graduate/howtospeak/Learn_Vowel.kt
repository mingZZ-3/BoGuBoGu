package com.graduate.howtospeak

import android.content.Intent
import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_learn__consonant.*
import kotlinx.android.synthetic.main.activity_learn__vowel.*

class Learn_Vowel : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setContentView(R.layout.activity_learn__vowel)

        // 상단 기본
        mtPractice3.setOnClickListener {
            val intent = Intent(this, PracticeActivity::class.java)
            startActivity(intent) }

        mtMain3.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent) }

        //
    }
}