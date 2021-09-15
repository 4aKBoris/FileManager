package com.example.filemanager.item

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.filemanager.constants.DIRECTORY
import com.example.filemanager.constants.FILE
import com.example.filemanager.extensions.sizeString
import java.io.File
import java.nio.file.Files
import java.nio.file.attribute.BasicFileAttributes
import java.text.SimpleDateFormat
import java.util.*

data class FileItem(
    val file: File,
    val oldPath: String
) {

    @RequiresApi(Build.VERSION_CODES.O)
    private val attr = Files.readAttributes(file.toPath(), BasicFileAttributes::class.java)

    val name: String
        get() = file.name

    val type: FolderOrFile
    get() = if (isDirectory) FolderOrFile.Folder else FolderOrFile.File

    val path: String
        get() = file.absolutePath

    val isDirectory: Boolean
        get() = file.isDirectory

    val isFile: Boolean
        get() = file.isFile

    val size: Long
        get() = file.length()

    val stringSize: String
        get() = file.sizeString()

    val dateCreate: Date
        @RequiresApi(Build.VERSION_CODES.O)
        get() = Date(attr.creationTime().toMillis())
    val dateChange: Date
        @RequiresApi(Build.VERSION_CODES.O)
        get() = Date(attr.lastModifiedTime().toMillis())
    val dateAccess: Date
        @RequiresApi(Build.VERSION_CODES.O)
        get() = Date(attr.lastAccessTime().toMillis())

    val partialDateChange: String
        get() = formatter.format(dateChange)

    val fullDateCreate: String
        get() = formatterInfo.format(dateCreate)

    val fullDateChange: String
        get() = formatterInfo.format(dateChange)

    val fullDateAccess: String
        get() = formatterInfo.format(dateAccess)

    val expansion: Int
        get() =
            types[if (isDirectory) DIRECTORY else if (!name.contains(
                    '.'
                ) || name.takeLastWhile { t -> t != '.' } !in types.keys
            ) FILE else name.takeLastWhile { t -> t != '.' }]

    override fun toString(): String = "path = $path; name = $name"

    companion object {
        @SuppressLint("WeekBasedYear")
        private val formatter = SimpleDateFormat("dd.MM.YYYY", Locale.ENGLISH)
        private val formatterInfo = SimpleDateFormat("HH:mm:ss dd-MM-yyyy", Locale.ENGLISH)

        private val types = FileTypes()
    }

}