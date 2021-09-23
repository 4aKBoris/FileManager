package com.example.filemanager.sort

import androidx.annotation.StringRes
import com.example.filemanager.R
import com.example.filemanager.item.FileItem

enum class SortingType(@StringRes val idRes: Int, val comparator: Comparator<FileItem>) {
    DEFAULT(idRes = R.string.default_string, comparator = compareBy { null }),
    ALPHABET(idRes = R.string.sorting_type_alphabetically, comparator = compareBy { it.name }),
    SIZE(idRes = R.string.sorting_type_by_size, comparator = compareBy { it.size }),
    DATE_CREATE(
        idRes = R.string.sorting_type_by_creation_date,
        comparator = compareBy { it.dateCreate }),
    DATE_CHANGE(
        idRes = R.string.sorting_type_by_date_of_change,
        comparator = compareBy { it.dateChange }),
    DATE_RECENT_DISCOVERIES(
        idRes = R.string.sorting_type_by_opening_date,
        comparator = compareBy { it.dateAccess })
}