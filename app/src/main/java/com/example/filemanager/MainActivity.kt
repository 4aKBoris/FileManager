@file:Suppress("EXPERIMENTAL_IS_NOT_ENABLED")

package com.example.filemanager

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.filemanager.ui.components.TopBarSort
import com.example.filemanager.ui.components.TopBarStorage
import com.example.filemanager.ui.components.bar.bottom.BottomBar
import com.example.filemanager.ui.components.bar.top.TopBar
import com.example.filemanager.ui.components.drawer.Drawer
import com.example.filemanager.ui.components.recyclerview.RecyclerView
import com.example.filemanager.ui.theme.FileManagerTheme
import com.example.filemanager.view.model.FileManagerViewModel
import com.example.filemanager.view.model.FileManagerViewModelFactory
import kotlinx.coroutines.launch

private const val DATA_STORE_FILE_MANAGER = "data_store_file_manager"

private val Context.dataStoreFileManager by preferencesDataStore(name = DATA_STORE_FILE_MANAGER)

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: FileManagerViewModel

    private lateinit var topBarStorage: TopBarStorage

    private lateinit var topBarSort: TopBarSort

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(
            this, FileManagerViewModelFactory(dataStore = dataStoreFileManager, resources = resources)
        ).get(FileManagerViewModel::class.java)

        lifecycle.addObserver(viewModel)

        requestMultiplePermissions(this, 0)

        topBarStorage = TopBarStorage(viewModel)

        topBarSort =
            TopBarSort(viewModel, resources.displayMetrics.widthPixels.toFloat() / 2)

        setContent {

            val theme by viewModel.theme.collectAsState(false)

            FileManagerTheme(theme) {
                Surface(
                    color = MaterialTheme.colors.background,
                    modifier = Modifier.wrapContentSize()
                ) {
                    Box(modifier = Modifier.wrapContentSize()) {
                        MainContent()
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        if (viewModel.isEmptyBackStack()) super.onBackPressed()
        else viewModel.onBackPressed()
    }

    @OptIn(ExperimentalAnimationApi::class, kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    @Composable
    fun MainContent() {

        val scaffoldState =
            rememberScaffoldState(rememberDrawerState(DrawerValue.Closed), SnackbarHostState())

        val scope = rememberCoroutineScope()

        val openDrawer = fun() {
            scope.launch {
                scaffoldState.drawerState.open()
            }
        }

        val closeDrawer = fun() {
            scope.launch {
                scaffoldState.drawerState.close()
            }
        }

        Scaffold(
            topBar = { TopBar(viewModel = viewModel, openDrawer = openDrawer) },
            drawerContent = { Drawer(viewModel = viewModel, closeDrawer = closeDrawer) },
            content = {
                Column(Modifier.fillMaxSize()) {
                    Box {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .align(Alignment.CenterStart)
                        ) {
                            topBarStorage.TopBarStorage()
                            RecyclerView(
                                viewModel = viewModel,
                                state = scaffoldState.snackbarHostState
                            )
                        }
                        topBarSort.TopBarSort(modifier = Modifier.align(Alignment.BottomCenter))
                    }

                }
            },
            scaffoldState = scaffoldState,
            bottomBar = {
                BottomBar(
                    viewModel = viewModel,
                    displayWidth =
                    (resources.displayMetrics.widthPixels / resources.displayMetrics.density).dp,
                    state = scaffoldState.snackbarHostState
                )
            },
        )
    }
}

