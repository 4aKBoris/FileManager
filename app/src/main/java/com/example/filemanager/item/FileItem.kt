package com.example.filemanager.item

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.filemanager.FileTypes
import com.example.filemanager.constants.DIRECTORY
import com.example.filemanager.constants.FILE
import com.example.filemanager.extensions.sizeString
import java.io.File
import java.nio.file.Files
import java.nio.file.attribute.BasicFileAttributes
import java.util.*

data class FileItem(private val file: File) {

    @RequiresApi(Build.VERSION_CODES.O)
    private val attr = Files.readAttributes(file.toPath(), BasicFileAttributes::class.java)

    val isDirectory = file.isDirectory
    val fileName: String = file.name
    val type =
        fileTypes.get(if (isDirectory) DIRECTORY else if (!fileName.contains('.') || fileName.takeLastWhile { t -> t != '.' } !in fileTypes.keys) FILE else fileName.takeLastWhile { t -> t != '.' })
    val size = file.sizeString()
    val fileSize = file.length()
    val path = file.absolutePath

    @RequiresApi(Build.VERSION_CODES.O)
    val dateCreate = Date(attr.creationTime().toMillis())

    @RequiresApi(Build.VERSION_CODES.O)
    val dateChange = Date(attr.lastModifiedTime().toMillis())

    @RequiresApi(Build.VERSION_CODES.O)
    val dateAccess = Date(attr.lastAccessTime().toMillis())

    override fun toString(): String = fileName

    companion object {
        val fileTypes = FileTypes()
    }
}