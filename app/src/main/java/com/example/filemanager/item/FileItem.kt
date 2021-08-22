package com.example.filemanager.item

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.filemanager.R
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
    val type = fileTypes[if (isDirectory) DIRECTORY else if (!fileName.contains(
            '.'
        ) || fileName.takeLastWhile { t -> t != '.' } !in fileTypes.keys
    ) FILE else fileName.takeLastWhile { t -> t != '.' }]!!
    val size = file.sizeString()
    val fileSize = file.length()
    @RequiresApi(Build.VERSION_CODES.O)
    val dateCreate = Date(attr.creationTime().toMillis())
    @RequiresApi(Build.VERSION_CODES.O)
    val dateChange = Date(attr.lastModifiedTime().toMillis())
    @RequiresApi(Build.VERSION_CODES.O)
    val dateAccess = Date(attr.lastAccessTime().toMillis())

    override fun toString(): String = fileName

    companion object {
        val fileTypes = mapOf(
            /* "7z" to R.drawable.z7z,
             "avi" to R.drawable.avi,
             "doc" to R.drawable.doc,
             "docx" to R.drawable.doc,
             "exe" to R.drawable.exe,*/
            FILE to R.drawable.file,
            /*"gif" to R.drawable.gif,
            "jpg" to R.drawable.jpg,
            "mp3" to R.drawable.mp3,
            "mp4" to R.drawable.mp4,*/
            "pdf" to R.drawable.pdf,
            /*"png" to R.drawable.png,
            "ppt" to R.drawable.ppt,
            "pptx" to R.drawable.ppt,
            "rar" to R.drawable.rar,
            "txt" to R.drawable.txt,
            "wav" to R.drawable.wav,
            "xls" to R.drawable.xls,
            "xlsx" to R.drawable.xls,
            "zip" to R.drawable.zip,*/
            DIRECTORY to R.drawable.ic_folder
        )
    }
}