@file:Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.example.filemanager.item

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import com.example.filemanager.R
import java.io.File
import java.nio.file.Files
import java.nio.file.attribute.BasicFileAttributes
import java.text.SimpleDateFormat
import java.util.*

data class FileItem(val file: File, private val resources: Resources) {

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

    val expansion: FileTypes
        get() = getType(file = file)

    override fun toString(): String = path

    private fun File.sizeString() =
        if (this.isDirectory) "${this.listFiles().size} ${resources.getString(R.string.objects)}"
        else {
            var k = this.length()
            var l = 0
            while (k >= 1000) {
                k /= 1000
                l++
            }
            "$k ${resources.getString(UnitsOfMeasurement.values()[l].idRes)}"
        }

    companion object {
        @SuppressLint("WeekBasedYear")
        private val formatter = SimpleDateFormat("dd.MM.YYYY", Locale.ENGLISH)
        private val formatterInfo = SimpleDateFormat("HH:mm:ss dd-MM-yyyy", Locale.ENGLISH)

        enum class UnitsOfMeasurement(@StringRes val idRes: Int) {
            BYTE(R.string.one_byte),
            KILOBYTE(R.string.kilobyte),
            MEGABYTE(R.string.megabyte),
            GIGABYTE(R.string.gigabyte);
        }
    }
}