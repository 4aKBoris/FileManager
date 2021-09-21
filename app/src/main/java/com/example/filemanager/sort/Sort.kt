package com.example.filemanager.sort

import androidx.annotation.StringRes
import com.example.filemanager.R
import com.example.filemanager.item.FileItem

enum class GroupingType(@StringRes val id: Int) {
    DEFAULT(R.string.default_string), FOLDERS(R.string.folders_on_top), FILES(R.string.files_on_top)
}

enum class SortingOrder(@StringRes val id: Int) {
    DEFAULT(R.string.default_string), DESCENDING(R.string.descending_order), ASCENDING(R.string.ascending_order)
}

enum class SortingType(@StringRes val id: Int) {
    DEFAULT(R.string.default_string),
    ALPHABET(R.string.sorting_type_alphabetically),
    SIZE(R.string.sorting_type_by_size),
    DATE_CREATE(R.string.sorting_type_by_creation_date),
    DATE_CHANGE(R.string.sorting_type_by_date_of_change),
    DATE_RECENT_DISCOVERIES(R.string.sorting_type_by_opening_date)
}

class Sort(
    private val files: List<FileItem>,
    private val groupingType: GroupingType,
    private val sortingType: SortingType,
    private val sortingOrder: SortingOrder
) {

    fun sort() = when (groupingType) {
        GroupingType.DEFAULT -> when (sortingType) {
            SortingType.ALPHABET -> sortFileName(files, sortingOrder)
            SortingType.SIZE -> sortFileSize(files, sortingOrder)
            SortingType.DATE_CREATE -> sortDateCreate(files, sortingOrder)
            SortingType.DATE_CHANGE -> sortDateChange(files, sortingOrder)
            SortingType.DATE_RECENT_DISCOVERIES -> sortDateAccess(files, sortingOrder)
            else -> files
        }
        GroupingType.FILES -> {
            val directories = files.filter { it.isDirectory }
            val file = files.filter { it.isFile }
            when (sortingType) {
                SortingType.ALPHABET -> sortFileName(file, sortingOrder).plus(
                    sortFileName(
                        directories,
                        sortingOrder
                    )
                )
                SortingType.SIZE -> sortFileSize(file, sortingOrder).plus(
                    sortFileSize(
                        directories,
                        sortingOrder
                    )
                )
                SortingType.DATE_CREATE -> sortDateCreate(file, sortingOrder).plus(
                    sortDateCreate(
                        directories,
                        sortingOrder
                    )
                )
                SortingType.DATE_CHANGE -> sortDateChange(file, sortingOrder).plus(
                    sortDateChange(
                        directories,
                        sortingOrder
                    )
                )
                SortingType.DATE_RECENT_DISCOVERIES -> sortDateCreate(file, sortingOrder).plus(
                    sortDateAccess(
                        directories,
                        sortingOrder
                    )
                )
                else -> files
            }
        }
        GroupingType.FOLDERS -> {
            val directories = files.filter { it.isDirectory }
            val file = files.filter { it.isFile }
            when (sortingType) {
                SortingType.ALPHABET -> sortFileName(directories, sortingOrder).plus(
                    sortFileName(
                        file,
                        sortingOrder
                    )
                )
                SortingType.SIZE -> sortFileSize(directories, sortingOrder).plus(
                    sortFileSize(
                        file,
                        sortingOrder
                    )
                )
                SortingType.DATE_CREATE -> sortDateCreate(directories, sortingOrder).plus(
                    sortDateCreate(
                        file,
                        sortingOrder
                    )
                )
                SortingType.DATE_CHANGE -> sortDateChange(directories, sortingOrder).plus(
                    sortDateChange(
                        file,
                        sortingOrder
                    )
                )
                SortingType.DATE_RECENT_DISCOVERIES -> sortDateCreate(directories, sortingOrder).plus(
                    sortDateAccess(
                        file,
                        sortingOrder
                    )
                )
                else -> files
            }
        }
    }

    private fun sortFileName(files: List<FileItem>, sortingOrder: SortingOrder) =
        if (sortingOrder == SortingOrder.DESCENDING) files.sortedByDescending { it.name } else files.sortedBy { it.name }

    private fun sortFileSize(files: List<FileItem>, sortingOrder: SortingOrder) =
        if (sortingOrder == SortingOrder.DESCENDING) files.sortedByDescending { it.size } else files.sortedBy { it.size }

    private fun sortDateCreate(files: List<FileItem>, sortingOrder: SortingOrder) =
        if (sortingOrder == SortingOrder.DESCENDING) files.sortedByDescending { it.dateCreate } else files.sortedBy { it.dateCreate }

    private fun sortDateChange(files: List<FileItem>, sortingOrder: SortingOrder) =
        if (sortingOrder == SortingOrder.DESCENDING) files.sortedByDescending { it.dateChange } else files.sortedBy { it.dateChange }

    private fun sortDateAccess(files: List<FileItem>, sortingOrder: SortingOrder) =
        if (sortingOrder == SortingOrder.DESCENDING) files.sortedByDescending { it.dateAccess } else files.sortedBy { it.dateAccess }
}