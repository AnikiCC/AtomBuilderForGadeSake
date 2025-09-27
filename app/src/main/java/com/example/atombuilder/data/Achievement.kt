package com.example.atombuilder.data

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val unlocked: Boolean = false
)
