package com.graduate.howtospeak

import android.Manifest
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.widget.Toast
import com.graduate.howtospeak.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    //lateinit var binding : ActivityMainBinding



    //Permission
    private val CAMERA_PERMISSION = arrayOf(Manifest.permission.CAMERA)
    private val CAMERA_PERMISSION_FLAG = 100
    private val STORAGE_PERMISSION = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private val STORAGE_PERMISSION_FLAG = 200



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setContentView(R.layout.activity_main)

/*
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

 */


        /* learn_con1.setOnClickListener {
            val intent = Intent(this, Learn_Consonant::class.java)
            startActivity(intent) } */

        learn_vowel1.setOnClickListener {
            val intent = Intent(this, Learn_Vowel::class.java)
            startActivity(intent) }

        /* practice_con1.setOnClickListener {
            val intent = Intent(this, Practice_Consonant::class.java)
            startActivity(intent) } */

        practice_vowel1.setOnClickListener {
            val intent = Intent(this, Practice_Vowel::class.java)
            startActivity(intent) }



        // permission
        if(checkPermission(CAMERA_PERMISSION, CAMERA_PERMISSION_FLAG)){
            checkPermission(STORAGE_PERMISSION, STORAGE_PERMISSION_FLAG)
        }

    }



    //permission
    private fun checkPermission(permissions : Array<out String>, flag : Int):Boolean{
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (permission in permissions) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(this, permissions, flag)
                    return false
                }
            }
        }
        return true
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            CAMERA_PERMISSION_FLAG -> {
                for (grant in grantResults) {
                    if (grant != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "카메라 권한을 승인해야지만 앱을 사용 할 수 있습니다.", Toast.LENGTH_LONG)
                            .show()
                        finish()
                    } else {
                        checkPermission(STORAGE_PERMISSION, STORAGE_PERMISSION_FLAG)
                    }
                }
            }
            STORAGE_PERMISSION_FLAG -> {
                for (grant in grantResults) {
                    if (grant != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "저장소 권한을 승인해야지만 앱을 사용 할 수 있습니다.", Toast.LENGTH_LONG)
                            .show()
                        finish()
                    }
                }
            }
        }
    }




}