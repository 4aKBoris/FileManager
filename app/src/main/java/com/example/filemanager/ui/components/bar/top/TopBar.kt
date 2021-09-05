@file:Suppress("EXPERIMENTAL_IS_NOT_ENABLED")

package com.example.filemanager.ui.components.topbar

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.filemanager.RecyclerViewModel
import kotlinx.coroutines.DelicateCoroutinesApi

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun TopBar(viewModel: RecyclerViewModel, openDrawer: () -> Unit) {

    TopAppBar(

        title = {
            Box(contentAlignment = Alignment.CenterEnd, modifier = Modifier.fillMaxSize()) {
                SearchTextField(viewModel = viewModel)
            }
        },

        navigationIcon = {
            Button(
                imageVector = Icons.Default.Menu,
                description = "Выбор диска",
                onClick = { openDrawer() })
        },

        actions = {
            Button(
                imageVector = Icons.Default.Sort,
                description = "Сортировка",
                onClick = viewModel::sortMenuChangeVisible
            )
            Button(
                imageVector = Icons.Default.Storage,
                description = "Путь к файлам",
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
private fun SearchTextField(viewModel: RecyclerViewModel) {

    var visible by remember { mutableStateOf(false) }

    val focusedLabelAndLeadingIconColor by animateColorAsState(
        targetValue = if (visible) MaterialTheme.colors.onBackground else MaterialTheme.colors.surface,
        animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessVeryLow)
    )

    val cursorAndIndicatorColor by animateColorAsState(
        targetValue = if (visible) MaterialTheme.colors.primary else MaterialTheme.colors.surface,
        animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessVeryLow)
    )

    val modifier = Modifier.animateContentSize(
        animationSpec = spring(stiffness = Spring.StiffnessVeryLow)
    ).padding(end = 10.dp)

    val (request, setRequest) = rememberSaveable { mutableStateOf("") }

    viewModel.request = request

    TextField(
        value = request,
        onValueChange = setRequest,
        trailingIcon = {
            Button(
                imageVector = Icons.Default.Cancel,
                description = "Прекратить поиск",
                onClick = { visible = false })
        },
        leadingIcon = {
            Button(
                imageVector = Icons.Default.Search,
                description = "Поиск",
                onClick = { visible = true }
            )
        },
        label = { Text(text = "Поиск") },
        singleLine = false,
        colors = TextFieldDefaults.textFieldColors(
            unfocusedLabelColor = MaterialTheme.colors.primary,
            focusedLabelColor = focusedLabelAndLeadingIconColor,
            backgroundColor = MaterialTheme.colors.surface,
            cursorColor = cursorAndIndicatorColor,
            focusedIndicatorColor = cursorAndIndicatorColor,
            unfocusedIndicatorColor = cursorAndIndicatorColor,
            disabledIndicatorColor = cursorAndIndicatorColor,
        ),
        modifier = if (visible) modifier.fillMaxSize() else modifier.width(40.dp),
        shape = MaterialTheme.shapes.large
    )
}

@Composable
private fun Button(imageVector: ImageVector, description: String, onClick: () -> Unit) {
    IconButton(
        onClick = { onClick() },
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = description,
            tint = MaterialTheme.colors.onBackground
        )
    }
}
