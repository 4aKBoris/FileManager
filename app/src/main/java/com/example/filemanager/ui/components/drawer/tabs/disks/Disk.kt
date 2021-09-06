package com.example.filemanager.ui.components.drawer.tabs.disks

import androidx.compose.ui.graphics.vector.ImageVector
import kotlin.math.pow

data class Disk(val name: String, val maxSize: Long, val size: Long, val imageVector: ImageVector) {
    val maxSizeDisk: String
        get() = maxSize.div(10.0.pow(9)).toString()

    val sizeDisk: String
        get() = size.div(10.0.pow(9)).toString()
}
