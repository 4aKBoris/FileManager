@file:Suppress("EXPERIMENTAL_IS_NOT_ENABLED")

package com.example.filemanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.filemanager.ui.components.bar.bottom.BottomBar
import com.example.filemanager.ui.components.drawer.Drawer
import com.example.filemanager.ui.components.recyclerview.RecyclerView
import com.example.filemanager.ui.components.bar.top.TopBar
import com.example.filemanager.ui.components.TopBarSort
import com.example.filemanager.ui.components.TopBarStorage
import com.example.filemanager.ui.theme.FileManagerTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val recyclerViewModel: RecyclerViewModel by viewModels()

    private lateinit var topBarStorage: TopBarStorage

    private lateinit var topBarSort: TopBarSort

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val p = Permissions()
        p.requestMultiplePermissions(this, 0)

        topBarStorage = TopBarStorage(recyclerViewModel)

        topBarSort =
            TopBarSort(recyclerViewModel, resources.displayMetrics.widthPixels.toFloat() / 2)

        setContent {
            FileManagerTheme(recyclerViewModel.theme) {
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
        if (recyclerViewModel.isEmptyBackStack()) super.onBackPressed()
        else recyclerViewModel.onBackPressed()
    }

    @OptIn(ExperimentalAnimationApi::class, kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    @Composable
    fun MainContent() {

        val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))

        val scope = rememberCoroutineScope()

        val openDrawer = {
            scope.launch {
                scaffoldState.drawerState.open()
            }
        }

        Scaffold(
            topBar = { TopBar(viewModel = recyclerViewModel, openDrawer = { openDrawer() }) },
            drawerContent = { Drawer(viewModel = recyclerViewModel, recyclerViewModel::swapTheme) },
            content = {
                Column(Modifier.fillMaxSize()) {
                    Box() {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .align(Alignment.CenterStart)
                        ) {
                            topBarStorage.TopBarStorage()
                            RecyclerView(recyclerViewModel)
                        }
                        topBarSort.TopBarSort(modifier = Modifier.align(Alignment.BottomCenter))
                    }

                }
            },
            scaffoldState = scaffoldState,
            bottomBar = {
                BottomBar(
                    viewModel = recyclerViewModel, displayWidth =
                    (resources.displayMetrics.widthPixels / resources.displayMetrics.density).dp
                )
            }
        )
    }
}

