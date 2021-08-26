package com.graduate.howtospeak

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
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
    // opencv
    private lateinit var test_Jsoup: TextView
    private val searchUrl = "http://13.124.114.1/vowel_recognition/result"
    var result_opencv :String = ""


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

        // 버튼
        mtMain1.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent) }

        mtRDetail1.setOnClickListener {
            val intent = Intent(this, Practice_RDetail::class.java)
            startActivity(intent) }

        mtVowelPractice.setOnClickListener {
            val intent = Intent(this, Practice_Vowel::class.java)
            startActivity(intent) }


        // opencv 결과값
        test_Jsoup = findViewById(R.id.test_view)
        val result = bring_opencvResult()
        test_Jsoup.text = result



    }


    // opencv 결과값 불러오기
    private fun bring_opencvResult(): String {
        val bring_html = CoroutineScope(Dispatchers.IO).async {
            try {
                val doc : Document = Jsoup.connect(searchUrl).get()
                val contents: Elements = doc.select("body")
                contents.text()
                result_opencv = contents.toString()

                Log.d("Test", result_opencv)
                result_opencv
            } catch (e: Exception) {
                Log.i("get_result_opencv","error")
                e.printStackTrace()}
        }
        bring_html.start()

        return result_opencv
    }



    /*

    // uri --> bitmap
    private fun getImageUri(context: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path: String = MediaStore.Images.Media.insertImage(
            context.getContentResolver(),
            inImage,
            "Title",
            null
        )
        return Uri.parse(path)
    }
    */
}