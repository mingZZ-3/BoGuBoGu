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

import android.content.Context
import android.graphics.Canvas
import android.media.audiofx.Visualizer
import android.util.AttributeSet
import android.view.View

abstract class VisualizerBase
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    protected var audioBytes: ByteArray? = null
    private var visualizer: Visualizer? = null


    fun setAudioSessionId(audioSessionId: Int) {
        release()

        visualizer = Visualizer(audioSessionId)
            .apply {
                this.captureSize = Visualizer.getCaptureSizeRange()[1]    // Capture size is maximum

                this.setDataCaptureListener(object: Visualizer.OnDataCaptureListener {
                    override fun onFftDataCapture(visualizer: Visualizer?, fft: ByteArray?, samplingRate: Int) {
                    }

                    override fun onWaveFormDataCapture(visualizer: Visualizer?, waveform: ByteArray?, samplingRate: Int) {
                        waveform?.also {
                            audioBytes = it.copyOf()
                            invalidate()
                        }
                    }
                }, Visualizer.getMaxCaptureRate() / 2, true, false)

                this.enabled = true
            }
    }


    fun release() {
        visualizer?.also { it.release() }
    }

    /**
     *
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        audioBytes
            ?.let {
                if(it.isNotEmpty()) {
                    draw(it, canvas)
                }
            }
    }

    /**
     * Draw algorithm must be implemented here
     */
    protected abstract fun draw(rawData: ByteArray, canvas: Canvas)
}