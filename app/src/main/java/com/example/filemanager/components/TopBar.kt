@file:Suppress("EXPERIMENTAL_IS_NOT_ENABLED")

package com.example.filemanager.components

import androidx.compose.animation.*
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.Storage
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.filemanager.R
import com.example.filemanager.RecyclerViewModel
import kotlinx.coroutines.DelicateCoroutinesApi

class TopBar(private val viewModel: RecyclerViewModel) {

    @OptIn(DelicateCoroutinesApi::class)
    @Composable
    fun TopBar() {

        TopAppBar(

            title = {
                Box(contentAlignment = Alignment.CenterEnd, modifier = Modifier.fillMaxSize()) {
                    SearchTextField()
                }
            },

            navigationIcon = {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Выбор диска",
                        tint = Color.Black
                    )
                }
            },

            actions = {
                ButtonSort()
                ButtonStorage()
            },

            backgroundColor = colorResource(id = R.color.title_background),
            elevation = AppBarDefaults.TopAppBarElevation,
            modifier = Modifier.height(60.dp)
        )
    }

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    private fun SearchTextField() {

        val visible = remember { mutableStateOf(false) }

        val focusedLabelAndLeadingIconColor by animateColorAsState(
            targetValue = if (visible.value) Color.Black else colorResource(
                id = R.color.title_background
            ), animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessVeryLow)
        )

        val cursorAndIndicatorColor by animateColorAsState(
            targetValue = if (visible.value) colorResource(id = R.color.purple_500) else colorResource(
                id = R.color.title_background
            ), animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessVeryLow)
        )

        val modifier = Modifier.animateContentSize(
            animationSpec = spring(stiffness = Spring.StiffnessVeryLow)
        )

        val (request, setRequest) = rememberSaveable { mutableStateOf("") }

        viewModel.request = request

        TextField(
            value = request,
            onValueChange = setRequest,
            trailingIcon = {
                IconButton(onClick = { visible.value = false }) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_cancel_search),
                        contentDescription = "Прекратить поиск"
                    )
                }
            },
            leadingIcon = {
                IconButton(onClick = { visible.value = true }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Поиск",
                        tint = Color.Black
                    )
                }
            },
            label = { Text(text = "Поиск") },
            singleLine = false,
            colors = TextFieldDefaults.textFieldColors(
                unfocusedLabelColor = colorResource(id = R.color.unfocused_label_color),
                focusedLabelColor = focusedLabelAndLeadingIconColor,
                backgroundColor = Color(0xFDCD7F32),
                cursorColor = cursorAndIndicatorColor,
                focusedIndicatorColor = cursorAndIndicatorColor,
                unfocusedIndicatorColor = cursorAndIndicatorColor,
                disabledIndicatorColor = cursorAndIndicatorColor,
            ),
            modifier = if (visible.value) modifier.fillMaxSize() else modifier.width(50.dp)

        )
    }

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    private fun TitleText(titleVisible: Boolean) {
        AnimatedVisibility(
            visible = titleVisible,
            enter = slideInVertically(
                initialOffsetY = { fullWidth -> -fullWidth },
                animationSpec = tween(
                    durationMillis = 1000,
                    easing = LinearOutSlowInEasing
                )
            ),
            exit = slideOutVertically(
                targetOffsetY = { fullWidth -> -fullWidth },
                animationSpec = tween(
                    durationMillis = 1000,
                    easing = LinearOutSlowInEasing
                )
            )
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.wrapContentWidth()
            ) { Text(text = "Файловый менеджер") }
        }
    }

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    private fun ButtonSort() {

        Box(Modifier.wrapContentSize(Alignment.TopEnd)) {
            IconButton(
                onClick = { viewModel.sortMenuChangeVisible() }) {
                Icon(
                    Icons.Default.Sort,
                    contentDescription = "Сортировка",
                    tint = Color.Black
                )
            }
        }
    }

    @Composable
    private fun ButtonStorage() {

        val colorStorage: Color by animateColorAsState(
            targetValue = Color.Black,
            animationSpec = spring(
                Spring.DampingRatioNoBouncy,
                Spring.StiffnessLow
            )
        )

        IconButton(
            onClick = { viewModel.storageMenuChangeVisible() },
        ) {
            Icon(
                imageVector = Icons.Default.Storage,
                contentDescription = "Путь к файлам",
                tint = colorStorage
            )
        }
    }

    companion object {

        private val typesOfSort = listOf(
            "По алфавиту",
            "По размеру",
            "По дате создания",
            "По дате изменения",
            "По дате последнего открытия"
        )
    }

}