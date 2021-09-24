package com.example.filemanager.sort

import androidx.annotation.StringRes
import com.example.filemanager.R
import com.example.filemanager.item.FileItem

enum class SortingOrder(@StringRes val idRes: Int) {
    DEFAULT(idRes = R.string.default_string) {
        override fun getComparator(comparator: Comparator<FileItem>): Comparator<FileItem> = comparator
    },
    DESCENDING(idRes = R.string.descending_order) {
        override fun getComparator(comparator: Comparator<FileItem>): Comparator<FileItem> = comparator.reversed()
    },
    ASCENDING(idRes = R.string.ascending_order) {
        override fun getComparator(comparator: Comparator<FileItem>): Comparator<FileItem> = comparator
    };

    abstract fun getComparator(comparator: Comparator<FileItem>):  Comparator<FileItem>
}