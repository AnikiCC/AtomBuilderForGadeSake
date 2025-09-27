package com.example.atombuilder.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Button
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.ButtonDefaults
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun ActionRow(
    onAddOrbit: () -> Unit,
    onAddElectron: () -> Unit,
    onUndo: () -> Unit,
    onCheck: () -> Unit,
    onBack: () -> Unit,
    canAddElectron: Boolean,
    canUndo: Boolean,
    playSound: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PrimaryButton(
                text = "+ Орбита",
                onClick = onAddOrbit,
                modifier = Modifier.weight(1f)
            )

            PrimaryButton(
                text = "+ Электрон",
                onClick = onAddElectron,
                modifier = Modifier.weight(1f),
                enabled = canAddElectron
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PrimaryButton(
                text = "Отмена",
                onClick = onUndo,
                modifier = Modifier.weight(1f),
                enabled = canUndo
            )

            PrimaryButton(
                text = "Проверить",
                onClick = onCheck,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        PrimaryButton(
            text = "Назад",
            onClick = {
                playSound()
                onBack()
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .height(56.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (enabled) Color(0xFFFFEB3B) else Color.Gray,
            contentColor = Color.Black,
            disabledBackgroundColor = Color.Gray,
            disabledContentColor = Color.DarkGray
        ),
        elevation = ButtonDefaults.elevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp,
            disabledElevation = 0.dp
        )
    ) {
        Text(
            text = text,
            fontSize = 16.sp
        )
    }
}