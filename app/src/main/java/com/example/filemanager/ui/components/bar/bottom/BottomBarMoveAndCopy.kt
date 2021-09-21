package com.example.filemanager.ui.components.bar.bottom

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun BottomBarMoveAndCopy(onCancelClick: () -> Unit, onPasteClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        ButtonItem(name = "Отмена", imageVector = Icons.Default.Cancel, onClick = onCancelClick)
        ButtonItem(name = "Вставить", imageVector = Icons.Default.ContentPaste, onClick = onPasteClick)
    }
}

@Composable
private fun ButtonItem(
    name: String,
    imageVector: ImageVector,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.clickable(onClick = onClick),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(imageVector = imageVector, contentDescription = name)
        Text(text = name)
    }
}