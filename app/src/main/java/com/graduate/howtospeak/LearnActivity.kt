package com.graduate.howtospeak

import android.content.Intent
import android.content.pm.ActivityInfo
import android.media.MediaPlayer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_learn.*
//import kotlinx.android.synthetic.main.activity_main.*
//import kotlinx.android.synthetic.main.activity_main.mtPractice1

class LearnActivity : AppCompatActivity() {
    private lateinit var textView_vowel: TextView

    private lateinit var vowel_getby: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setContentView(R.layout.activity_learn)

        textView_vowel = findViewById(R.id.whichVoweltoLearn) as TextView

        vowel_getby = intent.getStringExtra("vowel_tolearn").toString()

        when (vowel_getby) {
            "a" -> textView_vowel.setText("ㅏ")
            "eo" -> textView_vowel.setText("ㅓ")
            "i" -> textView_vowel.setText("ㅣ")
            "o" -> textView_vowel.setText("ㅗ")
            "u" -> textView_vowel.setText("ㅜ")
            "e" -> textView_vowel.setText("ㅐ/ㅔ")
            else -> textView_vowel.setText("error")
        }

        // 기본
        mtMain1.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent) }

        mtLearn_vowel.setOnClickListener {
            val intent = Intent(this, Learn_Vowel::class.java)
            startActivity(intent) }

        // 음성 & 영상 재생
        learnsound_play.setOnClickListener {
            val mediaPlayer = MediaPlayer.create(this, R.raw.geu).start()
            startActivity(intent) }

        learnVideo_play.setOnClickListener {
            val mediaPlayer = MediaPlayer.create(this, R.raw.geu).start()
            startActivity(intent) }




    }


}