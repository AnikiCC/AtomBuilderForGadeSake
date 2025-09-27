package com.example.atombuilder.services

import android.content.Context
import android.content.SharedPreferences
import com.example.atombuilder.data.Achievement

class AchievementManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("achievement_prefs", Context.MODE_PRIVATE)

    private val predefinedAchievements = (5..118 step 5).toMutableList().apply {
        if (last() != 118) add(118)
    }.map { count ->
        Achievement(
            id = "combo_$count",
            title = "$count подряд",
            description = "Соберите $count атомов подряд без ошибок"
        )
    }


    fun getAll(): List<Achievement> {
        return predefinedAchievements.map {
            it.copy(unlocked = prefs.getBoolean(it.id, false))
        }
    }

    fun unlock(id: String) {
        prefs.edit().putBoolean(id, true).apply()
    }

    fun isUnlocked(id: String): Boolean {
        return prefs.getBoolean(id, false)
    }

    fun reset() {
        prefs.edit().clear().apply()
    }
}
