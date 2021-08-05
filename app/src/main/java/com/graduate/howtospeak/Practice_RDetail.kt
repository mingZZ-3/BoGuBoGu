package com.graduate.howtospeak

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.annotation.RawRes
import androidx.appcompat.app.AppCompatActivity
//import kotlinx.android.synthetic.main.activity_learn.mtMain1
import kotlinx.android.synthetic.main.activity_practice__r_detail.*


class Practice_RDetail : AppCompatActivity() {
    // 접근 권한
    private lateinit var audioPlayer1: Visualizerutil
    private lateinit var audioPlayer2: Visualizerutil


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setContentView(R.layout.activity_practice__r_detail)


        // 홈 버튼
        mtMain1.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent) }

        // 뒤로가기 버튼
        mtPractice_result.setOnClickListener {
            val intent = Intent(this, Practice_Result::class.java)
            startActivity(intent) }


        // 음성 시각화
        audioPlayer1 = Visualizerutil()
        audioPlayer2 = Visualizerutil()
    }

    // 실행
    override fun onStart() {
        super.onStart()
        startPlayingAudio(R.raw.geu)
    }

    // 정지
    override fun onStop() {
        super.onStop()
        stopPlayingAudio()
    }

    //
    private fun startPlayingAudio(@RawRes resId: Int) {
        audioPlayer1.play(this, resId) {
        }
        audioPlayer1.getAudioSessionId()
            ?.also {
                barVisualizerPanel1.setAudioSessionId(it)
            }

        audioPlayer2.play(this, resId) {
        }
        audioPlayer2.getAudioSessionId()
            ?.also {
                barVisualizerPanel2.setAudioSessionId(it)
            }
    }

    private fun stopPlayingAudio() {
        audioPlayer1.stop()
        barVisualizerPanel1.release()

        audioPlayer2.stop()
        barVisualizerPanel1.release()
    }
}


// https://github.com/AlShevelev/music-visualization/blob/master/app/src/main/java/com/shevelev/music_visualization/demo/DemoActivity.kt
