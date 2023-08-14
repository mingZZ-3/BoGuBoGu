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

package com.graduate.howtospeak.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.TypedValue
import com.graduate.howtospeak.VisualizerBase


class Barvisualizer
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : VisualizerBase(context, attrs, defStyleAttr) {

    companion object {
        private const val barWidth = 1.5f        // [dp] 5
        private const val gapWidth = 2f        // [dp] 5
    }

    private val paint: Paint

    private val barWidthPix: Float
    private val gapWidthPix: Float

    private var barXBoards: List<Pair<Float, Float>>? = null


    init {
        paint = Paint()
            .apply {
                this.color = Color.GRAY
                this.style = Paint.Style.FILL
                this.isAntiAlias = true
            }

        resources.displayMetrics
            .also { displayMetrics ->
                barWidthPix = convertDpToPx(barWidth, displayMetrics)
                gapWidthPix = convertDpToPx(gapWidth, displayMetrics)
            }
    }


    override fun draw(rawData: ByteArray, canvas: Canvas) {
        if(barXBoards == null) {
            barXBoards = calculateBarXBoards(canvas.width)
        }

        val dataItemsInBar = rawData.size / barXBoards!!.size

        var dataItemsCounter = 0
        var barsCounter = 0
        var dataItemsSum = 0

        rawData.forEach { dataItem ->
            dataItemsSum += dataItem
            dataItemsCounter++

            if(dataItemsCounter == dataItemsInBar && barsCounter < barXBoards!!.size) {
                val averageData = dataItemsSum / dataItemsInBar

                drawBar(averageData, barXBoards!![barsCounter], canvas)

                barsCounter++

                dataItemsCounter = 0
                dataItemsSum = 0
            }
        }
    }



    private fun convertDpToPx(dpValue: Float, displayMetrics: DisplayMetrics): Float =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, displayMetrics)


    // Calculates offset from edges of a canvas to bars
    private fun calculateEdgeOffset(canvasWidth: Int): Int =
        ((canvasWidth/2 - barWidthPix/2) % (barWidthPix + gapWidthPix)).toInt()


    // Calculates bar boards in a canvas
    private fun calculateBarXBoards(canvasWidth: Int): List<Pair<Float, Float>> {

        val edgeOffset = calculateEdgeOffset(canvasWidth)

        val result = mutableListOf<Pair<Float, Float>>()

        var left = edgeOffset.toFloat()
        var right = left + barWidthPix

        do {
            result.add(Pair(left, right))

            left = right + gapWidthPix
            right = left + barWidthPix
        }
        while(right <= canvasWidth)

        return result }

    private fun drawBar(sourceData: Int, xBoards: Pair<Float, Float>, canvas: Canvas) {
        val normalizedData = sourceData + 128 //128

        val barHeight = canvas.height * (normalizedData / 256f) //256

        val barTop = (canvas.height-barHeight)/2
        val barRect = RectF(xBoards.first, barTop, xBoards.second, canvas.height-barTop)

        canvas.drawRoundRect(barRect, 100f, 100f, paint)
    }
}