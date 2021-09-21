package com.example.filemanager.ui.components.drawer

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.DrawerState
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
import com.example.filemanager.ui.components.drawer.tabs.*
import com.example.filemanager.ui.components.drawer.tabs.settings.Settings
import com.example.filemanager.view.model.FileManagerViewModel

@Composable
fun Drawer(
    viewModel: FileManagerViewModel,
    closeDrawer: () -> Unit
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
        }) { innerPadding ->
        Box(Modifier.padding(innerPadding)) {
            when(currentScreen) {
                Tab.Disks -> {}
                Tab.LastFiles -> LastFiles(viewModel = viewModel, closeDrawer = closeDrawer)
                Tab.FavoriteFiles -> FavoriteFiles(viewModel = viewModel, closeDrawer = closeDrawer)
                Tab.Settings -> Settings(viewModel = viewModel)
            }
        }
    }
}