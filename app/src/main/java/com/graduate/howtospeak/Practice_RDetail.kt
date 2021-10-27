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
    //====== 변수 ======//
    // 접근 권한
    private lateinit var audioPlayer1: Visualizerutil
    private lateinit var audioPlayer2: Visualizerutil
    // record path
    lateinit var recordPath_uri: Uri
    lateinit var addpath: String
    // 표준 음성
    var vowel_button: String? = null

// ================================== onCreate ==================================== //
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setContentView(R.layout.activity_practice__r_detail)

        // 상태바 없애기
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN ,
            WindowManager.LayoutParams.FLAG_FULLSCREEN ) }


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

        // 스펙트럼 실행 버튼
        play_spectrum.setOnClickListener {
            //user
            if(recordPath_uri != null) {
                startPlayingAudio(recordPath_uri)

            } else {
                Log.e("record_Path", "user voice error")
            }

            // default file
            when (vowel_button) {
                // 전달받은 버튼 값에 따른 설정
                //a
                "a" -> startPlayingAudioD(R.raw.voice_a)
                "ga" -> startPlayingAudioD(R.raw.voice_ga)
                "na" -> startPlayingAudioD(R.raw.voice_na)
                "da" -> startPlayingAudioD(R.raw.voice_da)
                //eo
                "eo" -> startPlayingAudioD(R.raw.voice_eo)
                "geo" -> startPlayingAudioD(R.raw.voice_geo)
                "neo" -> startPlayingAudioD(R.raw.voice_neo)
                "deo" -> startPlayingAudioD(R.raw.voice_deo)
                //i
                "i" -> startPlayingAudioD(R.raw.voice_i)
                "gi" -> startPlayingAudioD(R.raw.voice_gi)
                "ni" -> startPlayingAudioD(R.raw.voice_ni)
                "di" -> startPlayingAudioD(R.raw.voice_di)
                //o
                "o" -> startPlayingAudioD(R.raw.voice_o)
                "go" -> startPlayingAudioD(R.raw.voice_go)
                "no" -> startPlayingAudioD(R.raw.voice_no)
                "do" -> startPlayingAudioD(R.raw.voice_do)
                //u
                "u" -> startPlayingAudioD(R.raw.voice_u)
                "gu" -> startPlayingAudioD(R.raw.voice_gu)
                "nu" -> startPlayingAudioD(R.raw.voice_nu)
                "du" -> startPlayingAudioD(R.raw.voice_du)
                //e
                "e" -> startPlayingAudioD(R.raw.voice_e)
                "ge" -> startPlayingAudioD(R.raw.voice_ge)
                "ne" -> startPlayingAudioD(R.raw.voice_ne)
                "de" -> startPlayingAudioD(R.raw.voice_de)
                //eu
                "eu" -> startPlayingAudioD(R.raw.voice_eu)
                "geu" -> startPlayingAudioD(R.raw.voice_geu)
                "neu" -> startPlayingAudioD(R.raw.voice_neu)
                "deu" -> startPlayingAudioD(R.raw.voice_deu)

                else -> startPlayingAudioD(R.raw.voice_e)
            }
        }


        // 음성 스펙트럼 시각화
        if(recordPath_uri != null) {
            //
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
            "a" -> startPlayingAudioD(R.raw.voice_a)
            "ga" -> startPlayingAudioD(R.raw.voice_ga)
            "na" -> startPlayingAudioD(R.raw.voice_na)
            "da" -> startPlayingAudioD(R.raw.voice_da)
            //eo
            "eo" -> startPlayingAudioD(R.raw.voice_eo)
            "geo" -> startPlayingAudioD(R.raw.voice_geo)
            "neo" -> startPlayingAudioD(R.raw.voice_neo)
            "deo" -> startPlayingAudioD(R.raw.voice_deo)
            //i
            "i" -> startPlayingAudioD(R.raw.voice_i)
            "gi" -> startPlayingAudioD(R.raw.voice_gi)
            "ni" -> startPlayingAudioD(R.raw.voice_ni)
            "di" -> startPlayingAudioD(R.raw.voice_di)
            //o
            "o" -> startPlayingAudioD(R.raw.voice_o)
            "go" -> startPlayingAudioD(R.raw.voice_go)
            "no" -> startPlayingAudioD(R.raw.voice_no)
            "do" -> startPlayingAudioD(R.raw.voice_do)
            //u
            "u" -> startPlayingAudioD(R.raw.voice_u)
            "gu" -> startPlayingAudioD(R.raw.voice_gu)
            "nu" -> startPlayingAudioD(R.raw.voice_nu)
            "du" -> startPlayingAudioD(R.raw.voice_du)
            //e
            "e" -> startPlayingAudioD(R.raw.voice_e)
            "ge" -> startPlayingAudioD(R.raw.voice_ge)
            "ne" -> startPlayingAudioD(R.raw.voice_ne)
            "de" -> startPlayingAudioD(R.raw.voice_de)
            //eu
            "eu" -> startPlayingAudioD(R.raw.voice_eu)
            "geu" -> startPlayingAudioD(R.raw.voice_geu)
            "neu" -> startPlayingAudioD(R.raw.voice_neu)
            "deu" -> startPlayingAudioD(R.raw.voice_deu)
            else -> startPlayingAudioD(R.raw.voice_e)
        }

    }

    // 정지
    override fun onStop() {
        super.onStop()
        stopPlayingAudio()
    }

    // User 실행 파일 설정
    private fun startPlayingAudio(resUri_user: Uri) {
        audioPlayer1.play(this, resUri_user) {
        }
        audioPlayer1.getAudioSessionId()
            ?.also {
                barVisualizerPanel1.setAudioSessionId(it)
            }
    }

    // 표준 발음 실행
    private fun startPlayingAudioD(@RawRes resId: Int) {

        audioPlayer2.playD(this, resId) {
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