@file:Suppress("UNUSED_EXPRESSION", "EXPERIMENTAL_IS_NOT_ENABLED")

package com.example.filemanager.ui.components.recyclerview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.filemanager.RecyclerViewModel
import com.example.filemanager.extensions.dp
import kotlinx.coroutines.ExperimentalCoroutinesApi

const val ACTION_ITEM_SIZE = 56
const val CARD_HEIGHT = 56
const val CARD_OFFSET = 168f // we have 3 icons in a row, so that's 56 * 3

@ExperimentalCoroutinesApi
@Composable
fun RecyclerView(viewModel: RecyclerViewModel) {
    val files by viewModel.files.collectAsState()
    val revealedFiles by viewModel.revealedFiles.collectAsState()
    val favoriteFiles by viewModel.favoriteFiles.collectAsState()
    val selectedFiles by viewModel.selectedItems.collectAsState()
    Box {
        LazyColumn {
            items(items = files) { item ->

                var info by remember { mutableStateOf(false) }
                var edit by remember { mutableStateOf(false) }

                Box(Modifier.fillMaxWidth()) {
                    ActionsRow(
                        size = ACTION_ITEM_SIZE.dp,
                        favoriteFile = favoriteFiles.contains(item.path),
                        onInfo = {
                            viewModel.onItemCollapsed(item.fileName)
                            info = true
                        },
                        onFavorite = { viewModel.onClickFavoriteFile(item.path) },
                        onEdit = {
                            viewModel.onItemCollapsed(item.fileName)
                            edit = true
                        }
                    )
                    DraggableFile(
                        item = item,
                        info = info,
                        edit = edit,
                        selected = selectedFiles.contains(item.fileName),
                        isRevealed = revealedFiles.contains(item.fileName),
                        cardHeight = CARD_HEIGHT.dp,
                        cardOffset = CARD_OFFSET.dp(),
                        onClose = { info = false },
                        onCancelEdit = { edit = false },
                        onEdit = { edit = false },
                        onExpand = { viewModel.onItemExpanded(item.fileName) },
                        onCollapse = { viewModel.onItemCollapsed(item.fileName) },
                        onItemClick = { viewModel.onClick(item) },
                        onItemLongClick = { viewModel.onLongClick(item) }
                    )
                }
                Divider()
            }
        }
    }
}


