/*
* Copyright (C) 2017 Gautam Chibde
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.graduate.howtospeak

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.annotation.RawRes
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_learn.*
import kotlinx.android.synthetic.main.activity_learn.mtMain1
import kotlinx.android.synthetic.main.activity_practice__r_detail.*


class Practice_RDetail : AppCompatActivity() {
    // 접근 권한
    private lateinit var audioPlayer: Visualizerutil


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setContentView(R.layout.activity_practice__r_detail)


        // 홈 버튼
        mtMain1.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent) }

        // 다시 학습하기 버튼



        // 음성 시각화
        audioPlayer = Visualizerutil()
    }

    // 실행
    override fun onStart() {
        super.onStart()
        startPlayingAudio(R.raw.geu)
    }

    // 정지
    override fun onStop() {
        super.onStop()
        stopPlayingAudio();
    }

    //
    private fun startPlayingAudio(@RawRes resId: Int) {
        audioPlayer.play(this, resId) {
        }
        audioPlayer.getAudioSessionId()
            ?.also {
                barVisualizerPanel.setAudioSessionId(it)
            }
    }

    private fun stopPlayingAudio() {
        audioPlayer.stop();
        barVisualizerPanel.release()
    }


}

