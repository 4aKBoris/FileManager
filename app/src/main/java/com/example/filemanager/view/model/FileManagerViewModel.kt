@file:Suppress(
    "RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS",
    "EXPERIMENTAL_IS_NOT_ENABLED"
)

package com.example.filemanager.view.model

import android.content.res.Resources
import androidx.annotation.StringRes
import androidx.compose.runtime.*
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.*
import com.example.filemanager.R
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
import kotlinx.coroutines.launch
import java.io.File

class FileManagerViewModel(dataStore: DataStore<Preferences>, private val resources: Resources) :
    ViewModel(), LifecycleObserver {

    private val fileManagerDataStore = FileManagerDataStore(dataStore = dataStore)

    private val pathStack = mutableListOf<String>(STORAGE)

    var path: String by mutableStateOf(STORAGE)
    var hidden: Boolean by mutableStateOf(false)
    var sortingType: SortingType by mutableStateOf(SortingType.DEFAULT)
    var sortingOrder: SortingOrder by mutableStateOf(SortingOrder.DEFAULT)
    var groupingType: GroupingType by mutableStateOf(GroupingType.DEFAULT)
    var theme: Boolean by mutableStateOf(false)
    var favoriteFiles = mutableStateListOf<String>()
    var lastFiles = mutableStateListOf<String>()
    var request: String by mutableStateOf("")
    var selectionMode: Boolean by mutableStateOf(false)
    var revealedFiles = mutableStateListOf<String>()
    var selectedItems = mutableStateListOf<File>()
    var storageMenuVisible by mutableStateOf(false)
    val files = File(path).listFiles()
            .filter { it.name.contains(request) && (!it.isHidden || hidden) }
            .convertToFileItem(resources = resources)
            .sortedWith(groupingType.comparator.then(sortingOrder.getComparator(sortingType.comparator)))
            .toMutableStateList()

    fun storageMenuChangeVisible() {
        storageMenuVisible = storageMenuVisible.not()
    }

    fun onChangeHidden(hidden: Boolean) {
        this.hidden = hidden
    }

    fun onChangeRequest(request: String) {
        this.request = request
    }

    fun swapTheme(isDark: Boolean) {
        theme = isDark
    }

    fun setSortingType(@StringRes idRes: Int) {
        sortingType = SortingType.values().first { it.idRes == idRes }
    }

    fun setSortingOrder(@StringRes idRes: Int) {
        sortingOrder = SortingOrder.values().first { it.idRes == idRes }
    }

    fun setGroupingType(@StringRes idRes: Int) {
        groupingType = GroupingType.values().first { it.idRes == idRes }
    }

    private fun removeItem(file: File) {
        indexDeleteFiles[file.name] = findIndex(file)
        files.removeIf { it.file == file }
    }

    fun addItem(file: File, index: Int? = null) {
        if (index == null) files.add(element = FileItem(file, resources = resources))
        else files.add(index = index, element = FileItem(file, resources = resources))
    }

    private fun findIndex(file: File) =
        files.indexOf(files.find { it.file == file })

    fun onClickFavoriteFile(path: String) {
        if (favoriteFiles.contains(path)) removeFavoriteFile(path = path) else addFavoriteFile(
            path = path
        )
    }

    private fun addFavoriteFile(path: String) {
        favoriteFiles.add(path)
    }

    fun removeFavoriteFile(path: String) {
        favoriteFiles.remove(path)
    }

    fun removeAllFavoriteFile() {
        favoriteFiles.clear()
    }

    fun onItemExpanded(path: String) {
        revealedFiles.add(path)
    }

    fun onItemCollapsed(path: String) {
        revealedFiles.remove(path)
    }

    private fun addSelectedItem(file: File) {
        selectedItems.add(file)
    }

    private fun removeSelectedItem(file: File) {
        selectedItems.remove(file)
    }

    private fun clearSelectedItems() {
        selectedItems.clear()
    }

    private fun addPath(folder: String) {
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

    fun onClick(item: FileItem) {
        when (selectionMode) {
            true -> if (selectedItems.contains(item.file)) removeSelectedItem(item.file) else addSelectedItem(
                item.file
            )
            else -> {
                if (item.isDirectory) addPath(item.name)
                lastFiles.remove(item.path)
                lastFiles.add(0, item.path)
            }
        }
    }

    fun onLongClick(item: FileItem) {
        if (item.isDirectory) addPath(item.name)
    }


    fun swapSelectionMode() {
        if (selectionMode) clearSelectedItems()
        selectionMode = selectionMode.not()
    }

    fun renameFile(item: FileItem, name: String): Pair<String, String?> {
        return try {
            val newFile = File(item.path.replace(item.name, name))
            if (files.find { it.name == name } != null) throw Exception("${item.type.name} с таким именем уже существует!")
            if (name.isBlank()) throw Exception("Введите имя файла!")
            if (item.file.renameTo(newFile)) {
                files[files.indexOf(item)] = item.copy(file = newFile)
                "Переименование прошло успешно" to "Отменить"
            } else throw Exception("Переименовать закончиловь ошибкой!")
        } catch (e: Exception) {
            e.message!! to null
        }
    }

    fun renameFile(item: FileItem, index: Int): String {
        return try {
            if (files.find { it.name == item.name } != null) throw Exception("${item.type.name} с таким именем уже существует, отмена невозможна!")
            if (files[index].file.renameTo(item.file)) {
                files[index] = item
                "Отмена переименования прошла успешно!"
            } else throw Exception("Отмена переименования закончилась ошибкой!")
        } catch (e: Exception) {
            e.message!!
        }
    }

    fun deleteFiles(): String {
        val notDeleteFiles = mutableListOf<String>()
        selectedItems.forEach {
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
                it.copyTo(File("$path/${it.name}"), true),
                indexDeleteFiles.remove(it.name)
            )
            if (it.isDirectory) it.deleteRecursively() else it.delete()
        }
        indexDeleteFiles.clear()
    }

    fun pathToDataTab(list: List<String>, onDelete: (String) -> Unit, closeDrawer: () -> Unit) =
        list.map {
            val file = it.toFile()
            DataTab(
                idPainter = getType(file = file).idRes,
                name = file.name,
                info = file.parent ?: STORAGE,
                description = "${resources.getString(if (file.isFile) R.string.file else R.string.folder)} ${file.name}",
                closeDrawer = closeDrawer,
                onClick = { if (file.isDirectory) path = file.absolutePath },
                onLongClick = {
                    selectionMode = true
                    path = file.parent ?: STORAGE
                    addSelectedItem(file)
                },
                onDelete = { onDelete(it) }
            )
        }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun onCreate() {
        viewModelScope.launch(Dispatchers.IO) {
            println("dwadwa3443")
            fileManagerDataStore.apply {
                favoriteFiles.addAll(getSet(key = FAVORITE_FILES_KEY))
                lastFiles.addAll(getSet(key = LAST_FILES_KEY))
                theme = getBoolean(key = THEME)
                groupingType = GroupingType.valueOf(getString(key = GROUPING_TYPE))
                sortingType = SortingType.valueOf(getString(key = SORTING_TYPE))
                sortingOrder = SortingOrder.valueOf(getString(key = SORTING_ORDER))
                hidden = getBoolean(key = HIDDEN)
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private fun onStop() {
        viewModelScope.launch(Dispatchers.IO) {
            fileManagerDataStore.apply {
                save(key = FAVORITE_FILES_KEY, value = favoriteFiles)
                save(key = LAST_FILES_KEY, value = lastFiles)
                save(key = THEME, value = theme)
                save(key = GROUPING_TYPE, value = groupingType.name)
                save(key = SORTING_TYPE, value = sortingType.name)
                save(key = SORTING_ORDER, value = sortingOrder.name)
                save(key = HIDDEN, value = hidden)
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

        private const val HIDDEN = "hidden"

        fun List<File>.convertToFileItem(resources: Resources) =
            this.map { FileItem(it, resources) }
    }
}