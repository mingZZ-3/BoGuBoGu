package com.graduate.howtospeak

import android.Manifest
import android.R
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_learn.mtMain1
import kotlinx.android.synthetic.main.activity_practice.*
import java.io.File
import java.io.IOException
import java.util.*


@Suppress("DEPRECATION")
class PracticeActivity : AppCompatActivity() {

    var mRecorder: MediaRecorder? = null
    var recordoutput: String? = null
    var isRecording: Boolean = false
    var recordingStopped: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setContentView(com.graduate.howtospeak.R.layout.activity_practice)

        mtMain1.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent) }

        mtResult1.setOnClickListener {
            val intent = Intent(this, Practice_Result::class.java)
            startActivity(intent) }

        // 음성 녹음 버튼
        bt_rstart.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //Permission is not granted
                val permissions = arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                ActivityCompat.requestPermissions(this, permissions,0)
            } else {
                startRecording()
            }
        }

        bt_rstop.setOnClickListener {
            stopRecording()
        }
    }


    // 음성 녹음
    private fun stopRecording() {
        if(isRecording){
            mRecorder?.stop()
            mRecorder?.reset()
            mRecorder?.release()
            isRecording = false
            Toast.makeText(this, "중지 되었습니다.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "레코딩 상태가 아닙니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startRecording() {
        val fileName: String = Date().time.toString() + ".mp3"
        recordoutput = Environment.getExternalStorageDirectory().absolutePath + "/Download/" + fileName //내장메모리 밑에 위치
        mRecorder = MediaRecorder()
        mRecorder?.setAudioSource((MediaRecorder.AudioSource.MIC))
        mRecorder?.setOutputFormat((MediaRecorder.OutputFormat.MPEG_4))
        mRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        mRecorder?.setOutputFile(recordoutput)

        try {
            mRecorder?.prepare()
            mRecorder?.start()
            isRecording = true
            Toast.makeText(this, "레코딩 시작되었습니다.", Toast.LENGTH_SHORT).show()
        } catch (e: IllegalStateException){
            e.printStackTrace()
        } catch (e: IOException){
            e.printStackTrace()
        }
    }

}