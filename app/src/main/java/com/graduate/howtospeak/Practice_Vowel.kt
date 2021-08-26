package com.graduate.howtospeak

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.*
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ImageButton
import kotlinx.android.synthetic.main.activity_learn.mtMain1
import kotlinx.android.synthetic.main.activity_learn__vowel.*
import kotlinx.android.synthetic.main.activity_practice__vowel.*



class Practice_Vowel : AppCompatActivity() {

    private lateinit var bt_name1: ImageButton
    private lateinit var bt_name2: ImageButton
    private lateinit var bt_name3: ImageButton
    private lateinit var bt_name4: ImageButton
    private lateinit var bt_name5: ImageButton
    private lateinit var bt_name6: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setContentView(R.layout.activity_practice__vowel)

        // 상태바 없애기
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN ,
                WindowManager.LayoutParams.FLAG_FULLSCREEN ) }

        bt_name1 = findViewById(R.id.pvowel_a)
        bt_name2 = findViewById(R.id.pvowel_eo)
        bt_name3 = findViewById(R.id.pvowel_u)
        bt_name4 = findViewById(R.id.pvowel_o)
        bt_name5 = findViewById(R.id.pvowel_i)
        bt_name6 = findViewById(R.id.pvowel_e)

        // 기본 버튼
        mtMain1.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent) }

        pvowel_a.setOnClickListener {
            val intent = Intent(this, PracticeActivity::class.java)
            startActivity(intent) }


        // vowel 버튼
        pvowel_a.setOnClickListener {
            val intent = Intent(this, PracticeActivity::class.java)
            intent.putExtra("vowel_tolearn", bt_name1.tag.toString())
            startActivity(intent) }

        pvowel_eo.setOnClickListener {
            val intent = Intent(this, PracticeActivity::class.java)
            intent.putExtra("vowel_tolearn", bt_name2.tag.toString())
            startActivity(intent) }

        pvowel_u.setOnClickListener {
            val intent = Intent(this, PracticeActivity::class.java)
            intent.putExtra("vowel_tolearn", bt_name3.tag.toString())
            startActivity(intent) }

        pvowel_o.setOnClickListener {
            val intent = Intent(this, PracticeActivity::class.java)
            intent.putExtra("vowel_tolearn", bt_name4.tag.toString())
            startActivity(intent) }

        pvowel_i.setOnClickListener {
            val intent = Intent(this, PracticeActivity::class.java)
            intent.putExtra("vowel_tolearn", bt_name5.tag.toString())
            startActivity(intent) }

        pvowel_e.setOnClickListener {
            val intent = Intent(this, PracticeActivity::class.java)
            intent.putExtra("vowel_tolearn", bt_name6.tag.toString())
            startActivity(intent) }


    }


}