package com.graduate.howtospeak

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.media.MediaPlayer
import android.media.audiofx.Visualizer
import android.media.audiofx.Visualizer.OnDataCaptureListener
import android.util.AttributeSet
import android.view.View

abstract class BaseVisualizer : View {
    protected var bytes: ByteArray? = null
    protected var paint: Paint? = null
    var visualizer: Visualizer? = null
    protected var color = Color.BLUE


    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr) {
        init(attrs)
        init()
    }

    private fun init(attributeSet: AttributeSet?) {
        paint = Paint()
    }


    @Deprecated(
        """will be removed in next version use {@link com.graduate.howtospeak.BaseVisualizer#setPlayer(int)} instead
      """)
    fun setPlayer(mediaPlayer: MediaPlayer) {
        setPlayer(mediaPlayer.audioSessionId)
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
        //will be null if setPlayer hasn't yet been called
        if (visualizer == null) return
        visualizer!!.release()
        bytes = null
        invalidate()
    }

    protected abstract fun init()
}