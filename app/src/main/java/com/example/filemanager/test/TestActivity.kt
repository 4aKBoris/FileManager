package com.example.filemanager.test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Verified
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class TestActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MainContent()
        }
    }


    @Composable
    fun MainContent() {
        val isEnabled = remember { mutableStateOf(true) }
        val isRotated = remember { mutableStateOf(false) }

        val angle: Float by animateFloatAsState(
            targetValue = if (isRotated.value) 360F else 0F,
            animationSpec = tween(
                durationMillis = 2000, // duration
                easing = FastOutSlowInEasing
            ),
            finishedListener = {
                // disable the button
                isEnabled.value = true
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF0F8FF))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    isRotated.value = !isRotated.value
                    isEnabled.value = false
                },
                colors = ButtonDefaults.buttonColors(
                    Color(0xFF4B0082), Color(0xCCFFFFFF)
                ),
                enabled = isEnabled.value
            ) {
                Text(
                    text = "Rotate Icon",
                    modifier = Modifier.padding(12.dp)
                )
            }

            Icon(
                Icons.Filled.Verified,
                contentDescription = "Localized description",
                modifier = Modifier
                    .size(300.dp)
                    .rotate(angle)
                    .clickable {
                        isRotated.value = !isRotated.value
                        isEnabled.value = false
                    },
                tint = Color(0xFF5218FA),
            )
        }
    }


    @Preview
    @Composable
    fun ComposablePreview() {
        //MainContent()
    }
}