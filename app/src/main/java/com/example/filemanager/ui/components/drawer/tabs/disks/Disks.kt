package com.example.filemanager.ui.components.drawer.tabs.disks

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier


@Composable
fun Disks() {

    val scrollState = rememberLazyListState()

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(state = scrollState, modifier = Modifier.fillMaxSize()) {
        }
        Text(text = "Диски", modifier = Modifier.align(Alignment.Center))
    }
}