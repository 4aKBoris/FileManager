package com.example.filemanager.sort

import com.example.filemanager.item.FileItem

class Sort(
    private val files: List<FileItem>,
    private val typeOfGrouping: TypeOfGrouping,
    private val sortingType: SortingType,
    private val sortingOrder: SortingOrder
) {

    fun sort() = when (typeOfGrouping) {
        TypeOfGrouping.DEFAULT -> when (sortingType) {
            SortingType.ALPHABET -> sortFileName(files, sortingOrder)
            SortingType.SIZE -> sortFileSize(files, sortingOrder)
            SortingType.DATE_CREATE -> sortDateCreate(files, sortingOrder)
            SortingType.DATE_CHANGE -> sortDateChange(files, sortingOrder)
            SortingType.DATE_RECENT_DISCOVERIES -> sortDateAccess(files, sortingOrder)
            else -> files
        }
        TypeOfGrouping.FILES -> {
            val directories = files.filter { it.isDirectory }
            val file = files.filterNot { it.isDirectory }
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
        TypeOfGrouping.FOLDERS -> {
            val directories = files.filter { it.isDirectory }
            val file = files.filterNot { it.isDirectory }
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
        if (sortingOrder == SortingOrder.DESCENDING) files.sortedByDescending { it.fileName } else files.sortedBy { it.fileName }

    private fun sortFileSize(files: List<FileItem>, sortingOrder: SortingOrder) =
        if (sortingOrder == SortingOrder.DESCENDING) files.sortedByDescending { it.fileSize } else files.sortedBy { it.fileSize }

    private fun sortDateCreate(files: List<FileItem>, sortingOrder: SortingOrder) =
        if (sortingOrder == SortingOrder.DESCENDING) files.sortedByDescending { it.dateCreate } else files.sortedBy { it.dateCreate }

    private fun sortDateChange(files: List<FileItem>, sortingOrder: SortingOrder) =
        if (sortingOrder == SortingOrder.DESCENDING) files.sortedByDescending { it.dateChange } else files.sortedBy { it.dateChange }

    private fun sortDateAccess(files: List<FileItem>, sortingOrder: SortingOrder) =
        if (sortingOrder == SortingOrder.DESCENDING) files.sortedByDescending { it.dateAccess } else files.sortedBy { it.dateAccess }
}