package com.graduate.howtospeak

import android.content.Intent
import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_learn.*
import kotlinx.android.synthetic.main.activity_learn__consonant.*

class Learn_Consonant : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setContentView(R.layout.activity_learn__consonant)

        // 상단 기본
        mtPractice2.setOnClickListener {
            val intent = Intent(this, PracticeActivity::class.java)
            startActivity(intent) }

        mtMain2.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent) }

        // 각 학습 버튼
        lcon1.setOnClickListener {
            val intent = Intent(this, LearnActivity::class.java)
            startActivity(intent) }


    }
}