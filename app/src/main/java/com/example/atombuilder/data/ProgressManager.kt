package com.example.atombuilder.data

import android.content.Context
import android.content.SharedPreferences

class ProgressManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("progress_prefs", Context.MODE_PRIVATE)

    fun saveLevelResult(levelId: Int, time: Long, actions: Int, score: Int, atomName: String) {
        val currentBestTime = prefs.getLong("level_${levelId}_time", Long.MAX_VALUE)
        val currentMinActions = prefs.getInt("level_${levelId}_actions", Int.MAX_VALUE)
        val currentBestScore = prefs.getInt("level_${levelId}_score", 0)

        with(prefs.edit()) {
            putBoolean("level_${levelId}_completed", true)

            if (time < currentBestTime) {
                putLong("level_${levelId}_time", time)
            }

            if (actions < currentMinActions) {
                putInt("level_${levelId}_actions", actions)
            }

            if (score > currentBestScore) {
                putInt("level_${levelId}_score", score)
            }

            putString("level_${levelId}_name", atomName)

            apply()
        }
    }

    fun resetProgress() {
        prefs.edit().clear().apply()
    }

    private fun getLevelStats(levelId: Int): LevelStats {
        return LevelStats(
            bestTime = prefs.getLong("level_${levelId}_time", 0),
            minActions = prefs.getInt("level_${levelId}_actions", 0),
            bestScore = prefs.getInt("level_${levelId}_score", 0),
            completed = prefs.getBoolean("level_${levelId}_completed", false),
            atomName = prefs.getString("level_${levelId}_name", "Level $levelId") ?: "Level $levelId"
        )
    }

    fun getAllStats(): Map<Int, LevelStats> {
        return (1..118).associateWith { levelId -> getLevelStats(levelId) }
    }

    data class LevelStats(
        val bestTime: Long = 0,
        val minActions: Int = 0,
        val bestScore: Int = 0,
        val completed: Boolean = false,
        val atomName: String = ""
    )
}