package com.example.filemanager.ui.components.drawer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.filemanager.ui.components.drawer.tabs.FavoriteFiles
import com.example.filemanager.ui.components.drawer.tabs.LastFiles
import com.example.filemanager.ui.components.drawer.tabs.Tab
import com.example.filemanager.ui.components.drawer.tabs.TabRow
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