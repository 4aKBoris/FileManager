package com.example.filemanager.ui.components.drawer.tabs

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Storage
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.filemanager.R

enum class Tab(
    val icon: ImageVector,
    @StringRes val title: Int
) {
    Disks(
        icon = Icons.Filled.Storage,
        title = R.string.disks
    ),
    FavoriteFiles(
        icon = Icons.Filled.Favorite,
        title = R.string.favorite_files
    ),
    LastFiles(
        icon = Icons.Filled.AccessTime,
        title = R.string.last_files
    ),
    Settings(
        icon = Icons.Filled.Settings,
        title = R.string.settings
    );
}