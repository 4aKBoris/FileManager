@file:Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.example.filemanager

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.filemanager.constants.STORAGE
import com.example.filemanager.extensions.convertToFileItem
import com.example.filemanager.extensions.sortByCondition
import com.example.filemanager.item.FileItem
import com.example.filemanager.sort.SortingOrder
import com.example.filemanager.sort.SortingType
import com.example.filemanager.sort.TypeOfGrouping
import java.io.File

class RecyclerViewModel : ViewModel() {

    private val pathStack = mutableListOf<String>(STORAGE)

    var path: String by mutableStateOf(STORAGE)

    fun addPath(folder: String) {
        newPath("$path/$folder")
    }

    fun newPath(path: String) {
        this.path = path
        pathStack.add(path)
    }

    fun isEmptyBackStack() = pathStack.size == 1

    fun onBackPressed() {
        pathStack.removeLast()
        path = pathStack.last()
    }

    var files: List<FileItem> = listOf()
        private set
        get() = File(path).listFiles().filter { it.name.contains(request) }.convertToFileItem()
            .sortByCondition(typeOfGrouping, sortingType, sortingOrder)

    private val selectedItems = mutableStateListOf<String>()

    fun isSelectedItem(name: String) = selectedItems.contains(name)

    fun checkItem(name: String) {
        if (isSelectedItem(name)) removeSelectedItem(name)
        else addSelectedItem(name)
    }

    private fun addSelectedItem(name: String) {
        selectedItems.add(name)
    }

    private fun removeSelectedItem(name: String) {
        selectedItems.remove(name)
    }

    private fun clearSelectedItems() {
        selectedItems.clear()
    }

    var selectionMode by mutableStateOf(false)

    fun swapSelectionMode() {
        if (selectionMode) clearSelectedItems()
        selectionMode = selectionMode.not()
    }

    var request by mutableStateOf("")

    var sortMenuVisible by mutableStateOf(false)

    fun sortMenuChangeVisible() {
        sortMenuVisible = sortMenuVisible.not()
    }

    var storageMenuVisible by mutableStateOf(false)

    fun storageMenuChangeVisible() {
        storageMenuVisible = storageMenuVisible.not()
    }

    var sortingOrder by mutableStateOf(SortingOrder.DEFAULT)

    fun swapSortingOrder() {
        sortingOrder =
            if (sortingOrder == SortingOrder.ASCENDING || sortingOrder == SortingOrder.DEFAULT) SortingOrder.DESCENDING
            else SortingOrder.ASCENDING
    }

    var sortingType by mutableStateOf(SortingType.DEFAULT)

    var typeOfGrouping by mutableStateOf(TypeOfGrouping.DEFAULT)

    var theme by mutableStateOf(false)

    fun swapTheme() {
        theme = theme.not()
    }

}