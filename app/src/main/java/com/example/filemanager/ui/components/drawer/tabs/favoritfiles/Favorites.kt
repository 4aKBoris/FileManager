package com.example.filemanager.ui.components.drawer.tabs.favoritfiles

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource

@Composable
fun Favorites() {
    Box(modifier = Modifier.fillMaxSize()) {

        val scrollState = rememberLazyListState()

        LazyColumn(modifier = Modifier.fillMaxSize()) {

        }

        Text(text = "Избранное", modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
private fun Item(item: FavoriteFile) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Row() {
            ImageView(painter = painterResource(id = item.imageVector))
            Column {
                MainText(name = item.name)
                SecondaryText(text = item.fileSize)
            }
            ImageButtonView {

            }
        }
    }
}

@Composable
private fun MainText(name: String) {
    Text(
        text = name,
        style = MaterialTheme.typography.subtitle2,
        color = MaterialTheme.colors.primaryVariant,
    )
}

@Composable
private fun SecondaryText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.body2,
        color = MaterialTheme.colors.primary,
    )
}

@Composable
private fun ImageButtonView(onClick: () -> Unit) {
    Box {
       IconButton(onClick = { onClick() }) {
           Icon(imageVector = Icons.Default.Delete, contentDescription = "Удалить из избранных")
       }
    }
}

@Composable
private fun ImageView(painter: Painter) {
    Box {
        Image(painter = painter, contentDescription = "Тип файла")
    }
}