package com.graduate.howtospeak

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import kotlin.math.abs


class LineBarVisualizer : BaseVisualizer {
    private var middleLine: Paint? = null
    private var density = 0f
    private var gap = 0

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr)

    override fun init() {
        density = 50f
        gap = 4
        middleLine = Paint()
        middleLine!!.color = Color.BLUE
    }

    fun setDensity(density: Float) {
        if (this.density > 180) {
            middleLine!!.strokeWidth = 1f
            gap = 1
        } else {
            gap = 4
        }
        this.density = density
        if (density > 256) {
            this.density = 250f
            gap = 0
        } else if (density <= 10) {
            this.density = 10f
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (middleLine!!.color != Color.BLUE) {
            middleLine!!.color = color
        }
        if (bytes != null) {
            val barWidth: Float = width / density
            val div: Float = bytes!!.size / density
            canvas.drawLine(
                0f, (height / 2).toFloat(), width.toFloat(), (height / 2).toFloat(),
                middleLine!!
            )
            paint?.setStrokeWidth(barWidth - gap)
            var i = 0
            while (i < density) {
                val bytePosition = Math.ceil((i * div).toDouble()).toInt()
                val top: Int = (height / 2
                        + (128 - Math.abs(bytes!!.get(bytePosition)))
                        * (height / 2) / 128)
                val bottom: Int = (height / 2
                        - (128 - Math.abs(bytes!!.get(bytePosition)))
                        * (height / 2) / 128)
                val barX = i * barWidth + barWidth / 2
                canvas.drawLine(barX, bottom.toFloat(), barX, (height / 2).toFloat(), paint!!)
                canvas.drawLine(barX, top.toFloat(), barX, (height / 2).toFloat(), paint!!)
                i++
            }
            super.onDraw(canvas)
        }
    }
}