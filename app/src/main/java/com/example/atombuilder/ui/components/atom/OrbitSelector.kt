package com.example.atombuilder.ui.components.atom

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.example.atombuilder.data.Orbit

@Composable
fun OrbitSelector(
    orbits: List<Orbit>,
    selectedIndex: Int?,
    onSelect: (Int) -> Unit,
    playSound: () -> Unit

) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        NavigationButton(
            text = "<",
            onClick = {
                playSound()
                if (selectedIndex != null && selectedIndex > 0) {
                    onSelect(selectedIndex - 1)
                }
            },
            enabled = selectedIndex != null && selectedIndex > 0,
            modifier = Modifier.weight(1f)
        )

        NavigationButton(
            text = ">",
            onClick = {
                playSound()
                if (selectedIndex != null && selectedIndex < orbits.lastIndex) {
                    onSelect(selectedIndex + 1)
                }
            },
            enabled = selectedIndex != null && selectedIndex < orbits.lastIndex,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun NavigationButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.height(56.dp),
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
            fontSize = 20.sp,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
}