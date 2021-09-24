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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.filemanager.BuildConfig
import com.example.filemanager.R
import com.example.filemanager.ui.components.textfields.*
import com.example.filemanager.ui.theme.DarkBlack
import com.example.filemanager.view.model.FileManagerViewModel

@Composable
fun Settings(viewModel: FileManagerViewModel) {

    val state = rememberLazyListState()

    var stateAlertDialog by remember { mutableStateOf(false) }

    var type by remember { mutableStateOf(SortingSections.Grouping) }

    Box(modifier = Modifier.fillMaxSize()) {

        LazyColumn(state = state, contentPadding = PaddingValues(bottom = 24.dp)) {
            item {
                Group(title = R.string.decoration) {
                    Item(text = R.string.theme) {
                        Theme(theme = viewModel.theme, onChangeTheme = viewModel::swapTheme)
                    }
                }
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
            }
            item {
                Group(title = R.string.advanced_settings) {
                    Item(
                        text = R.string.show_hidden_files_and_folders,
                        modifier = Modifier.fillMaxWidth(fraction = 0.8f)
                    ) {
                        ShowHidden(
                            hidden = viewModel.hidden,
                            onChange = viewModel::onChangeHidden
                        )
                    }
                }
            }
            item {
                Group(title = R.string.about_application, divider = false) {
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
private fun Group(@StringRes title: Int, divider: Boolean = true, body: @Composable () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Column(
            modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 8.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp), contentAlignment = Alignment.CenterStart
            ) { TitleText(text = stringResource(id = title)) }
            body()
        }
        if (divider) {
            Spacer(modifier = Modifier.height(height = 8.dp))
            Divider()
        }
    }
}

@Composable
private fun Item(
    @StringRes text: Int,
    modifier: Modifier = Modifier,
    body: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = stringResource(id = text), modifier = modifier)
        body()
    }
}

@Composable
private fun Item(@StringRes title: Int, @StringRes text: Int, onClick: () -> Unit) {
    IconButton(
        onClick = onClick, modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            MainText(text = stringResource(id = title))
            SecondaryText(text = stringResource(id = text))
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
                TitleText(text = stringResource(id = title))
            }
        },
        confirmButton = {
            Box(
                modifier = Modifier
                    .clickable(onClick = onClose)
                    .padding(end = 16.dp, bottom = 16.dp)
            ) {
                CancelText(text = stringResource(id = R.string.cancel))
            }
        })
}

@Composable
private fun ShowHidden(hidden: Boolean, onChange: (Boolean) -> Unit) {
    Checkbox(
        checked = hidden,
        onCheckedChange = onChange,
        modifier = Modifier.padding(start = 8.dp, end = 16.dp),
        colors = CheckboxDefaults.colors(
            checkedColor = MaterialTheme.colors.secondaryVariant,
            uncheckedColor = MaterialTheme.colors.primaryVariant,
            checkmarkColor = DarkBlack
        )
    )
}

@Preview
@Composable
private fun TestShowHidden() {

    val (hidden, setHidden) = remember { mutableStateOf(false) }

    ShowHidden(hidden = hidden, onChange = setHidden)
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
