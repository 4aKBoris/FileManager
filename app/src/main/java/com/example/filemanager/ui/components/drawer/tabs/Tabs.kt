package com.example.filemanager.ui.components.drawer.tabs

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import com.example.filemanager.R
import com.example.filemanager.view.model.FileManagerViewModel

@Composable
fun Tabs(viewModel: FileManagerViewModel) {

    val allScreens = Tab.values().toList()
    var currentScreen by rememberSaveable { mutableStateOf(Tab.Disks) }

    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            TabRow(
                allScreens = allScreens,
                onTabSelected = { screen -> currentScreen = screen },
                currentScreen = currentScreen
            )
        }) {

    }
}

@Composable
fun FavoriteFiles(viewModel: FileManagerViewModel, closeDrawer: () -> Unit) {

    val list by viewModel.favoriteFiles.collectAsState()

    RecyclerViewTab(
        listItems = viewModel.pathToDataTab(
            list = list,
            onDelete = viewModel::removeFavoriteFile,
            closeDrawer = closeDrawer
        ),
        imageVector = Icons.Default.Delete,
        description = R.string.delete_all_items,
        closeDrawer = closeDrawer,
        onBottomButtonClick = viewModel::removeAllFavoriteFile
    )
}

@Composable
fun LastFiles(viewModel: FileManagerViewModel, closeDrawer: () -> Unit) {

    val list by viewModel.lastFiles.collectAsState()

    RecyclerViewTab(
        listItems = viewModel.pathToDataTab(
            list = list,
            onDelete = viewModel::removeFavoriteFile,
            closeDrawer = closeDrawer
        ),
        imageVector = Icons.Default.Delete,
        description = R.string.delete_all_items,
        closeDrawer = closeDrawer,
        onBottomButtonClick = viewModel::removeAllFavoriteFile
    )
}

@Composable
fun Disks() {

}

