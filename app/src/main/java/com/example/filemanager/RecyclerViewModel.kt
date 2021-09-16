@file:Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.example.filemanager

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filemanager.constants.STORAGE
import com.example.filemanager.constants.pathDeleteFiles
import com.example.filemanager.extensions.convertToFileItem
import com.example.filemanager.extensions.sortByCondition
import com.example.filemanager.item.FileItem
import com.example.filemanager.sort.SortingOrder
import com.example.filemanager.sort.SortingType
import com.example.filemanager.sort.TypeOfGrouping
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File

class RecyclerViewModel : ViewModel() {

    private val pathStack = mutableListOf<String>(STORAGE)

    private val _path = MutableStateFlow(STORAGE)
    private val _sortingType = MutableStateFlow(SortingType.DEFAULT)
    private val _sortingOrder = MutableStateFlow(SortingOrder.DEFAULT)
    private val _typeOfGrouping = MutableStateFlow(TypeOfGrouping.DEFAULT)
    private val _revealedFiles = MutableStateFlow(listOf<String>())
    private val _favoriteFiles = MutableStateFlow(listOf<String>())
    private val _selectedItems = MutableStateFlow(mutableStateListOf<File>())
    private val _selectionMode = MutableStateFlow(false)
    private val _files = MutableStateFlow(mutableStateListOf<FileItem>())

    val path: StateFlow<String> get() = _path
    val sortingType: MutableStateFlow<SortingType> get() = _sortingType
    val sortingOrder: StateFlow<SortingOrder> get() = _sortingOrder
    val typeOfGrouping: MutableStateFlow<TypeOfGrouping> get() = _typeOfGrouping
    val revealedFiles: StateFlow<List<String>> get() = _revealedFiles
    val favoriteFiles: StateFlow<List<String>> get() = _favoriteFiles
    val selectedItems: StateFlow<SnapshotStateList<File>> get() = _selectedItems
    val selectionMode: StateFlow<Boolean> get() = _selectionMode
    val files: StateFlow<SnapshotStateList<FileItem>> get() = _files

    private fun removeItem(file: File) {
        indexDeleteFiles[file.name] = findIndex(file)
        _files.value.removeIf { it.file == file }
    }

    fun addItem(file: File, index: Int? = null) {
        if (index == null) _files.value.add(element = FileItem(file))
        else _files.value.add(index = index, element = FileItem(file))
    }

    private fun findIndex(file: File) = _files.value.indexOf(_files.value.find { it.file == file })

    fun onClickFavoriteFile(path: String) {
        if (_favoriteFiles.value.contains(path)) onDeleteFavoriteFile(path = path) else onAddFavoriteFile(
            path = path
        )
    }

    private fun onAddFavoriteFile(path: String) {
        _favoriteFiles.value = _favoriteFiles.value.toMutableList().also { list -> list.add(path) }
    }

    private fun onDeleteFavoriteFile(path: String) {
        _favoriteFiles.value =
            _favoriteFiles.value.toMutableList().also { list -> list.remove(path) }
    }

    fun onItemExpanded(name: String, flag: Boolean) {
        if (_revealedFiles.value.contains(name) || flag) return
        _revealedFiles.value = _revealedFiles.value.toMutableList().also { list ->
            list.add(name)
        }
    }

    fun onItemCollapsed(name: String) {
        if (!_revealedFiles.value.contains(name)) return
        _revealedFiles.value = _revealedFiles.value.toMutableList().also { list ->
            list.remove(name)
        }
    }

    fun swapSortingOrder() {
        _sortingOrder.value =
            if (sortingOrder.value == SortingOrder.ASCENDING || sortingOrder.value == SortingOrder.DEFAULT) SortingOrder.DESCENDING
            else SortingOrder.ASCENDING
    }

    init {
        getFiles()
    }

    private fun getFiles() {
        viewModelScope.launch(Dispatchers.Default) {
            files.value.addAll(
                File(_path.value).listFiles().toList().filter { it.name.contains(request) }
                    .convertToFileItem()
                    .sortByCondition(typeOfGrouping.value, sortingType.value, sortingOrder.value))
        }
    }

    private fun addPath(folder: String) {
        newPath("${_path.value}/$folder")
    }

    fun newPath(path: String) {
        _path.value = path
        getFiles()
        pathStack.add(_path.value)
    }

    fun isEmptyBackStack() = pathStack.size == 1

    fun onBackPressed() {
        pathStack.removeLast()
        _path.value = pathStack.last()
        getFiles()
    }


    private fun addSelectedItem(file: File) {
        _selectedItems.value.add(file)
    }

    private fun removeSelectedItem(file: File) {
        _selectedItems.value.remove(file)
    }

    fun onClick(item: FileItem) {
        when (_selectionMode.value) {
            true -> if (_selectedItems.value.contains(item.file)) removeSelectedItem(item.file) else addSelectedItem(
                item.file
            )
            else -> if (item.isDirectory) addPath(item.name)
        }
    }

    fun onLongClick(item: FileItem) {
        if (item.isDirectory) addPath(item.name)
    }


    private fun clearSelectedItems() {
        _selectedItems.value.clear()
    }

    fun swapSelectionMode() {
        if (_selectionMode.value) clearSelectedItems()
        _selectionMode.value = _selectionMode.value.not()
    }

    fun renameFile(item: FileItem, name: String): Pair<String, String?> {
        return try {
            val newFile = File(item.path.replace(item.name, name))
            if (_files.value.find { it.name == name } != null) throw Exception("${item.type.name} с таким именем уже существует!")
            if (name.isBlank()) throw Exception("Введите имя файла!")
            if (item.file.renameTo(newFile)) {
                _files.value[_files.value.indexOf(item)] = item.copy(file = newFile)
                "Переименование прошло успешно" to "Отменить"
            } else throw Exception("Переименовать закончиловь ошибкой!")
        } catch (e: Exception) {
            e.message!! to null
        }
    }

    fun renameFile(item: FileItem, index: Int): String {
        return try {
            if (_files.value.find { it.name == item.name } != null) throw Exception("${item.type.name} с таким именем уже существует, отмена невозможна!")
            if (_files.value[index].file.renameTo(item.file)) {
                _files.value[index] = item
                "Отмена переименования прошла успешно!"
            } else throw Exception("Отмена переименования закончилась ошибкой!")
        } catch (e: Exception) {
            e.message!!
        }
    }

    fun deleteFiles(): String {
        val notDeleteFiles = mutableListOf<String>()
        _selectedItems.value.forEach {
            it.copyTo(File("$pathDeleteFiles/${it.name}"), true)
            if (it.deleteRecursively()) removeItem(it) else notDeleteFiles.add(it.name)
        }
        clearSelectedItems()
        return if (notDeleteFiles.isEmpty()) "Все выбранные папки и файлы удалены!" else "Не удалось удалить следующие файлы и папки: ${
            notDeleteFiles.joinToString(
                ", "
            )
        }"
    }

    fun confirmDeleteFiles() {
        val list = File(pathDeleteFiles).listFiles()
        if (list.isNotEmpty()) list.forEach {
            if (it.isDirectory) it.deleteRecursively() else it.delete()
        }
        indexDeleteFiles.clear()
    }

    fun cancelDeleteFiles() {
        val list = File(pathDeleteFiles).listFiles()
        if (list.isNotEmpty()) list.forEach {
            addItem(
                it.copyTo(File("${_path.value}/${it.name}"), true),
                indexDeleteFiles.remove(it.name)
            )
            if (it.isDirectory) it.deleteRecursively() else it.delete()
        }
        indexDeleteFiles.clear()
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

    var theme by mutableStateOf(false)

    fun swapTheme() {
        theme = theme.not()
    }

    companion object {
        private val indexDeleteFiles = mutableMapOf<String, Int>()
    }
}