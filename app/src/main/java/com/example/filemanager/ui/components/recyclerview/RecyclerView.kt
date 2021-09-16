@file:Suppress("UNUSED_EXPRESSION", "EXPERIMENTAL_IS_NOT_ENABLED")

package com.example.filemanager.ui.components.recyclerview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.filemanager.RecyclerViewModel
import com.example.filemanager.extensions.dp
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

const val ACTION_ITEM_SIZE = 56
const val CARD_HEIGHT = 56
const val CARD_OFFSET = 168f // we have 3 icons in a row, so that's 56 * 3

@ExperimentalCoroutinesApi
@Composable
fun RecyclerView(viewModel: RecyclerViewModel, state: SnackbarHostState) {

    val items by viewModel.files.collectAsState()
    val selectedItems by viewModel.selectedItems.collectAsState()
    val revealedItems by viewModel.revealedFiles.collectAsState()
    val favoriteItems by viewModel.favoriteFiles.collectAsState()
    val scope = rememberCoroutineScope()

    Box {
        LazyColumn {
            itemsIndexed(items = items) { index, item ->

                var info by remember { mutableStateOf(false) }
                var edit by remember { mutableStateOf(false) }

                val (text, setText) = rememberSaveable { mutableStateOf(item.name) }

                Box(Modifier.fillMaxWidth()) {
                    ActionsRow(
                        size = ACTION_ITEM_SIZE.dp,
                        favoriteFile = favoriteItems.contains(item.path),
                        onInfo = {
                            viewModel.onItemCollapsed(item.name)
                            info = true
                        },
                        onFavorite = { viewModel.onClickFavoriteFile(item.path) },
                        onEdit = {
                            viewModel.onItemCollapsed(item.name)
                            edit = true
                        }
                    )
                    DraggableFile(
                        item = item,
                        info = info,
                        edit = edit,
                        text = text,
                        selected = selectedItems.contains(item.file),
                        isRevealed = revealedItems.contains(item.name),
                        cardHeight = CARD_HEIGHT.dp,
                        cardOffset = CARD_OFFSET.dp(),
                        onClose = { info = false },
                        onCancelEdit = { edit = false },
                        onEdit = {
                            val pairMessage = viewModel.renameFile(item = item, name = text)
                            scope.launch {
                                val s = state.showSnackbar(
                                    message = pairMessage.first,
                                    actionLabel = pairMessage.second,
                                    duration = SnackbarDuration.Long
                                )
                                when (s) {
                                    SnackbarResult.Dismissed -> { }
                                    SnackbarResult.ActionPerformed -> {
                                        val message = viewModel.renameFile(item = item, index = index)
                                        setText(item.name)
                                        state.showSnackbar(message = message, null, SnackbarDuration.Short)
                                    }
                                }
                            }
                            edit = false
                        },
                        onExpand = { viewModel.onItemExpanded(item.name, edit || info) },
                        onCollapse = { viewModel.onItemCollapsed(item.name) },
                        onItemClick = { viewModel.onClick(item) },
                        onItemLongClick = { viewModel.onLongClick(item) },
                        setText = setText
                    )
                }
                Divider()
            }
        }
    }
}


