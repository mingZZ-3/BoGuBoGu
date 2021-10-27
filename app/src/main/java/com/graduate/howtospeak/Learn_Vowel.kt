package com.graduate.howtospeak

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import kotlinx.android.synthetic.main.activity_learn__vowel.*

class Learn_Vowel : AppCompatActivity() {

    //====== 변수 ======//
    // 버튼
    private lateinit var bt_name1: ImageButton
    //private lateinit var bt_name2: ImageButton
    private lateinit var bt_name3: ImageButton
    private lateinit var bt_name4: ImageButton
    private lateinit var bt_name5: ImageButton
    private lateinit var bt_name6: ImageButton
    private lateinit var bt_name7: ImageButton


    // ================================== onCreate ==================================== //
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setContentView(R.layout.activity_learn__vowel)

        // 상태바 없애기
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN ,
                WindowManager.LayoutParams.FLAG_FULLSCREEN ) }

        // 버튼 xml
        bt_name1 = findViewById(R.id.lvowel_a)
        //bt_name2 = findViewById(R.id.lvowel_eo)
        bt_name3 = findViewById(R.id.lvowel_u)
        bt_name4 = findViewById(R.id.lvowel_o)
        bt_name5 = findViewById(R.id.lvowel_i)
        bt_name6 = findViewById(R.id.lvowel_e)
        bt_name7 = findViewById(R.id.lvowel_eu)



        // 상단 기본
        mtMain3.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent) }

        // 모음 버튼
        lvowel_a.setOnClickListener {
            // 버튼 tag 값 전달
            val intent = Intent(this, LearnActivity::class.java)
            intent.putExtra("vowel_tolearn", bt_name1.tag.toString())
            startActivity(intent) }

        /*
        lvowel_eo.setOnClickListener {
            val intent = Intent(this, LearnActivity::class.java)
            intent.putExtra("vowel_tolearn", bt_name2.tag.toString())
            startActivity(intent) }
         */

        lvowel_u.setOnClickListener {
            val intent = Intent(this, LearnActivity::class.java)
            intent.putExtra("vowel_tolearn", bt_name3.tag.toString())
            startActivity(intent) }

        lvowel_o.setOnClickListener {
            val intent = Intent(this, LearnActivity::class.java)
            intent.putExtra("vowel_tolearn", bt_name4.tag.toString())
            startActivity(intent) }

        lvowel_i.setOnClickListener {
            val intent = Intent(this, LearnActivity::class.java)
            intent.putExtra("vowel_tolearn", bt_name5.tag.toString())
            startActivity(intent) }

        lvowel_e.setOnClickListener {
            val intent = Intent(this, LearnActivity::class.java)
            intent.putExtra("vowel_tolearn", bt_name6.tag.toString())
            startActivity(intent) }

        lvowel_eu.setOnClickListener {
            val intent = Intent(this, LearnActivity::class.java)
            intent.putExtra("vowel_tolearn", bt_name7.tag.toString())
            startActivity(intent) }

    }
}