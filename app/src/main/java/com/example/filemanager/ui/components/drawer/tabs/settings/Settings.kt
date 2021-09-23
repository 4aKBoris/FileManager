package com.example.filemanager.ui.components.drawer.tabs.settings

import androidx.annotation.StringRes
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.filemanager.BuildConfig
import com.example.filemanager.R
import com.example.filemanager.view.model.FileManagerViewModel

@Composable
fun Settings(viewModel: FileManagerViewModel) {

    val state = rememberLazyListState()

    var stateAlertDialog by remember { mutableStateOf(false) }

    var type by remember { mutableStateOf(SortingSections.Grouping) }

    val theme by viewModel.theme.collectAsState(false)

    Box(modifier = Modifier.fillMaxSize()) {

        LazyColumn(state = state, contentPadding = PaddingValues(bottom = 24.dp)) {
            item {
                Group(title = R.string.decoration) {
                    Item(text = R.string.theme) {
                        Theme(theme = theme, onChangeTheme = viewModel::swapTheme)
                    }
                }
                Divider()
            }
            item {
                Group(title = R.string.sorting) {
                    SortingSections.values().forEach {
                        Item(title = it.id, text = it.getIdRes(viewModel)) {
                            type = it
                            stateAlertDialog = true
                        }
                    }
                }
                Divider()
            }
            item {
                Group(title = R.string.about_application) {
                    AboutApplication()
                }
            }
        }

        AlertDialogBuilder(
            state = stateAlertDialog,
            modifier = Modifier.align(Alignment.Center),
            title = type.id,
            body = { type.Body(onClick = { stateAlertDialog = false }, viewModel = viewModel) },
            onClose = { stateAlertDialog = false })
    }
}

@Composable
private fun AboutApplication() {
    Item(text = R.string.version) {
        MinorText(text = BuildConfig.VERSION_NAME)
    }
}

@Composable
private fun Group(@StringRes title: Int, body: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp, top = 16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp), contentAlignment = Alignment.CenterStart
        ) { TitleText(title = title) }
        body()
    }
}

@Composable
private fun TitleText(@StringRes title: Int) {
    Text(
        text = stringResource(id = title),
        style = MaterialTheme.typography.h6,
        color = MaterialTheme.colors.secondaryVariant,
    )
}

@Composable
private fun Item(@StringRes text: Int, body: @Composable () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = stringResource(id = text))
        body()
    }
}

@Composable
private fun Item(@StringRes title: Int, @StringRes text: Int, onClick: () -> Unit) {
    IconButton(
        onClick = onClick, modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            MainText(text = title)
            SecondaryText(text = text)
        }
    }
}

@Composable
private fun AlertDialogBuilder(
    state: Boolean,
    modifier: Modifier,
    @StringRes title: Int,
    body: @Composable () -> Unit,
    onClose: () -> Unit
) {
    if (state) AlertDialog(
        backgroundColor = MaterialTheme.colors.background,
        modifier = modifier,
        onDismissRequest = onClose,
        text = { body() },
        title = {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
                TitleText(title = title)
            }
        },
        confirmButton = {
            Box(modifier = Modifier
                .clickable { onClose() }
                .padding(end = 16.dp, bottom = 16.dp)) {
                CancelText(text = stringResource(id = R.string.cancel))
            }
        })
}

@Composable
private fun CancelText(text: String) {
    Text(text = text, style = MaterialTheme.typography.h6, color = MaterialTheme.colors.primaryVariant)
}

@Composable
private fun Theme(theme: Boolean, onChangeTheme: (Boolean) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        MinorText(text = stringResource(id = if (theme) R.string.dark else R.string.light))

        IconToggleButton(
            checked = theme,
            onCheckedChange = onChangeTheme,
            modifier = Modifier.padding(start = 16.dp)
        ) {
            Crossfade(
                targetState = theme,
                animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
            ) {
                Icon(
                    imageVector = if (it) Icons.Default.DarkMode else Icons.Default.LightMode,
                    contentDescription = stringResource(id = R.string.change_theme)
                )
            }
        }
    }
}

@Composable
private fun MainText(@StringRes text: Int) {
    Text(
        text = stringResource(id = text),
        style = MaterialTheme.typography.subtitle2,
        color = MaterialTheme.colors.primaryVariant,
    )
}

@Composable
private fun MinorText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.subtitle2,
        color = MaterialTheme.colors.primary,
    )
}

@Composable
private fun SecondaryText(@StringRes text: Int) {
    Text(
        text = stringResource(id = text),
        style = MaterialTheme.typography.body2,
        color = MaterialTheme.colors.primary,
    )
}