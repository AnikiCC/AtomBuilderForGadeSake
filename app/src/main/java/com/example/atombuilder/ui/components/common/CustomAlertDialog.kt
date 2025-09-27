package com.example.atombuilder.ui.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.AlertDialog
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Button
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.ButtonDefaults
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomAlertDialog(
    title: String,
    message: String,
    isSuccess: Boolean,
    onDismiss: () -> Unit
) {
    val backgroundColor = if (isSuccess) listOf(Color(0xFFFFEB3B), Color(0xFFFFEB3B))
    else listOf(Color(0xFFFF9600), Color(0xFFFF9600))

    AlertDialog(
        onDismissRequest = onDismiss,
        backgroundColor = Color.Transparent,
        buttons = {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .padding(8.dp)
                    .background(
                        Brush.verticalGradient(backgroundColor),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = title,
                        color = Color.DarkGray,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = message,
                        color = Color.DarkGray,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = if (isSuccess) Color(0xFFFFC000) else Color(0xFFFF7800),
                            contentColor = Color.DarkGray
                        ),
                        shape = RoundedCornerShape(8.dp),
                        elevation = ButtonDefaults.elevation(
                            defaultElevation = 4.dp,
                            pressedElevation = 2.dp
                        ),
                        modifier = Modifier
                            .padding(top = 8.dp)
                    ) {
                        Text(
                            "OK",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    )
}