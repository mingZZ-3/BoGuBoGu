package com.graduate.howtospeak

import android.content.Intent
import android.content.pm.ActivityInfo
import android.media.MediaPlayer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_learn.*
//import kotlinx.android.synthetic.main.activity_main.*
//import kotlinx.android.synthetic.main.activity_main.mtPractice1

class LearnActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setContentView(R.layout.activity_learn)

        mtPractice1.setOnClickListener {
            val intent = Intent(this, PracticeActivity::class.java)
            startActivity(intent) }

        mtMain1.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent) }

        // 음성
        learnsound1.setOnClickListener {
            val mediaPlayer = MediaPlayer.create(this, R.raw.geu).start()
            startActivity(intent) }
    }
}