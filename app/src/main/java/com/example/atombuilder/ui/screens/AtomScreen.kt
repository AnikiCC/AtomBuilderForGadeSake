package com.example.atombuilder.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.atombuilder.data.Atom
import com.example.atombuilder.data.Electron
import com.example.atombuilder.data.Orbit
import com.example.atombuilder.services.JsonDatabase
import com.example.atombuilder.ui.components.ActionRow
import com.example.atombuilder.ui.components.atom.AtomView
import com.example.atombuilder.ui.components.atom.OrbitSelector
import com.example.atombuilder.ui.components.common.CustomAlertDialog

@Composable
fun AtomScreen(
    database: JsonDatabase,
    onBackToMenu: () -> Unit,
    onAtomBuilt: (Atom) -> Unit = {},
    showHeader: Boolean = true,
    isChallengeMode: Boolean = false,
    playSound: () -> Unit,
    onActionPerformed: () -> Unit,
    key: Int

) {
    var currentAtom by remember(key) { mutableStateOf(Atom("Custom", emptyList(), "")) }
    val history = remember(key) { mutableStateListOf<Atom>() }
    var selectedOrbitIndex by remember(key) { mutableStateOf<Int?>(null) }
    var dialogMessage by remember(key) { mutableStateOf<String?>(null) }

    fun updateAtom(newAtom: Atom) {
        playSound()
        history.add(currentAtom)
        currentAtom = newAtom
        if (selectedOrbitIndex != null && selectedOrbitIndex!! >= newAtom.orbits.size) {
            selectedOrbitIndex = newAtom.orbits.lastIndex
        }
    }

    fun undo() {
        if (history.isNotEmpty()) {
            playSound()
            onActionPerformed()
            val previousAtom = history.removeAt(history.size - 1)
            currentAtom = previousAtom
            if (currentAtom.orbits.isEmpty()) {
                selectedOrbitIndex = null
            } else if (selectedOrbitIndex != null && selectedOrbitIndex!! >= currentAtom.orbits.size) {
                selectedOrbitIndex = currentAtom.orbits.lastIndex
            }
        }
    }


    fun addOrbit() {
        if (currentAtom.orbits.size >= 8) return
        playSound()
        onActionPerformed()
        val newOrbit = Orbit(
            number = currentAtom.orbits.size + 1,
            radius = 50f + (currentAtom.orbits.size + 1) * 30f,
            electrons = emptyList()
        )
        val newOrbits = currentAtom.orbits + newOrbit
        updateAtom(currentAtom.copy(orbits = newOrbits))
        selectedOrbitIndex = newOrbits.size - 1
    }

    fun addElectron() {
        val orbits = currentAtom.orbits
        val orbitIndex = selectedOrbitIndex ?: return
        if (orbitIndex >= orbits.size) return
        val orbit = orbits[orbitIndex]
        if (orbit.electrons.size >= 32) return
        playSound()
        onActionPerformed()
        val newCount = orbit.electrons.size + 1
        val newElectrons = List(newCount) { i ->
            if (i < orbit.electrons.size) {
                orbit.electrons[i].copy(angle = i * (360f / newCount))
            } else {
                Electron(
                    id = i,
                    angle = i * (360f / newCount),
                    speed = 4f / (orbitIndex + 1)
                )
            }
        }
        val newOrbit = orbit.copy(electrons = newElectrons)
        val newOrbits = orbits.toMutableList().apply {
            set(orbitIndex, newOrbit)
        }
        updateAtom(currentAtom.copy(orbits = newOrbits))
    }

    fun checkAtom() {
        val constructed = currentAtom.orbits.map { it.electrons.size }
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

        val match = elements.firstNotNullOfOrNull { name ->
            database.getAtom(name)?.takeIf { dbAtom ->
                dbAtom.orbits.map { it.electrons.size } == constructed
            }
        }

        if (isChallengeMode) {
            onAtomBuilt(currentAtom)
        } else {
            dialogMessage = if (match != null) {
                "Атом: ${match.name}\nОписание: ${match.description}"
            } else {
                "Не соответствует ни одному известному элементу"
            }
        }
    }

    if (dialogMessage != null) {
        CustomAlertDialog(
            title = if (dialogMessage!!.startsWith("Атом")) "Вы собрали" else "Ошибка",
            message = dialogMessage!!,
            isSuccess = dialogMessage!!.startsWith("Атом"),
            onDismiss = { dialogMessage = null }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF333333))
            .padding(16.dp)
            .pointerInput(Unit) {
                detectHorizontalDragGestures { change, _ ->
                    if (change.position.x < 60.dp.toPx()) {
                        onBackToMenu()
                    }
                }
            }
    ) {
        if (showHeader) {
            Text(
                text = "Конструктор",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            AtomView(atom = currentAtom, selectedOrbitIndex = selectedOrbitIndex)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            OrbitSelector(
                orbits = currentAtom.orbits,
                selectedIndex = selectedOrbitIndex,
                onSelect = { selectedOrbitIndex = it },
                playSound
            )

            Spacer(modifier = Modifier.height(16.dp))

            ActionRow(
                onAddOrbit = ::addOrbit,
                onAddElectron = ::addElectron,
                onUndo = ::undo,
                onCheck = ::checkAtom,
                onBack = onBackToMenu,
                canAddElectron = selectedOrbitIndex != null && currentAtom.orbits.isNotEmpty(),
                canUndo = history.isNotEmpty(),
                playSound
            )
        }
    }
}