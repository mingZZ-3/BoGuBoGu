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
    // 표준 음성
    var vowel_button: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setContentView(R.layout.activity_practice__r_detail)

        // 뒤로 돌아갈때 값 남기기
        val vowelR = intent.getStringExtra("Rvowel_bt")
        Log.d("vowel-result_inRD", vowelR.toString())
        val sttR = intent.getStringExtra("RSTT_Result")
        Log.d("STT_result_inRD", sttR.toString())
        val imageR = intent.getStringExtra("RImageUri")
        Log.d("image_inRD", imageR.toString())
        val vowel_bt = intent.getStringExtra("Rvowel_bt")
        Log.d("button_inRD", vowel_bt.toString())
        vowel_button = vowel_bt

        // record path 설정
        val recordPath_receive = intent.getStringExtra("Record_path_send")
        if (recordPath_receive != null) {
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

            intent.putExtra("Vowel_bt", vowelR)
            intent.putExtra("STT_Result", sttR)
            intent.putExtra("ImageUri", imageR)
            intent.putExtra("Record_path", recordPath_receive)

            startActivity(intent) }

        play_spectrum.setOnClickListener {
            if(recordPath_uri != null) {
                startPlayingAudio(recordPath_uri)

            } else {
                Log.e("record_Path", "user voice error")
            }
            when (vowel_button) {
                //a
                "a" -> startPlayingAudioId(R.raw.voice_a)
                "ga" -> startPlayingAudioId(R.raw.voice_ga)
                "na" -> startPlayingAudioId(R.raw.voice_na)
                "da" -> startPlayingAudioId(R.raw.voice_da)
                //eo
                "eo" -> startPlayingAudioId(R.raw.voice_eo)
                "geo" -> startPlayingAudioId(R.raw.voice_geo)
                "neo" -> startPlayingAudioId(R.raw.voice_neo)
                "deo" -> startPlayingAudioId(R.raw.voice_deo)
                //i
                "i" -> startPlayingAudioId(R.raw.voice_i)
                "gi" -> startPlayingAudioId(R.raw.voice_gi)
                "ni" -> startPlayingAudioId(R.raw.voice_ni)
                "di" -> startPlayingAudioId(R.raw.voice_di)
                //o
                "o" -> startPlayingAudioId(R.raw.voice_o)
                "go" -> startPlayingAudioId(R.raw.voice_go)
                "no" -> startPlayingAudioId(R.raw.voice_no)
                "do" -> startPlayingAudioId(R.raw.voice_do)
                //u
                "u" -> startPlayingAudioId(R.raw.voice_u)
                "gu" -> startPlayingAudioId(R.raw.voice_gu)
                "nu" -> startPlayingAudioId(R.raw.voice_nu)
                "du" -> startPlayingAudioId(R.raw.voice_du)
                //e
                "e" -> startPlayingAudioId(R.raw.voice_e)
                "ge" -> startPlayingAudioId(R.raw.voice_ge)
                "ne" -> startPlayingAudioId(R.raw.voice_ne)
                "de" -> startPlayingAudioId(R.raw.voice_de)
                //eu
                "eu" -> startPlayingAudioId(R.raw.voice_eu)
                "geu" -> startPlayingAudioId(R.raw.voice_geu)
                "neu" -> startPlayingAudioId(R.raw.voice_neu)
                "deu" -> startPlayingAudioId(R.raw.voice_deu)

                else -> startPlayingAudioId(R.raw.voice_e)
            }
        }


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

        when (vowel_button) {
            //a
            "a" -> startPlayingAudioId(R.raw.voice_a)
            "ga" -> startPlayingAudioId(R.raw.voice_ga)
            "na" -> startPlayingAudioId(R.raw.voice_na)
            "da" -> startPlayingAudioId(R.raw.voice_da)
            //eo
            "eo" -> startPlayingAudioId(R.raw.voice_eo)
            "geo" -> startPlayingAudioId(R.raw.voice_geo)
            "neo" -> startPlayingAudioId(R.raw.voice_neo)
            "deo" -> startPlayingAudioId(R.raw.voice_deo)
            //i
            "i" -> startPlayingAudioId(R.raw.voice_i)
            "gi" -> startPlayingAudioId(R.raw.voice_gi)
            "ni" -> startPlayingAudioId(R.raw.voice_ni)
            "di" -> startPlayingAudioId(R.raw.voice_di)
            //o
            "o" -> startPlayingAudioId(R.raw.voice_o)
            "go" -> startPlayingAudioId(R.raw.voice_go)
            "no" -> startPlayingAudioId(R.raw.voice_no)
            "do" -> startPlayingAudioId(R.raw.voice_do)
            //u
            "u" -> startPlayingAudioId(R.raw.voice_u)
            "gu" -> startPlayingAudioId(R.raw.voice_gu)
            "nu" -> startPlayingAudioId(R.raw.voice_nu)
            "du" -> startPlayingAudioId(R.raw.voice_du)
            //e
            "e" -> startPlayingAudioId(R.raw.voice_e)
            "ge" -> startPlayingAudioId(R.raw.voice_ge)
            "ne" -> startPlayingAudioId(R.raw.voice_ne)
            "de" -> startPlayingAudioId(R.raw.voice_de)
            //eu
            "eu" -> startPlayingAudioId(R.raw.voice_eu)
            "geu" -> startPlayingAudioId(R.raw.voice_geu)
            "neu" -> startPlayingAudioId(R.raw.voice_neu)
            "deu" -> startPlayingAudioId(R.raw.voice_deu)
            else -> startPlayingAudioId(R.raw.voice_e)
        }

    }

    // 정지
    override fun onStop() {
        super.onStop()
        stopPlayingAudio()
    }

    // 실행 파일 설정
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
