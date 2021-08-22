@file:Suppress("UNREACHABLE_CODE", "RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.example.filemanager.extensions

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.filemanager.R
import com.example.filemanager.Sort
import com.example.filemanager.constants.DIRECTORY
import com.example.filemanager.constants.FILE
import com.example.filemanager.item.FileItem
import java.io.File
import java.nio.file.Files
import java.nio.file.Files.*
import java.nio.file.attribute.BasicFileAttributes
import java.util.*
import kotlin.math.pow

fun List<FileItem>.sortByCondition(separation: Int, type: Int, upDown: Boolean) =
    Sort(this, separation, type, upDown).sort()

fun File.sizeString(): String {

    val rule = "%.2f"

    fun pow(i: Int) = 1000.0.pow(i.toDouble()).toInt()

    if (this.isDirectory) return "${this.listFiles().size} объектов"
    else {
        return when (val size = this.length()) {
            in 0..pow(1) -> "$size Байт"
            in pow(1)..pow(2) -> String.format(
                rule, size.toDouble() / pow(
                    1
                )
            ).plus(" КБ")
            in pow(2)..pow(3) -> String.format(
                rule, size.toDouble() / pow(
                    2
                )
            ).plus(" МБ")
            in pow(3)..pow(4) -> String.format(
                rule, size.toDouble() / pow(
                    3
                )
            )
                .plus(" ГБ")
            else -> String.format(rule, size.toDouble() / pow(4)).plus(" ТБ")
        }
    }
}

fun Array<File>.convertToFileItem() = this.map { FileItem(it) }