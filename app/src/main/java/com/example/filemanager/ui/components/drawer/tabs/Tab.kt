package com.example.filemanager.ui.components.drawer.tabs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Storage
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.filemanager.ui.components.drawer.tabs.disks.Disks

enum class Tab(
    val icon: ImageVector,
    val title: String,
    val body: @Composable ((String) -> Unit) -> Unit
) {
    Disks(
        icon = Icons.Filled.Storage,
        title = "Диски",
        body = { Disks() }
    ),
    FavoriteFiles(
        icon = Icons.Filled.Favorite,
        title = "Избранное",
        body = { com.example.filemanager.ui.components.drawer.tabs.favoritfiles.Favorites() }
    ),
    LastFiles(
        icon = Icons.Filled.AccessTime,
        title = "Последнее",
        body = { com.example.filemanager.ui.components.drawer.tabs.lastfiles.LastFiles() }
    );

    @Composable
    fun content(onScreenChange: (String) -> Unit) {
        body(onScreenChange)
    }

    companion object {
        fun fromRoute(route: String?): Tab =
            when (route?.substringBefore("/")) {
                Disks.name -> Disks
                FavoriteFiles.name -> FavoriteFiles
                LastFiles.name -> LastFiles
                null -> Disks
                else -> throw IllegalArgumentException("Route $route is not recognized.")
            }
    }
}