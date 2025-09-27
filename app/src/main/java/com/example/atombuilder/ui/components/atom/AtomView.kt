package com.example.atombuilder.ui.components.atom

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.atombuilder.data.Atom
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun AtomView(atom: Atom, selectedOrbitIndex: Int?) {
    val scope = rememberCoroutineScope()
    val orbitAngles = remember(atom) {
        atom.orbits.map { Animatable(0f) }
    }

    LaunchedEffect(atom) {
        while (true) {
            atom.orbits.forEachIndexed { index, orbit ->
                scope.launch {
                    val speed = orbit.electrons.firstOrNull()?.speed ?: 1f
                    orbitAngles[index].animateTo(
                        orbitAngles[index].value + speed,
                        animationSpec = tween(60)
                    )
                }
            }
            kotlinx.coroutines.delay(16L)
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val center = Offset(size.width / 2, size.height / 2)
        val scaleFactor = 1.5f

        val nucleusRadius = 60f * scaleFactor

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFFFFE39C),
                    Color(0xFFFFD700),
                    Color(0xFFF4C20D),
                    Color(0xFFD48E00)
                ),
                center = center,
                radius = nucleusRadius
            ),
            radius = nucleusRadius,
            center = center
        )

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color.White.copy(alpha = 0.3f),
                    Color.Transparent
                ),
                center = Offset(center.x - 10f, center.y - 10f),
                radius = nucleusRadius * 0.6f
            ),
            radius = nucleusRadius * 0.6f,
            center = Offset(center.x - 10f, center.y - 10f)
        )

        atom.orbits.forEachIndexed { orbitIndex, orbit ->
            val adjustedRadius = (orbit.radius + 25f) * scaleFactor

            val orbitColor = if (orbitIndex == selectedOrbitIndex) {
                Color.White
            } else {
                Color.White.copy(alpha = 0.7f)
            }

            val strokeWidth = if (orbitIndex == selectedOrbitIndex) 4f else 3f

            drawCircle(
                color = orbitColor,
                radius = adjustedRadius,
                center = center,
                style = if (orbitIndex == selectedOrbitIndex) {
                    Stroke(width = strokeWidth)
                } else {
                    Stroke(
                        width = strokeWidth,
                        pathEffect = PathEffect.dashPathEffect(
                            floatArrayOf(15f, 10f),
                            0f
                        )
                    )
                }
            )

            orbit.electrons.forEachIndexed { electronIndex, _ ->
                val angle = orbitAngles[orbitIndex].value + (electronIndex * (360f / orbit.electrons.size))
                val x = center.x + adjustedRadius * cos(Math.toRadians(angle.toDouble()).toFloat())
                val y = center.y + adjustedRadius * sin(Math.toRadians(angle.toDouble()).toFloat())

                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color.Yellow.copy(alpha = 1f),
                            Color(0xFFFFD700),
                            Color(0xFFF4C20D),
                            Color(0xFFD48E00)
                        ),
                        center = Offset(x, y),
                        radius = 15f * scaleFactor
                    ),
                    radius = 15f * scaleFactor,
                    center = Offset(x, y)
                )
            }
        }
    }
}