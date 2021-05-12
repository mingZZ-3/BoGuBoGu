package com.graduate.howtospeak

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.media.audiofx.Visualizer
import android.media.audiofx.Visualizer.OnDataCaptureListener
import android.util.AttributeSet
import android.view.View

/*
abstract class BaseVisualizer : View {
    protected var bytes: ByteArray? = null
    protected var paint: Paint? = null
    var visualizer: Visualizer? = null
    protected var color = Color.BLUE


    constructor(context: Context?, attrs: AttributeSet?) : super(
        context, attrs) {
        init(attrs)
        init()
    }

    private fun init(attributeSet: AttributeSet?) {
        paint = Paint()
    }


    fun setPlayer(audioSessionId: Int) {
        visualizer = Visualizer(audioSessionId)
        visualizer!!.enabled = false
        visualizer!!.captureSize = Visualizer.getCaptureSizeRange()[1]
        visualizer!!.setDataCaptureListener(object : OnDataCaptureListener {
            override fun onWaveFormDataCapture(
                visualizer: Visualizer, bytes: ByteArray,
                samplingRate: Int
            ) {
                this@BaseVisualizer.bytes = bytes
                invalidate()
            }

            override fun onFftDataCapture(
                visualizer: Visualizer, bytes: ByteArray,
                samplingRate: Int
            ) {
            }
        }, Visualizer.getMaxCaptureRate() / 2, true, false)
        visualizer!!.enabled = true
    }

    fun release() {
        if (visualizer == null) return
        visualizer!!.release()
        bytes = null
        invalidate()
    }

    fun getVisualizer(): Visualizer? {
        return visualizer
    }

    protected abstract fun init()
}

 */