package com.example.filemanager.item

import com.example.filemanager.R
import com.example.filemanager.constants.DIRECTORY
import com.example.filemanager.constants.FILE

class FileTypes {
    private val fileTypes = mapOf(
        FILE to R.drawable.file,
        /* "7z" to R.drawable.z7z,
         "avi" to R.drawable.avi,
         "doc" to R.drawable.doc,
         "docx" to R.drawable.doc,
         "exe" to R.drawable.exe,*/
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

    operator fun get(type: String): Int = if (keys.contains(type)) fileTypes[type]!! else fileTypes[FILE]!!

    val keys: Set<String>
        get() = fileTypes.keys
}