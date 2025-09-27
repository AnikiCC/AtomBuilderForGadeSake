package com.example.atombuilder.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.atombuilder.data.Achievement
import com.example.atombuilder.data.ProgressManager
import com.example.atombuilder.services.AchievementManager

@Composable
fun ProgressScreen(
    progressManager: ProgressManager,
    achievementManager: AchievementManager,
    onBack: () -> Unit,
    playSound: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var showResetConfirmation by remember { mutableStateOf(false) }
    var stats by remember { mutableStateOf(progressManager.getAllStats()) }
    var achievements by remember { mutableStateOf(achievementManager.getAll()) }


    if (showResetConfirmation) {
        AlertDialog(
            onDismissRequest = { showResetConfirmation = false },
            backgroundColor = Color(0xFFFFEB3B),
            shape = RoundedCornerShape(16.dp),
            title = {
                Text(
                    text = "Сброс прогресса",
                    color = Color.DarkGray,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "Вы уверены, что хотите сбросить весь прогресс?",
                    color = Color.DarkGray
                )
            },
            buttons = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            progressManager.resetProgress()
                            stats = progressManager.getAllStats()
                            achievements = achievementManager.getAll()
                            showResetConfirmation = false
                            playSound()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFFFFC000),
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Сбросить")
                    }

                    Button(
                        onClick = {
                            showResetConfirmation = false
                            playSound()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFFFFC000),
                            contentColor = Color.DarkGray
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Отмена")
                    }
                }
            }
        )
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
                    text = "Прогресс",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )

                Spacer(modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(32.dp))

            TabRow(
                selectedTabIndex = selectedTab,
                backgroundColor = Color(0xFFFFEB3B),
                contentColor = Color.DarkGray
            ) {
                listOf("Статистика", "Рекорды", "Достижения").forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = {
                            playSound()
                            selectedTab = index
                        },
                        text = { Text(title) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (selectedTab) {
                0 -> OverallStats(stats)
                1 -> RecordsTable(stats)
                2 -> AchievementsList(achievements)
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    playSound()
                    showResetConfirmation = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFFFFC000),
                    contentColor = Color.DarkGray
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Сбросить прогресс", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    playSound()
                    onBack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFFFFEB3B),
                    contentColor = Color.DarkGray
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Назад", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun OverallStats(stats: Map<Int, ProgressManager.LevelStats>) {
    val completedLevels = stats.count { it.value.completed }
    val totalScore = stats.values.sumOf { it.bestScore }
    val averageTime = stats.values.filter { it.completed }.let { completed ->
        if (completed.isNotEmpty()) completed.map { it.bestTime }.average().toLong() / 1000 else null
    }

    Column(modifier = Modifier.padding(16.dp)) {
        StatItem("Собрано атомов подряд", "$completedLevels/118")
        StatItem("Общий счёт", totalScore.toString())
        StatItem("Среднее время", averageTime?.let { "$it сек" } ?: "-")
    }
}

@Composable
private fun RecordsTable(stats: Map<Int, ProgressManager.LevelStats>) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(stats.toList().sortedBy { it.first }) { (levelId, levelStats) ->
            if (levelStats.completed) {
                LevelRecordItem(
                    atomName = levelStats.atomName,
                    time = levelStats.bestTime,
                    score = levelStats.bestScore
                )
            }
        }
    }
}

@Composable
private fun LevelRecordItem(atomName: String, time: Long, score: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color.DarkGray, RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Text("Атом: $atomName", color = Color.White, fontWeight = FontWeight.Bold)
        Text("Время: ${time / 1000} сек", color = Color.LightGray)
        Text("Очки: $score", color = Color.LightGray)
    }
}

@Composable
private fun StatItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 18.sp, color = Color.White)
        Text(value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
    }

    Divider(color = Color.Gray)
}

@Composable
private fun AchievementsList(achievements: List<Achievement>) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(achievements) { achievement ->
            val textColor = if (achievement.unlocked) Color.DarkGray else Color.White
            val backgroundColor = if (achievement.unlocked) Color(0xFFFFEB3B) else Color.DarkGray

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(9.dp)
                    .background(backgroundColor, RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Text(
                    text = achievement.title,
                    color = textColor,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = achievement.description,
                    color = textColor
                )
            }
        }
    }
}
