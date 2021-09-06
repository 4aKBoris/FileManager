package com.example.filemanager.room

import androidx.room.*
import com.example.filemanager.ui.components.drawer.tabs.favoritfiles.FavoriteFileRoom


@Dao
interface FavoriteFileInterface {
    @get:Query("SELECT * FROM favoritefileroom")
    val all: List<Any?>?

    @Insert
    fun insert(file: FavoriteFileRoom)

    @Delete
    fun delete(file: FavoriteFileRoom)
}