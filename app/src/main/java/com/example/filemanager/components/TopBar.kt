@file:Suppress("EXPERIMENTAL_IS_NOT_ENABLED")

package com.example.filemanager.components

import androidx.compose.animation.*
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.filemanager.R
import com.example.filemanager.RecyclerViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TopBar(private val viewModel: RecyclerViewModel) {

    @OptIn(DelicateCoroutinesApi::class)
    @Composable
    fun TopBar() {

        val textFieldVisible = remember { mutableStateOf(true) }

        val searchIconVisible = remember { mutableStateOf(false) }

        val titleTextVisible = remember { mutableStateOf(false) }

        TopAppBar(
            title = {

                fun TextFieldClose() {
                    viewModel.request.value = ""
                    GlobalScope.launch {
                        textFieldVisible.value = false
                        delay(1000)
                        searchIconVisible.value = false
                        titleTextVisible.value = true
                    }
                }

                SearchTextField(
                    textFieldVisible = textFieldVisible.value,
                    search = searchIconVisible.value,
                    onClick = { TextFieldClose() })

                TitleText(titleTextVisible.value)

            },

            actions = {

                fun SearchFun() {
                    GlobalScope.launch {
                        titleTextVisible.value = false
                        delay(1000)
                        searchIconVisible.value = true
                        textFieldVisible.value = true
                    }
                }

                ButtonSearch(search = searchIconVisible.value, onClick = { SearchFun() })

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
    private fun SearchTextField(textFieldVisible: Boolean, search: Boolean, onClick: () -> Unit) {

        val request = viewModel.request.observeAsState("")

        val focusedLabelAndLeadingIconColor by animateColorAsState(
            targetValue = if (textFieldVisible) Color.Black else colorResource(
                id = R.color.title_background
            ), animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow)
        )

        val cursorAndFocusedBorderColor by animateColorAsState(
            targetValue = if (textFieldVisible) colorResource(id = R.color.purple_500) else colorResource(
                id = R.color.title_background
            ), animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow)
        )

        AnimatedVisibility(
            visible = textFieldVisible,
            enter = slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth - 140 },
                animationSpec = tween(
                    durationMillis = 1000,
                    easing = LinearOutSlowInEasing
                )
            ),
            exit = slideOutHorizontally(
                targetOffsetX = { fullWidth -> fullWidth - 140 },
                animationSpec = tween(
                    durationMillis = 1000,
                    easing = LinearOutSlowInEasing
                )
            ),
            modifier = Modifier.animateContentSize()
        ) {
            TextField(
                value = request.value,
                onValueChange = { value -> viewModel.request.value = value },
                trailingIcon = {
                    IconButton(onClick = { onClick() }) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_cancel_search),
                            contentDescription = "Прекратить поиск"
                        )
                    }
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Поиск",
                        tint = Color.Black
                    )
                },
                label = { Text(text = "Поиск") },
                singleLine = false,
                colors = TextFieldDefaults.textFieldColors(
                    unfocusedLabelColor = colorResource(id = R.color.unfocused_label_color),
                    focusedLabelColor = focusedLabelAndLeadingIconColor,
                    backgroundColor = Color(0xFDCD7F32),
                    cursorColor = cursorAndFocusedBorderColor,
                    disabledPlaceholderColor = colorResource(id = R.color.title_background),
                    placeholderColor = colorResource(id = R.color.title_background),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                ),
                modifier = Modifier
                    .width(30.dp)
                    .animateContentSize()
            )
        }
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
                modifier = Modifier.fillMaxSize()
            ) { Text(text = "Файловый менеджер") }
        }
    }

    @Composable
    private fun ButtonSearch(search: Boolean, onClick: () -> Unit) {
        if (!search) {
            IconButton(onClick = { onClick() }) {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "Поиск",
                    tint = Color.Black
                )
            }
        }
    }

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    private fun ButtonSort() {

        val expanded = remember { mutableStateOf(false) }

        Box(Modifier.wrapContentSize(Alignment.TopEnd)) {
            IconButton(
                onClick = { expanded.value = true }) {
                Icon(
                    Icons.Default.Sort,
                    contentDescription = "Сортировка",
                    tint = Color.Black
                )
            }

            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false },
                offset = DpOffset.Zero,
                /*modifier = Modifier.animateContentSize(
                    animationSpec = spring(stiffness = Spring.StiffnessLow)*/
            ) {

                AnimatedVisibility(
                    visible = expanded.value,
                    enter = slideIn(
                        initialOffset = { size -> IntOffset(size.width, size.height) },
                        animationSpec = tween(
                            durationMillis = 3000,
                            easing = LinearOutSlowInEasing
                        )
                    ),
                    exit = slideOut(
                        targetOffset = { IntOffset.Zero },
                        animationSpec = tween(
                            durationMillis = 1000,
                            easing = LinearOutSlowInEasing
                        )
                    )
                ) {
                    Column() {

                        fun OnClick(number: Int) {
                            expanded.value = false
                            viewModel.sort.value = number
                            viewModel.upDown.value =
                                viewModel.upDown.value!!.not()
                        }

                        typesOfSort.forEachIndexed { index, s ->
                            SortMenuItem(
                                text = s,
                                number = index + 1,
                                onClick = { OnClick(index + 1) })

                            if (index + 1 != typesOfSort.size) Divider()
                        }
                    }
                }
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
            onClick = { viewModel.storage.value = !viewModel.storage.value!! },
        ) {
            Icon(
                imageVector = Icons.Default.Storage,
                contentDescription = "Путь к файлам",
                tint = colorStorage
            )
        }
    }

    @Composable
    private fun SortMenuItem(text: String, number: Int, onClick: () -> Unit) {

        val type = viewModel.sort.observeAsState()

        val upDown = viewModel.upDown.observeAsState()

        DropdownMenuItem(
            onClick = { onClick() },
            modifier = Modifier.background(if (type.value == number) Color.Magenta else Color.White)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (type.value == number) if (!upDown.value!!) IconForSortMenuItem(
                    icon = Icons.Default.ArrowDownward,
                    description = "Сортировка по убыванию"
                )
                else IconForSortMenuItem(
                    icon = Icons.Default.ArrowUpward,
                    description = "Сортировка по возрастанию"
                )
                Text(text = text)
            }
        }
    }

    @Composable
    private fun IconForSortMenuItem(icon: ImageVector, description: String) {
        Box(modifier = Modifier.padding(end = 10.dp)) {
            Icon(imageVector = icon, contentDescription = description)
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