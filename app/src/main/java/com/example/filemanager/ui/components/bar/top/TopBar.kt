@file:Suppress("EXPERIMENTAL_IS_NOT_ENABLED")

package com.example.filemanager.ui.components.bar.top

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Storage
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.filemanager.R
import com.example.filemanager.view.model.FileManagerViewModel
import kotlinx.coroutines.DelicateCoroutinesApi

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun TopBar(viewModel: FileManagerViewModel, openDrawer: () -> Unit) {

    TopAppBar(

        title = {
            Box(contentAlignment = Alignment.CenterEnd, modifier = Modifier.fillMaxSize()) {
                SearchTextField(viewModel = viewModel)
            }
        },

        navigationIcon = {
            Button(
                imageVector = Icons.Default.Menu,
                description = stringResource(id = R.string.menu),
                onClick = openDrawer
            )
        },

        actions = {
            Button(
                imageVector = Icons.Default.Storage,
                description = stringResource(id = R.string.file_path),
                onClick = viewModel::storageMenuChangeVisible
            )
        },

        backgroundColor = MaterialTheme.colors.surface,
        contentColor = MaterialTheme.colors.surface,
        elevation = AppBarDefaults.TopAppBarElevation,
        modifier = Modifier.height(60.dp)
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun SearchTextField(viewModel: FileManagerViewModel) {

    var visible by remember { mutableStateOf(false) }

    val localFocusManager = LocalFocusManager.current
    val focusRequester = FocusRequester()

    val focusedLabelAndLeadingIconColor by animateColorAsState(
        targetValue = if (visible) MaterialTheme.colors.onBackground else MaterialTheme.colors.surface,
        animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessVeryLow)
    )

    val cursorAndIndicatorColor by animateColorAsState(
        targetValue = if (visible) MaterialTheme.colors.primary else MaterialTheme.colors.surface,
        animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessVeryLow)
    )

    val modifier = Modifier
        .animateContentSize(animationSpec = spring(stiffness = Spring.StiffnessVeryLow))
        .padding(end = 8.dp)
        .focusRequester(focusRequester)

    val (request, setRequest) = rememberSaveable { mutableStateOf("") }

    viewModel.onChangeRequest(request)

    TextField(
        value = request,
        onValueChange = setRequest,
        trailingIcon = {
            Button(
                imageVector = Icons.Default.Cancel,
                description = stringResource(id = R.string.stop_searching),
                onClick = {
                    visible = false
                    setRequest("")
                    localFocusManager.clearFocus()
                })
        },
        leadingIcon = {
            Button(
                imageVector = Icons.Default.Search,
                description = stringResource(id = R.string.search),
                onClick = {
                    visible = true
                    focusRequester.requestFocus()
                }
            )
        },
        label = { Text(text = stringResource(id = R.string.search)) },
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            textColor = MaterialTheme.colors.primaryVariant,
            unfocusedLabelColor = MaterialTheme.colors.primary,
            focusedLabelColor = focusedLabelAndLeadingIconColor,
            backgroundColor = MaterialTheme.colors.surface,
            cursorColor = cursorAndIndicatorColor,
            focusedIndicatorColor = cursorAndIndicatorColor,
            unfocusedIndicatorColor = cursorAndIndicatorColor,
            disabledIndicatorColor = cursorAndIndicatorColor,
        ),
        modifier = if (visible) modifier.fillMaxSize() else modifier.width(40.dp),
        shape = MaterialTheme.shapes.large,
        keyboardOptions = KeyboardOptions(
            autoCorrect = false,
            imeAction = ImeAction.Search,
            keyboardType = KeyboardType.Text,
            capitalization = KeyboardCapitalization.None
        ),
        keyboardActions = KeyboardActions(
            onSearch = { localFocusManager.clearFocus() }
        )
    )
}

@Composable
private fun Button(imageVector: ImageVector, description: String, onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = description,
            tint = Color.DarkGray
        )
    }
}
