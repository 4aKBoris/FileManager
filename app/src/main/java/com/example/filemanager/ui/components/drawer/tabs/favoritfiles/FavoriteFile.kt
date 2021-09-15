package com.example.filemanager.ui.components.drawer.tabs.favoritfiles

import com.example.filemanager.item.FileTypes
import java.io.File

data class FavoriteFile(val path: String) {

    private val file: File = File(path)
    val fileSize: String = ""
    val name: String = file.name
    val imageVector: Int = types.get("")

    companion object {
        private val types = FileTypes()
    }
}
