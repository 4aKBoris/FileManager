package com.example.filemanager.ui.components.drawer.tabs

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.filemanager.RecyclerViewModel

@Composable
fun Tabs(viewModel: RecyclerViewModel) {

    val allScreens = Tab.values().toList()
    var currentScreen by rememberSaveable { mutableStateOf(Tab.Disks) }

    Scaffold(modifier = Modifier.fillMaxSize(),
    topBar = {
        TabRow(
            allScreens = allScreens,
            onTabSelected = { screen -> currentScreen = screen },
            currentScreen = currentScreen
        )
    }) {

    }
}

