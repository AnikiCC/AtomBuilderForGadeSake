package com.example.atombuilder.services

import android.content.Context
import android.media.MediaPlayer
import android.media.SoundPool
import com.example.atombuilder.R

class SoundManager(private val context: Context) {
    private val menuMusic = R.raw.background_music
    private val gameMusic = R.raw.gameplay_music
    private val challengeMusic = R.raw.challenge_music

    private val buttonClickSoundId: Int
    private val successSoundId: Int
    private val failureSoundId: Int

    private var mediaPlayer: MediaPlayer? = null
    private val soundPool = SoundPool.Builder().setMaxStreams(5).build()

    private var musicVolume = 1f
    private var soundEffectsVolume = 1f

    init {
        buttonClickSoundId = soundPool.load(context, R.raw.button_click, 1)
        successSoundId = soundPool.load(context, R.raw.success, 1)
        failureSoundId = soundPool.load(context, R.raw.failure, 1)
    }

    fun playMenuMusic() = playMusic(menuMusic)
    fun playGameMusic() = playMusic(gameMusic)
    fun playChallengeMusic() = playMusic(challengeMusic)

    private fun playMusic(resId: Int) {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.release()
        }
        mediaPlayer = MediaPlayer.create(context, resId).apply {
            isLooping = true
            setVolume(musicVolume, musicVolume)
            start()
        }
    }

    fun pauseMusic() {
        mediaPlayer?.pause()
    }

    fun resumeMusic() {
        mediaPlayer?.start()
    }

    fun playButtonClick() {
        soundPool.play(buttonClickSoundId, soundEffectsVolume, soundEffectsVolume, 0, 0, 1f)
    }

    fun playSuccess() {
        soundPool.play(successSoundId, soundEffectsVolume, soundEffectsVolume, 0, 0, 1f)
    }

    fun playFailure() {
        soundPool.play(failureSoundId, soundEffectsVolume, soundEffectsVolume, 0, 0, 1f)
    }

    fun setMusicVolume(volume: Float) {
        musicVolume = volume
        mediaPlayer?.setVolume(volume, volume)
    }

    fun setSoundEffectsVolume(volume: Float) {
        soundEffectsVolume = volume
    }

    fun getMusicVolume(): Float = musicVolume
    fun getSoundEffectsVolume(): Float = soundEffectsVolume

    fun release() {
        mediaPlayer?.release()
        soundPool.release()
    }
}