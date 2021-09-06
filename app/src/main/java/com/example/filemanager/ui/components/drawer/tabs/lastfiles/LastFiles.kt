package com.example.filemanager.ui.components.drawer.tabs.lastfiles

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun LastFiles() {
    Box(modifier = Modifier.fillMaxSize()) {

        val scrollState = rememberLazyListState()

        LazyColumn(state = scrollState, modifier = Modifier.fillMaxSize()) {

        }

        Text(text = "Последнее", modifier = Modifier.align(Alignment.Center))
    }
}