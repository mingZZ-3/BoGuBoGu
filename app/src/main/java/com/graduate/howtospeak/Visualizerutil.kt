package com.graduate.howtospeak

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import androidx.annotation.RawRes

class Visualizerutil {
    private var mediaPlayer: MediaPlayer? = null

    fun stop() {
        if (mediaPlayer != null) {
            mediaPlayer!!.release()
            mediaPlayer = null
        }
    }


    // user voice spectrum _ Uri
    fun play(context: Context, resUri: Uri, completedCallback: () -> Unit) {
        stop()

        MediaPlayer().apply {
            mediaPlayer = this
            setAudioStreamType(AudioManager.STREAM_MUSIC)
            setDataSource(context, resUri)
            prepare()

            this.setOnCompletionListener {
                stop()
                completedCallback()
            }

            this.start()
        }
    }

    // defalt voice spectrum _ file
    fun playId(context: Context, @RawRes resId: Int, completedCallback: () -> Unit) {
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

    fun getAudioSessionId(): Int? = mediaPlayer?.audioSessionId
}