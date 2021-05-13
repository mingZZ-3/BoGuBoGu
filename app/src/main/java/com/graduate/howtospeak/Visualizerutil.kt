package com.graduate.howtospeak

import android.content.Context
import android.media.MediaPlayer
import android.support.annotation.RawRes

class Visualizerutil {
    private var mediaPlayer: MediaPlayer? = null

    fun stop() {
        if (mediaPlayer != null) {
            mediaPlayer!!.release()
            mediaPlayer = null
        }
    }

    fun play(context: Context, @RawRes resId: Int, completedCallback: () -> Unit) {
        stop()

        MediaPlayer.create(context, resId)
            .apply {
                mediaPlayer = this

                this.setOnCompletionListener {
                    stop()
                    completedCallback()
                }

                this.start()
            }
    }

    /**
     *
     */
    fun getAudioSessionId(): Int? = mediaPlayer?.audioSessionId
}