package com.graduate.howtospeak

import android.content.ContentResolver
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_learn.mtMain1
import kotlinx.android.synthetic.main.activity_practice__result.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.lang.Exception

class Practice_Result : AppCompatActivity() {

    private lateinit var bt_vowel : String
    // opencv & STT
    private val searchUrl = "http://13.124.114.1/vowel_recognition/result"
    var result_opencv :String = ""
    var result_stt :String = ""
    private lateinit var imageview_opencv: ImageView
    private lateinit var imageview_stt: ImageView
    private lateinit var imageview_totall: ImageView

    // Imageview
    private lateinit var imageView_result: ImageView
    private lateinit var imageResult_string : String
    private lateinit var imageResult_uri : Uri


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setContentView(R.layout.activity_practice__result)

        // record path
        val recordPath = intent.getStringExtra("Record_path")
        Log.d("record_path_inR", recordPath.toString())

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

        mtRDetail1.setOnClickListener {
            val intent = Intent(this, Practice_RDetail::class.java)

            intent.putExtra("Record_path_send", recordPath.toString())
            intent.putExtra("Rvowel_bt", bt_vowel)
            intent.putExtra("RSTT_Result", result_stt)
            intent.putExtra("RImageUri", imageResult_string)

            startActivity(intent) }

        mtVowelPractice.setOnClickListener {
            val intent = Intent(this, Practice_Vowel::class.java)
            startActivity(intent) }


        // 결과 화면
        bring_opencvResult()
    }

    //====== openCV & STT 결과
    private fun bring_opencvResult() {
        val bring_html = CoroutineScope(Dispatchers.IO).async {
            val htmlResource: String
            try {
                val doc : Document = Jsoup.connect(searchUrl).get()
                val contents: String = doc.select("body h1").text()
                htmlResource = contents

                Log.d("OpenCv result1 : ", htmlResource)

                htmlResource
            } catch (e: Exception) {
                Log.i("get_result_opencv","error")
                e.printStackTrace()}
        }

        // 메인 화면 결과 반영
        CoroutineScope(Dispatchers.Main).launch {
            result_opencv = bring_html.await().toString()

            //result_opencv
            bt_vowel = intent.getStringExtra("Vowel_bt").toString()  // button
            result_stt = intent.getStringExtra("STT_Result").toString()  //stt


            // opecv 결과
            imageview_opencv = findViewById(R.id.mouthResult)
            when(result_opencv) {
                "a" -> imageview_opencv.setImageResource(R.drawable.mouth_a)
                "i" -> imageview_opencv.setImageResource(R.drawable.mouth_i)
                "o" -> imageview_opencv.setImageResource(R.drawable.mouth_o)
                "u" -> imageview_opencv.setImageResource(R.drawable.mouth_u)
                "e" -> imageview_opencv.setImageResource(R.drawable.mouth_e)
                else -> imageview_opencv.setImageResource(R.drawable.mouth_error)
            }

            // stt 결과
            imageview_stt = findViewById(R.id.voiceResult)
            when(result_stt) {
                "[아]" -> imageview_stt.setImageResource(R.drawable.voice_a)
                "[이]" -> imageview_stt.setImageResource(R.drawable.voice_i)

                "[오]" -> imageview_stt.setImageResource(R.drawable.voice_o)
                "[오오]" -> imageview_stt.setImageResource(R.drawable.voice_o)

                "[우]" -> imageview_stt.setImageResource(R.drawable.voice_u)
                "[우우]" -> imageview_stt.setImageResource(R.drawable.voice_u)
                "[우와]" -> imageview_stt.setImageResource(R.drawable.voice_u)

                "[에]" -> imageview_stt.setImageResource(R.drawable.voice_e)
                "[애]" -> imageview_stt.setImageResource(R.drawable.voice_e)
                "[예]" -> imageview_stt.setImageResource(R.drawable.voice_e)
                "[얘]" -> imageview_stt.setImageResource(R.drawable.voice_e)

                else -> imageview_stt.setImageResource(R.drawable.voice_error)
            }

            // totall result
            imageview_totall = findViewById(R.id.totallResult)
            when (bt_vowel) {
                "a" -> {
                    if (result_opencv == "a" && result_stt == "[아]") {
                        imageview_totall.setImageResource(R.drawable.result_goodjob)
                    } else {
                        imageview_totall.setImageResource(R.drawable.result_tryagain)
                    }
                }
                "i" -> {
                    if (result_opencv == "i" && result_stt == "[이]") {
                        imageview_totall.setImageResource(R.drawable.result_goodjob)
                    } else {
                        imageview_totall.setImageResource(R.drawable.result_tryagain)
                    }
                }
                "o" -> {
                    if (result_opencv == "o" && result_stt == "[오]") {
                        imageview_totall.setImageResource(R.drawable.result_goodjob) }
                    if(result_opencv == "o" && result_stt == "[오오]"){
                        imageview_totall.setImageResource(R.drawable.result_goodjob) }
                    else {
                        imageview_totall.setImageResource(R.drawable.result_tryagain)
                    }
                }
                "u" -> {
                    if (result_opencv == "u" && result_stt == "[우]") {
                        imageview_totall.setImageResource(R.drawable.result_goodjob)
                    }
                    if (result_opencv == "u" && result_stt == "[우우]") {
                        imageview_totall.setImageResource(R.drawable.result_goodjob)
                    }
                    if (result_opencv == "u" && result_stt == "[우와]") {
                        imageview_totall.setImageResource(R.drawable.result_goodjob)
                    }
                    else {
                        imageview_totall.setImageResource(R.drawable.result_tryagain)
                    }
                }

                "e" -> {
                    if (result_opencv == "e" && result_stt == "[에]") {
                        imageview_totall.setImageResource(R.drawable.result_goodjob)
                    } else {
                        imageview_totall.setImageResource(R.drawable.result_tryagain)
                    }
                }
                "e" -> {
                    if (result_opencv == "e" && result_stt == "[애]") {
                        imageview_totall.setImageResource(R.drawable.result_goodjob)
                    } else {
                        imageview_totall.setImageResource(R.drawable.result_tryagain)
                    }
                }
                "e" -> {
                    if (result_opencv == "e" && result_stt == "[예]") {
                        imageview_totall.setImageResource(R.drawable.result_goodjob)
                    } else {
                        imageview_totall.setImageResource(R.drawable.result_tryagain)
                    }
                }
                else -> {
                    imageview_totall.setImageResource(R.drawable.result_tryagain)
                }
            }


            // ImageView
            imageView_result = findViewById(R.id.imageResult_view)
            if (intent.hasExtra("ImageUri")) {
                imageResult_string = intent.getStringExtra("ImageUri").toString()
                imageResult_uri = Uri.parse(imageResult_string)
                Log.d("image_uri_send_test", imageResult_string)
                imageView_result.setImageURI(imageResult_uri)
            } else {
                Log.d("이미지 uri 받기", "error")
            }

        }

    }

}