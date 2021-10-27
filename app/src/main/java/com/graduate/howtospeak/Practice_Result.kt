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

    //====== 변수 ======//
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


// ================================== onCreate ==================================== //
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setContentView(R.layout.activity_practice__result)

        // 상태바 없애기
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN ,
                WindowManager.LayoutParams.FLAG_FULLSCREEN ) }


        // record path
        val recordPath = intent.getStringExtra("Record_path")
        Log.d("record_path_inR", recordPath.toString())

        // 결과 화면
        bring_opencvResult()

        // 버튼
        mtMain1.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent) }

        mtRDetail1.setOnClickListener {
            // 결과값 액티비티간 전달
            val intent = Intent(this, Practice_RDetail::class.java)

            intent.putExtra("Record_path_send", recordPath.toString())
            intent.putExtra("Rvowel_bt", bt_vowel)
            intent.putExtra("RSTT_Result", result_stt)
            intent.putExtra("RImageUri", imageResult_string)

            startActivity(intent) }

        mtVowelPractice.setOnClickListener {
            val intent = Intent(this, Practice_Vowel::class.java)
            startActivity(intent) }

        mtEnrichment.setOnClickListener {
            val intent = Intent(this, Practice_Enrichment::class.java)
            startActivity(intent) }


    }

// ================================== functions ==================================== //
    //====== openCV & STT result
    private fun bring_opencvResult() {
    val bring_html = CoroutineScope(Dispatchers.IO).async {
        val htmlResource: String
        try {
            val doc: Document = Jsoup.connect(searchUrl).get()
            val contents: String = doc.select("body h1").text()
            htmlResource = contents

            Log.d("OpenCv result1 : ", htmlResource)

            htmlResource
        } catch (e: Exception) {
            Log.i("get_result_opencv", "error")
            e.printStackTrace()
        }
    }

    // 메인 화면 결과 반영
    CoroutineScope(Dispatchers.Main).launch {
        result_opencv = bring_html.await().toString()

        // result_opencv
        bt_vowel = intent.getStringExtra("Vowel_bt").toString()  // button
        result_stt = intent.getStringExtra("STT_Result").toString()  //stt


        // opecv 결과 이미지 설정
        imageview_opencv = findViewById(R.id.mouthResult)
        when (result_opencv) {
            //a
            "a" -> {
                when (bt_vowel) {
                    "a" -> imageview_opencv.setImageResource(R.drawable.mouth_a)
                    "ga" -> imageview_opencv.setImageResource(R.drawable.mouth_ga)
                    "na" -> imageview_opencv.setImageResource(R.drawable.mouth_na)
                    "da" -> imageview_opencv.setImageResource(R.drawable.mouth_da)
                    else -> imageview_opencv.setImageResource(R.drawable.mouth_a)
                }
            }
            //eo
            "eo" -> {
                when (bt_vowel) {
                    "eo" -> imageview_opencv.setImageResource(R.drawable.mouth_eo)
                    "geo" -> imageview_opencv.setImageResource(R.drawable.mouth_geo)
                    "neo" -> imageview_opencv.setImageResource(R.drawable.mouth_neo)
                    "deo" -> imageview_opencv.setImageResource(R.drawable.mouth_deo)
                    else -> imageview_opencv.setImageResource(R.drawable.mouth_eo)
                }
            }
            //i
            "i" -> {
                when (bt_vowel) {
                    "i" -> imageview_opencv.setImageResource(R.drawable.mouth_i)
                    "gi" -> imageview_opencv.setImageResource(R.drawable.mouth_gi)
                    "ni" -> imageview_opencv.setImageResource(R.drawable.mouth_ni)
                    "di" -> imageview_opencv.setImageResource(R.drawable.mouth_di)
                    else -> imageview_opencv.setImageResource(R.drawable.mouth_i)
                }
            }
            //o
            "o" -> {
                when (bt_vowel) {
                    "o" -> imageview_opencv.setImageResource(R.drawable.mouth_o)
                    "go" -> imageview_opencv.setImageResource(R.drawable.mouth_go)
                    "no" -> imageview_opencv.setImageResource(R.drawable.mouth_no)
                    "do" -> imageview_opencv.setImageResource(R.drawable.mouth_do)
                    else -> imageview_opencv.setImageResource(R.drawable.mouth_o)
                }
            }
            //u
            "u" -> {
                when (bt_vowel) {
                    "u" -> imageview_opencv.setImageResource(R.drawable.mouth_u)
                    "gu" -> imageview_opencv.setImageResource(R.drawable.mouth_gu)
                    "nu" -> imageview_opencv.setImageResource(R.drawable.mouth_nu)
                    "duu" -> imageview_opencv.setImageResource(R.drawable.mouth_du)
                    else -> imageview_opencv.setImageResource(R.drawable.mouth_u)
                }
            }
            //e
            "e" -> {
                when (bt_vowel) {
                    "e" -> imageview_opencv.setImageResource(R.drawable.mouth_e)
                    "ge" -> imageview_opencv.setImageResource(R.drawable.mouth_ge)
                    "ne" -> imageview_opencv.setImageResource(R.drawable.mouth_ne)
                    "de" -> imageview_opencv.setImageResource(R.drawable.mouth_de)
                    else -> imageview_opencv.setImageResource(R.drawable.mouth_e)
                }
            }
            //eu
            "eu" -> {
                when (bt_vowel) {
                    "eu" -> imageview_opencv.setImageResource(R.drawable.mouth_eu)
                    "geu" -> imageview_opencv.setImageResource(R.drawable.mouth_geu)
                    "neu" -> imageview_opencv.setImageResource(R.drawable.mouth_neu)
                    "deu" -> imageview_opencv.setImageResource(R.drawable.mouth_deu)
                    else -> imageview_opencv.setImageResource(R.drawable.mouth_eu)
                }
            }
            else -> imageview_opencv.setImageResource(R.drawable.mouth_error)
        }

        // stt 결과 이미지 설정
        imageview_stt = findViewById(R.id.voiceResult)
        when (result_stt) {
            // stt 결과 여러 오류 반영한 예외처리 포함됨
            // === a
            "[아]" -> imageview_stt.setImageResource(R.drawable.voice_a)
            //
            "[가]" -> imageview_stt.setImageResource(R.drawable.voice_ga)
            "[나]" -> imageview_stt.setImageResource(R.drawable.voice_na)
            "[다]" -> imageview_stt.setImageResource(R.drawable.voice_da)

            // === eo
            "[어]" -> imageview_stt.setImageResource(R.drawable.voice_eo)
            //
            "[거]" -> imageview_stt.setImageResource(R.drawable.voice_geo)
            "[너]" -> imageview_stt.setImageResource(R.drawable.voice_neo)
            "[더]" -> imageview_stt.setImageResource(R.drawable.voice_deo)

            // === i
            "[이]" -> imageview_stt.setImageResource(R.drawable.voice_i)
            "[이야]" -> imageview_stt.setImageResource(R.drawable.voice_i)
            "[띠]" -> imageview_stt.setImageResource(R.drawable.voice_i)
            "[2]" -> imageview_stt.setImageResource(R.drawable.voice_i)
            //
            "[기]" -> imageview_stt.setImageResource(R.drawable.voice_gi)
            "[니]" -> imageview_stt.setImageResource(R.drawable.voice_ni)
            "[디]" -> imageview_stt.setImageResource(R.drawable.voice_di)

            // === o
            "[오]" -> imageview_stt.setImageResource(R.drawable.voice_o)
            "[오오]" -> imageview_stt.setImageResource(R.drawable.voice_o)
            "[5]" -> imageview_stt.setImageResource(R.drawable.voice_o)
            "[꼬모]" -> imageview_stt.setImageResource(R.drawable.voice_o)
            "[보]" -> imageview_stt.setImageResource(R.drawable.voice_o)
            "[응]" -> imageview_stt.setImageResource(R.drawable.voice_o)
            //
            "[고]" -> imageview_stt.setImageResource(R.drawable.voice_go)
            "[노]" -> imageview_stt.setImageResource(R.drawable.voice_no)
            "[도]" -> imageview_stt.setImageResource(R.drawable.voice_do)


            // === u
            "[우]" -> imageview_stt.setImageResource(R.drawable.voice_u)
            "[우우]" -> imageview_stt.setImageResource(R.drawable.voice_u)
            "[우와]" -> imageview_stt.setImageResource(R.drawable.voice_u)
            "[운]" -> imageview_stt.setImageResource(R.drawable.voice_u)
            "[9]" -> imageview_stt.setImageResource(R.drawable.voice_u)
            "[꾸]" -> imageview_stt.setImageResource(R.drawable.voice_u)
            "[부]" -> imageview_stt.setImageResource(R.drawable.voice_u)
            "[무]" -> imageview_stt.setImageResource(R.drawable.voice_u)
            //
            "[구]" -> imageview_stt.setImageResource(R.drawable.voice_gu)
            "[누]" -> imageview_stt.setImageResource(R.drawable.voice_nu)
            "[두]" -> imageview_stt.setImageResource(R.drawable.voice_du)

            // === e
            "[에]" -> imageview_stt.setImageResource(R.drawable.voice_e)
            "[애]" -> imageview_stt.setImageResource(R.drawable.voice_e)
            "[예]" -> imageview_stt.setImageResource(R.drawable.voice_e)
            "[얘]" -> imageview_stt.setImageResource(R.drawable.voice_e)
            "[m]" -> imageview_stt.setImageResource(R.drawable.voice_e)
            "[n]" -> imageview_stt.setImageResource(R.drawable.voice_e)
            "[예에]" -> imageview_stt.setImageResource(R.drawable.voice_e)
            "[앱]" -> imageview_stt.setImageResource(R.drawable.voice_e)
            "[헤헤]" -> imageview_stt.setImageResource(R.drawable.voice_e)
            "[때]" -> imageview_stt.setImageResource(R.drawable.voice_e)
            "[a]" -> imageview_stt.setImageResource(R.drawable.voice_e)
            //
            "[개]" -> imageview_stt.setImageResource(R.drawable.voice_ge)
            "[게]" -> imageview_stt.setImageResource(R.drawable.voice_ge)
            "[내]" -> imageview_stt.setImageResource(R.drawable.voice_ne)
            "[네]" -> imageview_stt.setImageResource(R.drawable.voice_ne)
            "[대]" -> imageview_stt.setImageResource(R.drawable.voice_de)
            "[데]" -> imageview_stt.setImageResource(R.drawable.voice_de)

            // === eu
            "[으]" -> imageview_stt.setImageResource(R.drawable.voice_eu)
            //
            "[그]" -> imageview_stt.setImageResource(R.drawable.voice_geu)
            "[느]" -> imageview_stt.setImageResource(R.drawable.voice_neu)
            "[드]" -> imageview_stt.setImageResource(R.drawable.voice_deu)

            else -> imageview_stt.setImageResource(R.drawable.voice_error)
        }

        // totall result 결과 이미지 설정
        imageview_totall = findViewById(R.id.totallResult)
        when (bt_vowel) {
            // === a
            "a" -> {
                if (result_opencv == "a" && result_stt == "[아]") {
                    imageview_totall.setImageResource(R.drawable.result_goodjob)
                } else {
                    imageview_totall.setImageResource(R.drawable.result_tryagain)
                }
            }
            //
            "ga" -> {
                if (result_opencv == "a" && (result_stt == "[가]")) {
                    imageview_totall.setImageResource(R.drawable.result_goodjob)
                } else {
                    imageview_totall.setImageResource(R.drawable.result_tryagain)
                }
            }
            "na" -> {
                if (result_opencv == "a" && (result_stt == "[나]")) {
                    imageview_totall.setImageResource(R.drawable.result_goodjob)
                } else {
                    imageview_totall.setImageResource(R.drawable.result_tryagain)
                }
            }
            "da" -> {
                if (result_opencv == "a" && (result_stt == "[다]")) {
                    imageview_totall.setImageResource(R.drawable.result_goodjob)
                } else {
                    imageview_totall.setImageResource(R.drawable.result_tryagain)
                }
            }

            // === eo
            "eo" -> {
                if (result_opencv == "eo" && result_stt == "[어]") {
                    imageview_totall.setImageResource(R.drawable.result_goodjob)
                } else {
                    imageview_totall.setImageResource(R.drawable.result_tryagain)
                }
            }
            //
            "geo" -> {
                if (result_opencv == "eo" && (result_stt == "[거]")) {
                    imageview_totall.setImageResource(R.drawable.result_goodjob)
                } else {
                    imageview_totall.setImageResource(R.drawable.result_tryagain)
                }
            }
            "neo" -> {
                if (result_opencv == "eo" && (result_stt == "[너]")) {
                    imageview_totall.setImageResource(R.drawable.result_goodjob)
                } else {
                    imageview_totall.setImageResource(R.drawable.result_tryagain)
                }
            }
            "deo" -> {
                if (result_opencv == "eo" && (result_stt == "[더]")) {
                    imageview_totall.setImageResource(R.drawable.result_goodjob)
                } else {
                    imageview_totall.setImageResource(R.drawable.result_tryagain)
                }
            }

            // === i
            "i" -> {
                if (result_opencv == "i" && result_stt == "[이]") {
                    imageview_totall.setImageResource(R.drawable.result_goodjob)
                } else if (result_opencv == "i" && result_stt == "[이야]") {
                    imageview_totall.setImageResource(R.drawable.result_goodjob)
                } else if (result_opencv == "i" && result_stt == "[띠]") {
                    imageview_totall.setImageResource(R.drawable.result_goodjob)
                } else {
                    imageview_totall.setImageResource(R.drawable.result_tryagain)
                }
            }
            //
            "gi" -> {
                if (result_opencv == "i" && (result_stt == "[기]")) {
                    imageview_totall.setImageResource(R.drawable.result_goodjob)
                } else {
                    imageview_totall.setImageResource(R.drawable.result_tryagain)
                }
            }
            "ni" -> {
                if (result_opencv == "i" && (result_stt == "[니]")) {
                    imageview_totall.setImageResource(R.drawable.result_goodjob)
                } else {
                    imageview_totall.setImageResource(R.drawable.result_tryagain)
                }
            }
            "di" -> {
                if (result_opencv == "i" && (result_stt == "[디]")) {
                    imageview_totall.setImageResource(R.drawable.result_goodjob)
                } else {
                    imageview_totall.setImageResource(R.drawable.result_tryagain)
                }
            }

            // === o
            "o" -> {
                if (result_opencv == "o" && result_stt == "[오]") {
                    imageview_totall.setImageResource(R.drawable.result_goodjob)
                } else if (result_opencv == "o" && result_stt == "[오오]") {
                    imageview_totall.setImageResource(R.drawable.result_goodjob)
                } else if (result_opencv == "o" && result_stt == "[5]") {
                    imageview_totall.setImageResource(R.drawable.result_goodjob)
                } else if (result_opencv == "o" && result_stt == "[꼬모]") {
                    imageview_totall.setImageResource(R.drawable.result_goodjob)
                } else if (result_opencv == "o" && result_stt == "[보]") {
                    imageview_totall.setImageResource(R.drawable.result_goodjob)
                } else if (result_opencv == "o" && result_stt == "[응]") {
                    imageview_totall.setImageResource(R.drawable.result_goodjob)
                } else {
                    imageview_totall.setImageResource(R.drawable.result_tryagain)
                }
            }
            //
            "go" -> {
                if (result_opencv == "o" && (result_stt == "[고]")) {
                    imageview_totall.setImageResource(R.drawable.result_goodjob)
                } else {
                    imageview_totall.setImageResource(R.drawable.result_tryagain)
                }
            }
            "no" -> {
                if (result_opencv == "o" && (result_stt == "[노]")) {
                    imageview_totall.setImageResource(R.drawable.result_goodjob)
                } else {
                    imageview_totall.setImageResource(R.drawable.result_tryagain)
                }
            }
            "do" -> {
                if (result_opencv == "o" && (result_stt == "[도]")) {
                    imageview_totall.setImageResource(R.drawable.result_goodjob)
                } else {
                    imageview_totall.setImageResource(R.drawable.result_tryagain)
                }
            }

            // === u
            "u" -> {
                if (result_opencv == "u" && (result_stt == "[우]")) {
                    imageview_totall.setImageResource(R.drawable.result_goodjob)
                } else if (result_opencv == "u" && result_stt == "[우우]") {
                    imageview_totall.setImageResource(R.drawable.result_goodjob)
                } else if (result_opencv == "u" && result_stt == "[우와]") {
                    imageview_totall.setImageResource(R.drawable.result_goodjob)
                } else if (result_opencv == "u" && result_stt == "[운]") {
                    imageview_totall.setImageResource(R.drawable.result_goodjob)
                } else if (result_opencv == "u" && result_stt == "[9]") {
                    imageview_totall.setImageResource(R.drawable.result_goodjob)
                } else if (result_opencv == "u" && result_stt == "[꾸]") {
                    imageview_totall.setImageResource(R.drawable.result_goodjob)
                } else if (result_opencv == "u" && result_stt == "[부]") {
                    imageview_totall.setImageResource(R.drawable.result_goodjob)
                } else if (result_opencv == "u" && result_stt == "[무]") {
                    imageview_totall.setImageResource(R.drawable.result_goodjob)
                } else {
                    imageview_totall.setImageResource(R.drawable.result_tryagain)
                }
            }
            //
            "gu" -> {
                if (result_opencv == "u" && (result_stt == "[구]")) {
                    imageview_totall.setImageResource(R.drawable.result_goodjob)
                } else {
                    imageview_totall.setImageResource(R.drawable.result_tryagain)
                }
            }
            "nu" -> {
                if (result_opencv == "u" && (result_stt == "[누]")) {
                    imageview_totall.setImageResource(R.drawable.result_goodjob)
                } else {
                    imageview_totall.setImageResource(R.drawable.result_tryagain)
                }
            }
            "du" -> {
                if (result_opencv == "u" && (result_stt == "[두]")) {
                    imageview_totall.setImageResource(R.drawable.result_goodjob)
                } else {
                    imageview_totall.setImageResource(R.drawable.result_tryagain)
                }
            }

            // === e
            "e" -> {
                if (result_opencv == "e" && result_stt == "[에]") {
                    imageview_totall.setImageResource(R.drawable.result_goodjob)
                } else if (result_opencv == "e" && result_stt == "[애]") {
                    imageview_totall.setImageResource(R.drawable.result_goodjob)
                } else if (result_opencv == "e" && result_stt == "[에]") {
                    imageview_totall.setImageResource(R.drawable.result_goodjob)
                } else if (result_opencv == "e" && result_stt == "[앱]") {
                    imageview_totall.setImageResource(R.drawable.result_goodjob)
                } else if (result_opencv == "e" && result_stt == "[예]") {
                    imageview_totall.setImageResource(R.drawable.result_goodjob)
                } else if (result_opencv == "e" && result_stt == "[m]") {
                    imageview_totall.setImageResource(R.drawable.result_goodjob)
                } else if (result_opencv == "e" && result_stt == "[n]") {
                    imageview_totall.setImageResource(R.drawable.result_goodjob)
                } else if (result_opencv == "e" && result_stt == "[예에]") {
                    imageview_totall.setImageResource(R.drawable.result_goodjob)
                } else if (result_opencv == "e" && result_stt == "[헤헤]") {
                    imageview_totall.setImageResource(R.drawable.result_goodjob)
                } else if (result_opencv == "e" && result_stt == "[때]") {
                    imageview_totall.setImageResource(R.drawable.result_goodjob)
                } else if (result_opencv == "e" && result_stt == "[a]") {
                    imageview_totall.setImageResource(R.drawable.result_goodjob)
                } else {
                    imageview_totall.setImageResource(R.drawable.result_tryagain)
                }
            }
            //
            "ge" -> {
                if (result_opencv == "e" && result_stt == "[개]") {
                    imageview_totall.setImageResource(R.drawable.result_goodjob)
                } else if (result_opencv == "e" && result_stt == "[게]") {
                    imageview_totall.setImageResource(R.drawable.result_goodjob)
                } else {
                    imageview_totall.setImageResource(R.drawable.result_tryagain)
                }
            }
            "ne" -> {
                if (result_opencv == "e" && result_stt == "[내]") {
                    imageview_totall.setImageResource(R.drawable.result_goodjob)
                } else if (result_opencv == "e" && result_stt == "[네]") {
                    imageview_totall.setImageResource(R.drawable.result_goodjob)
                } else {
                    imageview_totall.setImageResource(R.drawable.result_tryagain)
                }
            }
            "de" -> {
                if (result_opencv == "e" && result_stt == "[대]") {
                    imageview_totall.setImageResource(R.drawable.result_goodjob)
                } else if (result_opencv == "e" && result_stt == "[데]") {
                    imageview_totall.setImageResource(R.drawable.result_goodjob)
                } else {
                    imageview_totall.setImageResource(R.drawable.result_tryagain)
                }
            }

            // === eu
            "eu" -> {
                if (result_opencv == "eu" && result_stt == "[으]") {
                    imageview_totall.setImageResource(R.drawable.result_goodjob)
                }
            }
            //
            "geu" -> {
                if (result_opencv == "eu" && result_stt == "[그]") {
                    imageview_totall.setImageResource(R.drawable.result_goodjob)
                } else {
                    imageview_totall.setImageResource(R.drawable.result_tryagain)
                }
            }
            "neu" -> {
                if (result_opencv == "eu" && result_stt == "[느]") {
                    imageview_totall.setImageResource(R.drawable.result_goodjob)
                } else {
                    imageview_totall.setImageResource(R.drawable.result_tryagain)
                }
            }
            "deu" -> {
                if (result_opencv == "eu" && result_stt == "[드]") {
                    imageview_totall.setImageResource(R.drawable.result_goodjob)
                } else {
                    imageview_totall.setImageResource(R.drawable.result_tryagain)
                }
            }

            else -> {
                imageview_totall.setImageResource(R.drawable.result_tryagain)
            }
        }

        // User face Image View
        imageView_result = findViewById(R.id.imageResult_view)
        if (intent.hasExtra("ImageUri")) {
            imageResult_string = intent.getStringExtra("ImageUri").toString()
            imageResult_uri = Uri.parse(imageResult_string)
            Log.d("image_uri_inR", imageResult_string)
            imageView_result.setImageURI(imageResult_uri)
        } else {
            Log.d("이미지 uri 받기", "error")
        }
    }
}

}