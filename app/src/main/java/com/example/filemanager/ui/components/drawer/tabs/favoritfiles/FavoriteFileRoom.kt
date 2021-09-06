package com.example.filemanager.ui.components.drawer.tabs.favoritfiles

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
class FavoriteFileRoom {
    @PrimaryKey
    var path: String = ""
}