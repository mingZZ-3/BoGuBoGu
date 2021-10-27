package com.graduate.howtospeak

import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.*
import kotlinx.android.synthetic.main.activity_learn.*


class LearnActivity : AppCompatActivity() {
    //====== 변수 ======//
    //  학습뷰 변수
    private lateinit var imageView_vowel: ImageView
    private lateinit var videoView_vowel: VideoView
    // 전달받은 버튼 tag 값 변수
    private lateinit var vowel_getby: String


    // ================================== onCreate ==================================== //
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setContentView(R.layout.activity_learn)

        // 상태바 없애기
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN ,
                WindowManager.LayoutParams.FLAG_FULLSCREEN ) }


        // 학습 내용 사진 적용
        imageView_vowel = findViewById(R.id.whichVoweltoLearn) as ImageView
        vowel_getby = intent.getStringExtra("vowel_tolearn").toString()

        when (vowel_getby) {
            "a" -> imageView_vowel.setImageResource(R.drawable.learn_a)
            "eo" -> imageView_vowel.setImageResource(R.drawable.learn_eo)
            "i" -> imageView_vowel.setImageResource(R.drawable.learn_i)
            "o" -> imageView_vowel.setImageResource(R.drawable.learn_o)
            "u" -> imageView_vowel.setImageResource(R.drawable.learn_u)
            "e" -> imageView_vowel.setImageResource(R.drawable.learn_e)
            "eu" -> imageView_vowel.setImageResource(R.drawable.learn_eu)
            else -> imageView_vowel.setImageResource(R.drawable.learn_error)
        }


        // 학습 영상 적용
        videoView_vowel = findViewById(R.id.learn_videoView) as VideoView
        videoView_vowel.setMediaController(MediaController(this))
        videoView_vowel.requestFocus()

        val video_a = Uri.parse(
            "android.resource://" + packageName + "/"+R.raw.video_a)
        val video_eo = Uri.parse(
            "android.resource://" + packageName + "/"+R.raw.video_eo)
        val video_i = Uri.parse(
            "android.resource://" + packageName + "/"+R.raw.video_i)
        val video_o = Uri.parse(
            "android.resource://" + packageName + "/"+R.raw.video_o)
        val video_u = Uri.parse(
            "android.resource://" + packageName + "/"+R.raw.video_u)
        val video_e = Uri.parse(
            "android.resource://" + packageName + "/"+R.raw.video_e)
        val video_eu = Uri.parse(
            "android.resource://" + packageName + "/"+R.raw.video_eu)
        val video_uri_test = Uri.parse(
            "android.resource://" + packageName + "/"+R.raw.video_test_source)

        when(vowel_getby) {
            "a" -> {
                videoView_vowel.setVideoURI(video_a)
                videoView_vowel.setMediaController(MediaController(this))
                videoView_vowel.requestFocus()
            }
            "eo" -> {
                videoView_vowel.setVideoURI(video_eo)
                videoView_vowel.setMediaController(MediaController(this))
                videoView_vowel.requestFocus()
            }
            "i" -> {
                videoView_vowel.setVideoURI(video_i)
                videoView_vowel.setMediaController(MediaController(this))
                videoView_vowel.requestFocus()
            }
            "o" -> {
                videoView_vowel.setVideoURI(video_o)
                videoView_vowel.setMediaController(MediaController(this))
                videoView_vowel.requestFocus()
            }
            "u" -> {
                videoView_vowel.setVideoURI(video_u)
                videoView_vowel.setMediaController(MediaController(this))
                videoView_vowel.requestFocus()
            }
            "e" -> {
                videoView_vowel.setVideoURI(video_e)
                videoView_vowel.setMediaController(MediaController(this))
                videoView_vowel.requestFocus()
            }
            "eu" -> {
                videoView_vowel.setVideoURI(video_eu)
                videoView_vowel.setMediaController(MediaController(this))
                videoView_vowel.requestFocus()
            }
            else -> {
                videoView_vowel.setVideoURI(video_uri_test)
                videoView_vowel.setMediaController(MediaController(this))
                videoView_vowel.requestFocus()
            }
        }


        // 기본 버튼
        mtMain1.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent) }

        mtLearn_vowel.setOnClickListener {
            val intent = Intent(this, Learn_Vowel::class.java)
            startActivity(intent) }

        // 영상 실행 관련 버튼
        learnVideo_play.setOnClickListener {
            videoView_vowel.start()
            }

        learnVideo_pause.setOnClickListener {
            videoView_vowel.pause() }
    }
}