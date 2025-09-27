package com.example.atombuilder.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.atombuilder.ui.components.common.StyledButton
import com.example.atombuilder.ui.components.common.GradientText

@Composable
fun MainMenu(
    onStartGame: () -> Unit,
    onStartChallenges: () -> Unit,
    onSettings: () -> Unit,
    onExit: () -> Unit,
    onProgress: () -> Unit,
    playSound: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF333333))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GradientText(
                text = "Atom Builder",
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold,
                gradient = Brush.horizontalGradient(
                    listOf(Color(0xFFFFEB3B), Color(0xFFFFEB3B))
                )
            )
            Spacer(modifier = Modifier.height(32.dp))
            StyledButton("Конструктор", onStartGame, playSound)
            Spacer(modifier = Modifier.height(16.dp))
            StyledButton("Испытания", onStartChallenges, playSound)
            Spacer(modifier = Modifier.height(16.dp))
            StyledButton("Прогресс", onProgress, playSound)
            Spacer(modifier = Modifier.height(16.dp))
            StyledButton("Настройки", onSettings, playSound)
            Spacer(modifier = Modifier.height(16.dp))
            StyledButton("Выход", onExit, playSound)
        }
    }
}
