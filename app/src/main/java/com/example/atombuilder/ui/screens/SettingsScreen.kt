package com.example.atombuilder.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsScreen(
    onBackToMenu: () -> Unit,
    musicVolume: Float,
    soundEffectsVolume: Float,
    onMusicVolumeChange: (Float) -> Unit,
    onSoundEffectsVolumeChange: (Float) -> Unit,
    playSound: () -> Unit
) {
    var currentMusicVol by remember { mutableFloatStateOf(musicVolume) }
    var currentSoundVol by remember { mutableFloatStateOf(soundEffectsVolume) }

    LaunchedEffect(musicVolume, soundEffectsVolume) {
        currentMusicVol = musicVolume
        currentSoundVol = soundEffectsVolume
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF333333))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "Настройки",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )

                Spacer(modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Громкость музыки",
                fontSize = 24.sp,
                color = Color.White,
                modifier = Modifier.padding(start = 16.dp)
            )
            Slider(
                value = currentMusicVol,
                onValueChange = { newValue ->
                    currentMusicVol = newValue
                    onMusicVolumeChange(newValue)
                },
                valueRange = 0f..1f,
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFFFFEB3B),
                    activeTrackColor = Color(0xFFFFEB3B),
                    inactiveTrackColor = Color.Gray
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Громкость звуковых эффектов",
                fontSize = 24.sp,
                color = Color.White,
                modifier = Modifier.padding(start = 16.dp)
            )
            Slider(
                value = currentSoundVol,
                onValueChange = { newValue ->
                    currentSoundVol = newValue
                    onSoundEffectsVolumeChange(newValue)
                },
                valueRange = 0f..1f,
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFFFFEB3B),
                    activeTrackColor = Color(0xFFFFEB3B),
                    inactiveTrackColor = Color.Gray
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    playSound()
                    onBackToMenu()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFFFFEB3B),
                    contentColor = Color.DarkGray
                ),
            ) {
                Text(
                    text = "Назад",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}