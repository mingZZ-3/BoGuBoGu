package com.graduate.howtospeak

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ImageButton
import kotlinx.android.synthetic.main.activity_learn.*
import kotlinx.android.synthetic.main.activity_practice_enrichment.*
import kotlinx.android.synthetic.main.activity_practice_enrichment.mtMain1

class Practice_Enrichment : AppCompatActivity() {

    //====== 변수 ======//
    private lateinit var bt_name1: ImageButton
    private lateinit var bt_name2: ImageButton
    private lateinit var bt_name3: ImageButton
    /*
    private lateinit var bt_name4: ImageButton
    private lateinit var bt_name5: ImageButton
    private lateinit var bt_name6: ImageButton
     */
    private lateinit var bt_name7: ImageButton
    private lateinit var bt_name8: ImageButton
    private lateinit var bt_name9: ImageButton
    private lateinit var bt_name10: ImageButton
    private lateinit var bt_name11: ImageButton
    private lateinit var bt_name12: ImageButton
    private lateinit var bt_name13: ImageButton
    private lateinit var bt_name14: ImageButton
    private lateinit var bt_name15: ImageButton
    private lateinit var bt_name16: ImageButton
    private lateinit var bt_name17: ImageButton
    private lateinit var bt_name18: ImageButton
    private lateinit var bt_name19: ImageButton
    private lateinit var bt_name20: ImageButton
    private lateinit var bt_name21: ImageButton

// ================================== onCreate ==================================== //
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practice_enrichment)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        // 상태바 없애기
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        // 기본 버튼
        mtMain1.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent) }


        //a
        bt_name1 = findViewById(R.id.Evowel_ga)
        bt_name2 = findViewById(R.id.Evowel_na)
        bt_name3 = findViewById(R.id.Evowel_da)
        //eo
        /*
        bt_name4 = findViewById(R.id.Evowel_geo)
        bt_name5 = findViewById(R.id.Evowel_neo)
        bt_name6 = findViewById(R.id.Evowel_deo)
         */
        //u
        bt_name7 = findViewById(R.id.Evowel_gu)
        bt_name8 = findViewById(R.id.Evowel_nu)
        bt_name9= findViewById(R.id.Evowel_du)
        //o
        bt_name10 = findViewById(R.id.Evowel_go)
        bt_name11 = findViewById(R.id.Evowel_no)
        bt_name12 = findViewById(R.id.Evowel_do)
        //i
        bt_name13 = findViewById(R.id.Evowel_gi)
        bt_name14 = findViewById(R.id.Evowel_ni)
        bt_name15 = findViewById(R.id.Evowel_di)
        //e
        bt_name16 = findViewById(R.id.Evowel_ge)
        bt_name17 = findViewById(R.id.Evowel_ne)
        bt_name18 = findViewById(R.id.Evowel_de)
        //eu
        bt_name19 = findViewById(R.id.Evowel_geu)
        bt_name20 = findViewById(R.id.Evowel_neu)
        bt_name21 = findViewById(R.id.Evowel_deu)


        //  버튼
        //a
        Evowel_ga.setOnClickListener {
            // tag 값 전달
            val intent = Intent(this, PracticeActivity::class.java)
            intent.putExtra("vowel_tolearn", bt_name1.tag.toString())
            startActivity(intent) }
        Evowel_na.setOnClickListener {
            val intent = Intent(this, PracticeActivity::class.java)
            intent.putExtra("vowel_tolearn", bt_name2.tag.toString())
            startActivity(intent) }
        Evowel_da.setOnClickListener {
            val intent = Intent(this, PracticeActivity::class.java)
            intent.putExtra("vowel_tolearn", bt_name3.tag.toString())
            startActivity(intent) }

        /*
        //eo
        Evowel_geo.setOnClickListener {
            val intent = Intent(this, PracticeActivity::class.java)
            intent.putExtra("vowel_tolearn", bt_name4.tag.toString())
            startActivity(intent) }
        Evowel_neo.setOnClickListener {
            val intent = Intent(this, PracticeActivity::class.java)
            intent.putExtra("vowel_tolearn", bt_name5.tag.toString())
            startActivity(intent) }
        Evowel_deo.setOnClickListener {
            val intent = Intent(this, PracticeActivity::class.java)
            intent.putExtra("vowel_tolearn", bt_name6.tag.toString())
            startActivity(intent) }
         */

        //u
        Evowel_gu.setOnClickListener {
            val intent = Intent(this, PracticeActivity::class.java)
            intent.putExtra("vowel_tolearn", bt_name7.tag.toString())
            startActivity(intent) }
        Evowel_nu.setOnClickListener {
            val intent = Intent(this, PracticeActivity::class.java)
            intent.putExtra("vowel_tolearn", bt_name8.tag.toString())
            startActivity(intent) }
        Evowel_du.setOnClickListener {
            val intent = Intent(this, PracticeActivity::class.java)
            intent.putExtra("vowel_tolearn", bt_name9.tag.toString())
            startActivity(intent) }
        //o
        Evowel_go.setOnClickListener {
            val intent = Intent(this, PracticeActivity::class.java)
            intent.putExtra("vowel_tolearn", bt_name10.tag.toString())
            startActivity(intent) }
        Evowel_no.setOnClickListener {
            val intent = Intent(this, PracticeActivity::class.java)
            intent.putExtra("vowel_tolearn", bt_name11.tag.toString())
            startActivity(intent) }
        Evowel_do.setOnClickListener {
            val intent = Intent(this, PracticeActivity::class.java)
            intent.putExtra("vowel_tolearn", bt_name12.tag.toString())
            startActivity(intent) }
        //i
        Evowel_gi.setOnClickListener {
            val intent = Intent(this, PracticeActivity::class.java)
            intent.putExtra("vowel_tolearn", bt_name13.tag.toString())
            startActivity(intent) }
        Evowel_ni.setOnClickListener {
            val intent = Intent(this, PracticeActivity::class.java)
            intent.putExtra("vowel_tolearn", bt_name14.tag.toString())
            startActivity(intent) }
        Evowel_di.setOnClickListener {
            val intent = Intent(this, PracticeActivity::class.java)
            intent.putExtra("vowel_tolearn", bt_name15.tag.toString())
            startActivity(intent) }
        //e
        Evowel_ge.setOnClickListener {
            val intent = Intent(this, PracticeActivity::class.java)
            intent.putExtra("vowel_tolearn", bt_name16.tag.toString())
            startActivity(intent) }
        Evowel_ne.setOnClickListener {
            val intent = Intent(this, PracticeActivity::class.java)
            intent.putExtra("vowel_tolearn", bt_name17.tag.toString())
            startActivity(intent) }
        Evowel_de.setOnClickListener {
            val intent = Intent(this, PracticeActivity::class.java)
            intent.putExtra("vowel_tolearn", bt_name18.tag.toString())
            startActivity(intent) }
        //eu
        Evowel_geu.setOnClickListener {
            val intent = Intent(this, PracticeActivity::class.java)
            intent.putExtra("vowel_tolearn", bt_name19.tag.toString())
            startActivity(intent) }
        Evowel_neu.setOnClickListener {
            val intent = Intent(this, PracticeActivity::class.java)
            intent.putExtra("vowel_tolearn", bt_name20.tag.toString())
            startActivity(intent) }
        Evowel_deu.setOnClickListener {
            val intent = Intent(this, PracticeActivity::class.java)
            intent.putExtra("vowel_tolearn", bt_name21.tag.toString())
            startActivity(intent) }

    }
}