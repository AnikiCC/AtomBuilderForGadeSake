package com.example.atombuilder

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.atombuilder.data.ProgressManager
import com.example.atombuilder.services.JsonDatabase
import com.example.atombuilder.services.SoundManager
import com.example.atombuilder.ui.screens.*
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.atombuilder.services.AchievementManager


class MainActivity : ComponentActivity() {
    private lateinit var preferences: SharedPreferences
    private lateinit var soundManager: SoundManager
    private lateinit var progressManager: ProgressManager
    lateinit var achievementManager: AchievementManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        achievementManager = AchievementManager(applicationContext)

        preferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        soundManager = SoundManager(applicationContext).apply {
            setMusicVolume(preferences.getFloat("music_volume", 1f))
            setSoundEffectsVolume(preferences.getFloat("sound_effects_volume", 1f))
        }
        progressManager = ProgressManager(applicationContext)

        val lifecycleObserver = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    soundManager.pauseMusic()
                }
                Lifecycle.Event.ON_RESUME -> {
                    soundManager.resumeMusic()
                }
                else -> {}
            }
        }

        lifecycle.addObserver(lifecycleObserver)

        setContent {
            var selectedScreen by remember { mutableStateOf(AppScreen.MainMenu) }
            val database = JsonDatabase(applicationContext)

            LaunchedEffect(selectedScreen) {
                when (selectedScreen) {
                    AppScreen.MainMenu, AppScreen.Settings, AppScreen.Progress ->
                        soundManager.playMenuMusic()
                    AppScreen.Game -> soundManager.playGameMusic()
                    AppScreen.Challenges -> soundManager.playChallengeMusic()
                }
            }

            when (selectedScreen) {
                AppScreen.MainMenu -> MainMenu(
                    onStartGame = { selectedScreen = AppScreen.Game },
                    onStartChallenges = { selectedScreen = AppScreen.Challenges },
                    onSettings = { selectedScreen = AppScreen.Settings },
                    onProgress = { selectedScreen = AppScreen.Progress },
                    onExit = { finish() },
                    playSound = soundManager::playButtonClick
                )

                AppScreen.Game -> AtomScreen(
                    database = database,
                    onBackToMenu = { selectedScreen = AppScreen.MainMenu },
                    playSound = soundManager::playButtonClick,
                    key = 0,
                    onActionPerformed = { soundManager.playButtonClick() }

                )

                AppScreen.Challenges -> ChallengesScreen(
                    onBackToMenu = { selectedScreen = AppScreen.MainMenu },
                    database = database,
                    progressManager = progressManager,
                    achievementManager = achievementManager,
                    playSound = soundManager::playButtonClick,
                    playSuccess = soundManager::playSuccess,
                    playFailure = soundManager::playFailure
                )

                AppScreen.Settings -> SettingsScreen(
                    onBackToMenu = { selectedScreen = AppScreen.MainMenu },
                    musicVolume = soundManager.getMusicVolume(),
                    soundEffectsVolume = soundManager.getSoundEffectsVolume(),
                    onMusicVolumeChange = {
                        soundManager.setMusicVolume(it)
                        preferences.edit().putFloat("music_volume", it).apply()
                    },
                    onSoundEffectsVolumeChange = {
                        soundManager.setSoundEffectsVolume(it)
                        preferences.edit().putFloat("sound_effects_volume", it).apply()
                    },
                    playSound = soundManager::playButtonClick
                )

                AppScreen.Progress -> ProgressScreen(
                    progressManager = progressManager,
                    achievementManager = achievementManager,
                    onBack = { selectedScreen = AppScreen.MainMenu },
                    playSound = soundManager::playButtonClick
                )
            }
        }
    }

    override fun onDestroy() {
        soundManager.release()
        super.onDestroy()
    }
}

enum class AppScreen {
    MainMenu, Game, Challenges, Settings, Progress
}

