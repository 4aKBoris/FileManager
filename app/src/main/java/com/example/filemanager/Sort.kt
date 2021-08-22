package com.example.filemanager

import com.example.filemanager.item.FileItem

class Sort(private val files: List<FileItem>,
           private val type: Int,
           private val typeSort: Int,
           private val upDown: Boolean) {

    fun sort() = when (type) {
        1 -> when (typeSort) {
            1 -> sortFileName(files, upDown)
            2 -> sortFileSize(files, upDown)
            3 -> sortDateCreate(files, upDown)
            4 -> sortDateChange(files, upDown)
            5 -> sortDateAccess(files, upDown)
            else -> files
        }
        2 -> {
            val directories = files.filter { it.isDirectory }
            val file = files.filterNot { it.isDirectory }
            when (typeSort) {
                1 -> sortFileName(file, upDown).plus(sortFileName(directories, upDown))
                2 -> sortFileSize(file, upDown).plus(sortFileSize(directories, upDown))
                3 -> sortDateCreate(file, upDown).plus(sortDateCreate(directories,
                    upDown))
                4 -> sortDateChange(file, upDown).plus(sortDateChange(directories,
                    upDown))
                5 -> sortDateCreate(file, upDown).plus(sortDateAccess(directories,
                    upDown))
                else -> files
            }
        }
        3 -> {
            val directories = files.filter { it.isDirectory }
            val file = files.filterNot { it.isDirectory }
            when (typeSort) {
                1 -> sortFileName(directories, upDown).plus(sortFileName(file, upDown))
                2 -> sortFileSize(directories, upDown).plus(sortFileSize(file, upDown))
                3 -> sortDateCreate(directories, upDown).plus(sortDateCreate(file,
                    upDown))
                4 -> sortDateChange(directories, upDown).plus(sortDateChange(file,
                    upDown))
                5 -> sortDateCreate(directories, upDown).plus(sortDateAccess(file,
                    upDown))
                else -> files
            }
        }

        else -> files
    }

    private fun sortFileName(files: List<FileItem>, upDown: Boolean) =
        if (upDown) files.sortedByDescending { it.fileName } else files.sortedBy { it.fileName }

    private fun sortFileSize(files: List<FileItem>, upDown: Boolean) =
        if (upDown) files.sortedByDescending { it.fileSize } else files.sortedBy { it.fileSize }

    private fun sortDateCreate(files: List<FileItem>, upDown: Boolean) =
        if (upDown) files.sortedByDescending { it.dateCreate } else files.sortedBy { it.dateCreate }

    private fun sortDateChange(files: List<FileItem>, upDown: Boolean) =
        if (upDown) files.sortedByDescending { it.dateChange } else files.sortedBy { it.dateChange }

    private fun sortDateAccess(files: List<FileItem>, upDown: Boolean) =
        if (upDown) files.sortedByDescending { it.dateAccess } else files.sortedBy { it.dateAccess }
}