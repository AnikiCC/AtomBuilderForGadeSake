package com.example.atombuilder.data

data class Level(
    val id: Int,
    val atomToBuild: Atom,
    val timeLimit: Long
)