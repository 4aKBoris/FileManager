@file:Suppress("UNREACHABLE_CODE", "RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.example.filemanager.extensions

import android.content.res.Resources
import java.io.File

fun String.toFile() = File(this)

fun Float.dp(): Float = this * density + 0.5f

val density: Float
    get() = Resources.getSystem().displayMetrics.density