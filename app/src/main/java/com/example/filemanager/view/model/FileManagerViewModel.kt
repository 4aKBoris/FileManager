@file:Suppress(
    "RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS",
    "EXPERIMENTAL_IS_NOT_ENABLED"
)

package com.example.filemanager.view.model

import android.content.res.Resources
import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.*
import com.example.filemanager.R
import com.example.filemanager.constants.LOG_TAG
import com.example.filemanager.constants.STORAGE
import com.example.filemanager.constants.pathDeleteFiles
import com.example.filemanager.extensions.toFile
import com.example.filemanager.item.FileItem
import com.example.filemanager.item.getType
import com.example.filemanager.sort.GroupingType
import com.example.filemanager.sort.SortingOrder
import com.example.filemanager.sort.SortingType
import com.example.filemanager.ui.components.drawer.tabs.DataTab
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File

class FileManagerViewModel(dataStore: DataStore<Preferences>, private val resources: Resources) :
    ViewModel(), LifecycleObserver {

    private val fileManagerDataStore = FileManagerDataStore(dataStore = dataStore)

    private val pathStack = mutableListOf<String>(STORAGE)


    var request by mutableStateOf("")

    private val _path = mutableStateOf(STORAGE)
    private val _sortingType = mutableStateOf(SortingType.DEFAULT)
    private val _sortingOrder = mutableStateOf(SortingOrder.DEFAULT)
    private val _groupingType = mutableStateOf(GroupingType.DEFAULT)
    private val _theme = MutableStateFlow(false)

    private val _revealedFiles = MutableStateFlow(mutableStateListOf<String>())
    private val _favoriteFiles = MutableStateFlow(mutableStateListOf<String>())
    private val _lastFiles = MutableStateFlow(listOf<String>())
    private val _selectedItems = MutableStateFlow(mutableStateListOf<File>())
    private val _selectionMode = MutableStateFlow(false)

    val path: State<String> get() = _path
    val sortingType: State<SortingType> get() = _sortingType
    val sortingOrder: State<SortingOrder> get() = _sortingOrder
    val groupingType: State<GroupingType> get() = _groupingType
    val theme: StateFlow<Boolean> get() = _theme

    val revealedFiles: StateFlow<SnapshotStateList<String>> get() = _revealedFiles
    val favoriteFiles: StateFlow<List<String>> get() = _favoriteFiles
    val lastFiles: StateFlow<List<String>> get() = _lastFiles
    val selectedItems: StateFlow<List<File>> get() = _selectedItems
    val selectionMode: StateFlow<Boolean> get() = _selectionMode
    val files: SnapshotStateList<FileItem>
        get() = File(_path.value).listFiles().filter { it.name.contains(request) }
            .convertToFileItem(resources = resources)
            .sortedWith(_groupingType.value.comparator.then(_sortingOrder.value.getComparator(_sortingType.value.comparator)))
            .toMutableStateList()

    fun swapTheme(isDark: Boolean) {
        _theme.value = isDark
    }

    fun setSortingType(@StringRes idRes: Int) {
        _sortingType.value = SortingType.values().first { it.idRes == idRes }
    }

    fun setSortingOrder(@StringRes idRes: Int) {
        _sortingOrder.value = SortingOrder.values().first { it.idRes == idRes }
    }

    fun setGroupingType(@StringRes idRes: Int) {
        _groupingType.value = GroupingType.values().first { it.idRes == idRes }
    }

    private fun removeItem(file: File) {
        indexDeleteFiles[file.name] = findIndex(file)
        files.removeIf { it.file == file }
    }

    fun add() {
        println(_path.value)
    }

    fun addItem(file: File, index: Int? = null) {
        if (index == null) files.add(element = FileItem(file, resources = resources))
        else files.add(index = index, element = FileItem(file, resources = resources))
    }

    private fun findIndex(file: File) =
        files.indexOf(files.find { it.file == file })

    fun onClickFavoriteFile(path: String) {
        if (_favoriteFiles.value.contains(path)) removeFavoriteFile(path = path) else addFavoriteFile(
            path = path
        )
    }

    private fun addFavoriteFile(path: String) {
        _favoriteFiles.value.add(path)
    }

    fun removeFavoriteFile(path: String) {
        _favoriteFiles.value.remove(path)
    }

    fun removeAllFavoriteFile() {
        _favoriteFiles.value.clear()
    }

    fun onItemExpanded(name: String) {
        _revealedFiles.value.add(name)
    }

    fun onItemCollapsed(name: String) {
        _revealedFiles.value.remove(name)
    }

    fun swapSortingOrder() {
        _sortingOrder.value =
            if (sortingOrder.value == SortingOrder.ASCENDING || sortingOrder.value == SortingOrder.DEFAULT) SortingOrder.DESCENDING
            else SortingOrder.ASCENDING
    }

    init {
        // getFiles()
    }

    /*private fun getFiles() {
        viewModelScope.launch(Dispatchers.Default) {
            files.value.addAll(
                File(_path.value).listFiles().toList().filter { it.name.contains(request) }
                    .convertToFileItem()
                    .sortByCondition(typeOfGrouping.value, sortingType.value, sortingOrder.value))
        }
    }*/

    private fun addPath(folder: String) {
        newPath("${_path.value}/$folder")
    }

    fun newPath(path: String) {
        _path.value = path
        Log.d(LOG_TAG, _path.value)
        Log.d(LOG_TAG, path)
        Log.d(LOG_TAG, files.size.toString())
        //getFiles()
        pathStack.add(_path.value)
    }

    fun isEmptyBackStack() = pathStack.size == 1

    fun onBackPressed() {
        pathStack.removeLast()
        _path.value = pathStack.last()
        //getFiles()
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
        /*return try {
            val newFile = File(item.path.replace(item.name, name))
            if (_files.value.find { it.name == name } != null) throw Exception("${item.type.name} с таким именем уже существует!")
            if (name.isBlank()) throw Exception("Введите имя файла!")
            if (item.file.renameTo(newFile)) {
                _files.value[_files.value.indexOf(item)] = item.copy(file = newFile)
                "Переименование прошло успешно" to "Отменить"
            } else throw Exception("Переименовать закончиловь ошибкой!")
        } catch (e: Exception) {
            e.message!! to null
        }*/
        return "dwa" to "dwa"
    }

    fun renameFile(item: FileItem, index: Int): String {
        /*return try {
            if (_files.value.find { it.name == item.name } != null) throw Exception("${item.type.name} с таким именем уже существует, отмена невозможна!")
            if (_files.value[index].file.renameTo(item.file)) {
                _files.value[index] = item
                "Отмена переименования прошла успешно!"
            } else throw Exception("Отмена переименования закончилась ошибкой!")
        } catch (e: Exception) {
            e.message!!
        }*/
        return "dwa"
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

    var sortMenuVisible by mutableStateOf(false)

    fun sortMenuChangeVisible() {
        sortMenuVisible = sortMenuVisible.not()
    }

    var storageMenuVisible by mutableStateOf(false)

    fun storageMenuChangeVisible() {
        storageMenuVisible = storageMenuVisible.not()
    }

    fun pathToDataTab(list: List<String>, onDelete: (String) -> Unit, closeDrawer: () -> Unit) = list.map {
        val file = it.toFile()
        DataTab(
            idPainter = getType(file = file).idRes,
            name = file.name,
            info = file.parent ?: STORAGE,
            description = "${resources.getString(if (file.isFile) R.string.file else R.string.folder)} ${file.name}",
            closeDrawer = closeDrawer,
            onClick = { if (file.isDirectory) _path.value = file.absolutePath },
            onLongClick = {
                _selectionMode.value = true
                _path.value = file.parent ?: STORAGE
                addSelectedItem(file)
            },
            onDelete = { onDelete(it) }
        )
    }

    fun onItemClick(path: String, closeDrawer: () -> Unit) {
        _path.value = path
        closeDrawer()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun onCreate() {
        viewModelScope.launch(Dispatchers.IO) {
            println("dwadwa3443")
            fileManagerDataStore.apply {
                _favoriteFiles.value.addAll(getSet(key = FAVORITE_FILES_KEY))
                _lastFiles.value = getSet(key = LAST_FILES_KEY)
                _theme.value = getBoolean(THEME)
                _groupingType.value = GroupingType.valueOf(getString(GROUPING_TYPE))
                _sortingType.value = SortingType.valueOf(getString(SORTING_TYPE))
                _sortingOrder.value = SortingOrder.valueOf(getString(SORTING_ORDER))
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private fun onStop() {
        viewModelScope.launch(Dispatchers.IO) {
            fileManagerDataStore.apply {
                save(key = FAVORITE_FILES_KEY, value = _favoriteFiles.value)
                save(key = LAST_FILES_KEY, value = _lastFiles.value)
                save(key = THEME, value = _theme.value)
                save(key = GROUPING_TYPE, value = _groupingType.value.name)
                save(key = SORTING_TYPE, value = _sortingType.value.name)
                save(key = SORTING_ORDER, value = _sortingOrder.value.name)
            }
        }
    }

    companion object {

        private val indexDeleteFiles = mutableMapOf<String, Int>()

        private const val FAVORITE_FILES_KEY = "favorite_files_key"

        private const val LAST_FILES_KEY = "last_files_key"

        private const val THEME = "theme"

        private const val GROUPING_TYPE = "grouping_type"

        private const val SORTING_TYPE = "sorting_type"

        private const val SORTING_ORDER = "sorting_order"

        fun List<File>.convertToFileItem(resources: Resources) = this.map { FileItem(it, resources) }
    }
}