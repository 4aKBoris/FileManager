@file:Suppress("EXPERIMENTAL_IS_NOT_ENABLED")

package com.example.filemanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.filemanager.components.*
import com.example.filemanager.ui.theme.FileManagerTheme

class MainActivity : ComponentActivity() {

    private val recyclerViewModel: RecyclerViewModel by viewModels()

    private lateinit var recyclerView: RecyclerView

    private lateinit var topBar: TopBar

    private lateinit var bottomBar: BottomBar

    private lateinit var topBarStorage: TopBarStorage

    private lateinit var topBarSort: TopBarSort

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val p = Permissions()
        p.requestMultiplePermissions(this, 0)

        recyclerView = RecyclerView(recyclerViewModel)

        topBar = TopBar(recyclerViewModel)

        bottomBar = BottomBar(recyclerViewModel)

        topBarStorage = TopBarStorage(recyclerViewModel)

        topBarSort =
            TopBarSort(recyclerViewModel, resources.displayMetrics.widthPixels.toFloat() / 2)

        setContent {
            FileManagerTheme {
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

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    fun MainContent() {

        Scaffold(
            topBar = { topBar.TopBar() },
            content = {
                Column(Modifier.fillMaxSize()) {
                    Box() {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .align(Alignment.CenterStart)
                        ) {
                            topBarStorage.TopBarStorage()
                            recyclerView.RecyclerView()
                        }
                        topBarSort.TopBarSort(modifier = Modifier.align(Alignment.BottomCenter))
                    }

                }
            },
            floatingActionButton = {
                IconToggleButton(checked = false, onCheckedChange = {}, modifier = Modifier.border(width = 2.dp, color = Color.Black)) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = "Режим выделения")
                }
            },
            floatingActionButtonPosition = FabPosition.End,
            isFloatingActionButtonDocked = true,
            bottomBar = { bottomBar.BottomBar() }
        )
    }
}

