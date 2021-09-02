package com.graduate.howtospeak

import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import androidx.annotation.RawRes
import androidx.appcompat.app.AppCompatActivity
//import kotlinx.android.synthetic.main.activity_learn.mtMain1
import kotlinx.android.synthetic.main.activity_practice__r_detail.*


class Practice_RDetail : AppCompatActivity() {
    // 접근 권한
    private lateinit var audioPlayer1: Visualizerutil
    private lateinit var audioPlayer2: Visualizerutil
    // record path
    lateinit var recordPath_uri: Uri
    lateinit var addpath: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setContentView(R.layout.activity_practice__r_detail)

        // record path 설정
        val recordPath_receive = intent.getStringExtra("Record_path_send")
        if (recordPath_receive != null) {
            //addpath = "content://media/external/" + recordPath_receive
            recordPath_uri = Uri.parse(recordPath_receive)
            Log.d("recordPath_inRD", recordPath_uri.toString())
        } else {
            Log.e("recordPath_inRd", "error")
        }

        // 상태바 없애기
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN ,
                WindowManager.LayoutParams.FLAG_FULLSCREEN ) }


        // 버튼
        mtMain1.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent) }
        mtPractice_result.setOnClickListener {
            val intent = Intent(this, Practice_Result::class.java)



            startActivity(intent) }


        // 음성 시각화
        if(recordPath_uri != null) {
            audioPlayer1 = Visualizerutil()

        } else {
            Log.e("record_Path", "user voice error")
        }
        audioPlayer2 = Visualizerutil()
    }

    // 실행
    override fun onStart() {
        super.onStart()
        startPlayingAudio(recordPath_uri)
        startPlayingAudioId(R.raw.geu)
    }

    // 정지
    override fun onStop() {
        super.onStop()
        stopPlayingAudio()
    }

    //
    private fun startPlayingAudio(resUri_user: Uri) {
        audioPlayer1.play(this, resUri_user) {
        }
        audioPlayer1.getAudioSessionId()
            ?.also {
                barVisualizerPanel1.setAudioSessionId(it)
            }
    }

    private fun startPlayingAudioId(@RawRes resId: Int) {

        audioPlayer2.playId(this, resId) {
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
