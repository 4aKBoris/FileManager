@file:Suppress("UNREACHABLE_CODE", "RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.example.filemanager.extensions

import android.content.res.Resources
import com.example.filemanager.sort.Sort
import com.example.filemanager.item.FileItem
import com.example.filemanager.sort.SortingOrder
import com.example.filemanager.sort.SortingType
import com.example.filemanager.sort.TypeOfGrouping
import java.io.File
import kotlin.math.pow

fun List<FileItem>.sortByCondition(typeOfGrouping: TypeOfGrouping, sortingType: SortingType, sortingOrder: SortingOrder) =
    Sort(this, typeOfGrouping, sortingType, sortingOrder).sort()

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

fun List<File>.convertToFileItem() = this.map { FileItem(it) }

fun Float.dp(): Float = this * density + 0.5f

val density: Float
    get() = Resources.getSystem().displayMetrics.density

fun Boolean.String(): String = if (this) "Да" else "Нет"