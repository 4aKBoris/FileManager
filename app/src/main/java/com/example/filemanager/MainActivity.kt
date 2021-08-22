@file:Suppress("EXPERIMENTAL_IS_NOT_ENABLED")

package com.example.filemanager

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.filemanager.components.BottomBar
import com.example.filemanager.components.RecyclerView
import com.example.filemanager.components.TopBar
import com.example.filemanager.ui.theme.FileManagerTheme

class MainActivity : ComponentActivity() {

    private val recyclerViewModel: RecyclerViewModel by viewModels()

    private lateinit var recyclerView: RecyclerView

    private lateinit var topBar: TopBar

    private lateinit var bottomBar: BottomBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val p = Permissions()
        p.requestMultiplePermissions(this, 0)

        recyclerView = RecyclerView(recyclerViewModel)

        topBar = TopBar(recyclerViewModel)

        bottomBar = BottomBar(recyclerViewModel)

        setContent {
            FileManagerTheme {
                // A surface container using the 'background' color from the theme
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

    @SuppressLint("CoroutineCreationDuringComposition")
    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    fun MainContent() {

        Scaffold(
            topBar = { topBar.TopBar() },
            content = {
                Column(Modifier.fillMaxSize()) {
                    Row() {
                        recyclerView.RecyclerView()
                    }

                }
            },
            bottomBar = { bottomBar.BottomBar()}
        )
    }
}

