package com.vinay.catch_the_ball.util

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioAttributes.Builder
import android.media.SoundPool
import android.media.AudioManager
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import com.vinay.catch_the_ball.util.SoundPlayer.Companion.hitSound
import com.vinay.catch_the_ball.util.SoundPlayer.Companion.overSound
import com.vinay.catch_the_ball.util.SoundPlayer.Companion.soundPool

class SoundPlayer() {

    companion object {
        private var soundPool: SoundPool? = null
        private const val hitSound = 0
        private const val overSound = 0
    }
    init {
        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            val audioAttributes = Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
            soundPool = SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                .setMaxStreams(2).build()
        } else {
            soundPool = SoundPool(2, AudioManager.STREAM_MUSIC, 0)
        }

        //hitSound = soundPool.load(context,R.raw.sdf,1)
        //  overSound = soundPool.load(context,R.raw.sdf,1)
    }

    fun playHitSound() {
        soundPool?.play(hitSound, 1.0f, 1.0f, 1, 0, 1.0f)
    }

    fun playOverSound() {
        soundPool?.play(overSound, 1.0f, 1.0f, 1, 0, 1.0f)
    }


}