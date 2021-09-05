package com.example.filemanager.ui.components.drawer

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.filemanager.RecyclerViewModel

@Composable
fun Drawer(
    viewModel: RecyclerViewModel,
    swapTheme: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .padding(vertical = 10.dp, horizontal = 15.dp)
                .align(Alignment.TopEnd)
        ) {
            IconToggleButton(
                checked = viewModel.theme,
                onCheckedChange = { swapTheme() }
            ) {
                Crossfade(
                    targetState = viewModel.theme,
                    animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
                ) {
                    Icon(
                        imageVector = if (it) Icons.Default.DarkMode else Icons.Default.LightMode,
                        contentDescription = "Смена темы"
                    )
                }
            }
        }
    }
}