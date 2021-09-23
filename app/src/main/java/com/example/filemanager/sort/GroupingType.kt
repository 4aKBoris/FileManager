package com.example.filemanager.sort

import androidx.annotation.StringRes
import com.example.filemanager.R
import com.example.filemanager.item.FileItem

enum class GroupingType(@StringRes val idRes: Int, val comparator: Comparator<FileItem>) {
    DEFAULT(idRes = R.string.default_string, comparator = compareBy { null }),
    FOLDERS(idRes = R.string.folders_on_top, comparator = compareBy { it.isFile }),
    FILES(idRes = R.string.files_on_top, comparator = compareBy { it.isDirectory })
}