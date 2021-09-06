package com.example.filemanager.ui.components.recyclerview

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ActionsRow(
    size: Dp,
    favoriteFile: Boolean,
    onInfo: () -> Unit,
    onFavorite: () -> Unit,
    onEdit: () -> Unit,
) {
    Row(
        Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            modifier = Modifier.size(size = size),
            imageVector = Icons.Default.Info,
            description = "Информация о файле",
            color = Color.Gray,
            onClick = onInfo
        )

        val scale by animateFloatAsState(
            targetValue = if (favoriteFile) 1f else 0.999f,
            repeatable(
                iterations = 6,
                repeatMode = RepeatMode.Reverse,
                animation = keyframes {
                    durationMillis = 150
                    1f at 0 with LinearOutSlowInEasing
                    2f at 150 with LinearOutSlowInEasing
                }
            )
        )

        Crossfade(
            targetState = favoriteFile,
            animationSpec = tween(durationMillis = 900, easing = LinearEasing)
        ) {
            Button(
                modifier = Modifier
                    .size(size = size)
                    .scale(scale = scale),
                imageVector = if (it) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                description = "Избранное",
                color = Color.Red,
                onClick = onFavorite
            )
        }

        Button(
            modifier = Modifier.size(size = size),
            imageVector = Icons.Default.Edit,
            description = "Редактировать название",
            color = Color.Gray,
            onClick = onEdit
        )
    }
}

@Composable
private fun Button(
    modifier: Modifier,
    imageVector: ImageVector,
    description: String,
    color: Color,
    onClick: () -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = onClick,
        content = {
            Icon(
                imageVector = imageVector,
                tint = color,
                contentDescription = description,
            )
        }
    )
}