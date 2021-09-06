package com.example.filemanager.ui.components.drawer

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.filemanager.RecyclerViewModel
import com.example.filemanager.ui.components.drawer.tabs.Tab
import com.example.filemanager.ui.components.drawer.tabs.TabRow
import com.example.filemanager.ui.components.drawer.tabs.Tabs

@Composable
fun Drawer(
    viewModel: RecyclerViewModel,
    swapTheme: () -> Unit
) {

    val allScreens = Tab.values().toList()
    var currentScreen by rememberSaveable { mutableStateOf(Tab.Disks) }

    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            TabRow(
                allScreens = allScreens,
                onTabSelected = { screen -> currentScreen = screen },
                currentScreen = currentScreen
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier.fillMaxWidth().height(60.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                IconToggleButton(
                    checked = viewModel.theme,
                    onCheckedChange = { swapTheme() },
                    modifier = Modifier.padding(end = 15.dp)
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
        }) { innerPadding ->
        Box(Modifier.padding(innerPadding)) {
            currentScreen.content(
                onScreenChange = { screen ->
                    currentScreen = Tab.valueOf(screen)
                }
            )
        }
    }
}