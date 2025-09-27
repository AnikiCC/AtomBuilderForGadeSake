package com.example.atombuilder.ui.screens

import androidx.compose.foundation.layout.*
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import androidx.compose.foundation.background
import androidx.compose.ui.unit.sp
import com.example.atombuilder.ui.components.common.TimerBox
import com.example.atombuilder.services.JsonDatabase
import com.example.atombuilder.data.ProgressManager
import com.example.atombuilder.services.AchievementManager
import com.example.atombuilder.ui.components.common.CustomAlertDialog

@Composable
fun ChallengesScreen(
    onBackToMenu: () -> Unit,
    database: JsonDatabase,
    progressManager: ProgressManager,
    achievementManager: AchievementManager,
    playSound: () -> Unit,
    playSuccess: () -> Unit,
    playFailure: () -> Unit
) {
    val availableAtoms = remember {
        val elements = listOf(
            "Hydrogen", "Helium", "Lithium", "Beryllium", "Boron",
            "Carbon", "Nitrogen", "Oxygen", "Fluorine", "Neon",
            "Sodium", "Magnesium", "Aluminium", "Silicon", "Phosphorus",
            "Sulfur", "Chlorine", "Argon", "Potassium", "Calcium",
            "Scandium", "Titanium", "Vanadium", "Chromium", "Manganese",
            "Iron", "Cobalt", "Nickel", "Copper", "Zinc",
            "Gallium", "Germanium", "Arsenic", "Selenium", "Bromine",
            "Krypton", "Rubidium", "Strontium", "Yttrium", "Zirconium",
            "Niobium", "Molybdenum", "Technetium", "Ruthenium", "Rhodium",
            "Palladium", "Silver", "Cadmium", "Indium", "Tin",
            "Antimony", "Tellurium", "Iodine", "Xenon", "Cesium",
            "Barium", "Lanthanum", "Cerium", "Praseodymium", "Neodymium",
            "Promethium", "Samarium", "Europium", "Gadolinium", "Terbium",
            "Dysprosium", "Holmium", "Erbium", "Thulium", "Ytterbium",
            "Lutetium", "Hafnium", "Tantalum", "Tungsten", "Rhenium",
            "Osmium", "Iridium", "Platinum", "Gold", "Mercury",
            "Thallium", "Lead", "Bismuth", "Polonium", "Astatine",
            "Radon", "Francium", "Radium", "Actinium", "Thorium",
            "Protactinium", "Uranium", "Neptunium", "Plutonium", "Americium",
            "Curium", "Berkelium", "Californium", "Einsteinium", "Fermium",
            "Mendelevium", "Nobelium", "Lawrencium", "Rutherfordium", "Dubnium",
            "Seaborgium", "Bohrium", "Hassium", "Meitnerium", "Darmstadtium",
            "Roentgenium", "Copernicium", "Nihonium", "Flerovium", "Moscovium",
            "Livermorium", "Tennessine", "Oganesson"
        )
        elements.mapNotNull { database.getAtom(it) }.shuffled()
    }

    var currentLevelIndex by remember { mutableIntStateOf(0) }
    val defaultTime = 180000L
    var timeLeft by remember { mutableLongStateOf(defaultTime) }
    var isLevelCompleted by remember { mutableStateOf(false) }
    var isGameOver by remember { mutableStateOf(false) }
    var isWrongAtom by remember { mutableStateOf(false) }
    var actionsCount by remember { mutableIntStateOf(0) }
    var timerActive by remember { mutableStateOf(true) }
    var showCompletionDialog by remember { mutableStateOf(false) }
    val hasMoreLevels = currentLevelIndex < availableAtoms.size
    val currentAtom = availableAtoms.getOrNull(currentLevelIndex)


    fun calculateScore(): Int {
        val timeBonus = (timeLeft / 1000).toInt()
        val actionsPenalty = actionsCount * 5
        return maxOf(100 + timeBonus - actionsPenalty, 10)
    }

    fun onLevelCompleted() {
        timerActive = false
        currentAtom?.let { atom ->
            progressManager.saveLevelResult(
                levelId = currentLevelIndex + 1,
                time = defaultTime - timeLeft,
                actions = actionsCount,
                score = calculateScore(),
                atomName = atom.name
            )
            val completed = progressManager.getAllStats().count { it.value.completed }

            val thresholds = (5..118 step 5).toMutableList().apply {
                if (last() != 118) add(118)
            }

            thresholds.forEach { count ->
                if (completed >= count && !achievementManager.isUnlocked("combo_$count")) {
                    achievementManager.unlock("combo_$count")
                }
            }

        }
        playSuccess()
        isLevelCompleted = true

        if (currentLevelIndex >= availableAtoms.size - 1) {
            showCompletionDialog = true
            isLevelCompleted = false
        }
    }

    fun restartLevel() {
        playSound()
        actionsCount = 0
        timeLeft = defaultTime
        isLevelCompleted = false
        isGameOver = false
        isWrongAtom = false
        timerActive = true
    }

    fun nextLevel() {
        playSound()
        actionsCount = 0
        currentLevelIndex++
        timeLeft = defaultTime
        isLevelCompleted = false
        isGameOver = false
        isWrongAtom = false
        timerActive = true
    }

    LaunchedEffect(currentLevelIndex, timerActive) {
        while (timeLeft > 0 && timerActive && !isLevelCompleted && !isWrongAtom && hasMoreLevels) {
            delay(1000L)
            timeLeft -= 1000
        }

        if (timeLeft <= 0 && !isLevelCompleted && hasMoreLevels) {
            timerActive = false
            isGameOver = true
            playFailure()
        }
    }
    val onActionPerformed: () -> Unit = {
        playSound()
        actionsCount++
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF333333))
            .padding(16.dp)
    ) {
        if (hasMoreLevels) {
            TimerBox(
                timeLeft = timeLeft,
                totalTime = defaultTime,
                atomName = currentAtom?.name ?: ""
            )

            Spacer(modifier = Modifier.height(16.dp))

            currentAtom?.let { atom ->
                AtomScreen(
                    database = database,
                    onBackToMenu = onBackToMenu,
                    onAtomBuilt = { builtAtom ->
                        if (builtAtom.orbits == atom.orbits) {
                            onLevelCompleted()
                        } else {
                            timerActive = false
                            isWrongAtom = true
                            playFailure()
                        }
                    },
                    showHeader = false,
                    isChallengeMode = true,
                    playSound = {
                        playSound()
                    },
                    onActionPerformed = onActionPerformed,
                    key = currentLevelIndex
                )
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Все уникальные атомы пройдены!",
                    color = Color.White,
                    fontSize = 24.sp
                )
            }
        }

        // Диалоги
        when {
            showCompletionDialog -> {
                CustomAlertDialog(
                    title = "🏆 Забег завершен!",
                    message = "Вы собрали все уникальные атомы!",
                    isSuccess = true,
                    onDismiss = { onBackToMenu() }
                )
            }

            isLevelCompleted -> {
                CustomAlertDialog(
                    title = "🏆 Уровень ${currentLevelIndex + 1} завершен!",
                    message = "Счет: ${calculateScore()}\nВремя: ${(defaultTime - timeLeft) / 1000} сек\nДействий: $actionsCount",
                    isSuccess = true,
                    onDismiss = {
                        if (hasMoreLevels) {
                            nextLevel()
                        }
                    }
                )
            }

            isGameOver -> {
                CustomAlertDialog(
                    title = "⏳ Время вышло!",
                    message = "Вы не успели собрать атом ${currentAtom?.name}",
                    isSuccess = false,
                    onDismiss = { restartLevel() }
                )
            }

            isWrongAtom -> {
                CustomAlertDialog(
                    title = "Ошибка!",
                    message = "Вы собрали неправильный атом",
                    isSuccess = false,
                    onDismiss = { restartLevel() }
                )
            }
        }
    }
}