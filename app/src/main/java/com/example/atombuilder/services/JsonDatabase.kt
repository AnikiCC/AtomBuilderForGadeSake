package com.example.atombuilder.services

import android.content.Context
import com.example.atombuilder.data.Atom
import com.example.atombuilder.data.Electron
import com.example.atombuilder.data.Orbit
import org.json.JSONObject

class JsonDatabase(context: Context) {
    private val data: JSONObject

    init {
        val inputStream = context.assets.open("elements.json")
        val json = inputStream.bufferedReader().use { it.readText() }
        data = JSONObject(json)
    }

    fun getAtom(name: String): Atom? {
        val obj = data.optJSONObject(name) ?: return null
        val electrons = obj.getString("electrons_per_orbit").split(",").map { it.toInt() }
        val description = obj.getString("description")

        val orbits = electrons.mapIndexed { index, count ->
            Orbit(index + 1, 50f + (index + 1) * 30f, List(count) { Electron(it, it * (360f / count), 4f / (index + 1)) })
        }
        return Atom(name, orbits, description)
    }
}
