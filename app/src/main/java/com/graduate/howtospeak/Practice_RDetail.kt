package com.graduate.howtospeak

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.annotation.RawRes
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_learn.*
import kotlinx.android.synthetic.main.activity_learn.mtMain1
import kotlinx.android.synthetic.main.activity_practice__r_detail.*


class Practice_RDetail : AppCompatActivity() {
    // 접근 권한
    private lateinit var audioPlayer: Visualizerutil


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setContentView(R.layout.activity_practice__r_detail)


        // 홈 버튼
        mtMain1.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent) }

        // 다시 학습하기 버튼



        // 음성 시각화
        audioPlayer = Visualizerutil()
    }

    // 실행
    override fun onStart() {
        super.onStart()
        startPlayingAudio(R.raw.test2)
    }

    // 정지
    override fun onStop() {
        super.onStop()
        stopPlayingAudio();
    }

    //
    private fun startPlayingAudio(@RawRes resId: Int) {
        audioPlayer.play(this, resId) {
        }
        audioPlayer.getAudioSessionId()
            ?.also {
                barVisualizerPanel.setAudioSessionId(it)
            }
    }

    private fun stopPlayingAudio() {
        audioPlayer.stop();
        barVisualizerPanel.release()
    }


}

