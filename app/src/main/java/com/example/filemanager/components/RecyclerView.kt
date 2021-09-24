@file:Suppress("UNUSED_EXPRESSION")

package com.example.filemanager.components

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.filemanager.R
import com.example.filemanager.RecyclerViewModel
import com.example.filemanager.constants.STORAGE
import com.example.filemanager.extensions.convertToFileItem
import com.example.filemanager.extensions.sortByCondition
import com.example.filemanager.item.FileItem
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "EXPERIMENTAL_IS_NOT_ENABLED")
class RecyclerView(private val viewModel: RecyclerViewModel) {

    private var fileName = ""

    @OptIn(
        ExperimentalFoundationApi::class,
        androidx.compose.animation.ExperimentalAnimationApi::class
    )
    @SuppressLint("NewApi")
    @Composable
    fun RecyclerView() {
        val scrollState = rememberLazyListState()

        var items = listOf<FileItem>()

        val path by viewModel.path.observeAsState(STORAGE)

        val sortType = viewModel.typeSort.observeAsState(1)

        val sort = viewModel.sort.observeAsState(0)

        val upDown = viewModel.upDown.observeAsState(false)

        val emptyFolder = remember { mutableStateOf(false) }

        val selectedItems = remember { mutableStateListOf<String>() }

        val dialog = viewModel.dialog.observeAsState()

        val dialogSelect = viewModel.dialogSelect.observeAsState()

        val search = viewModel.request.observeAsState("")

        val storage = viewModel.storage.observeAsState(false)

        fun swap(name: String) {
            if (selectedItems.contains(name)) selectedItems.remove(name) else selectedItems.add(name)
        }

        viewModel.select.value = selectedItems.isNotEmpty()

        val files = File(path).listFiles()
        emptyFolder.value = files.isNullOrEmpty()

        if (!emptyFolder.value) items = files.convertToFileItem()

        viewModel.path.observe(LocalLifecycleOwner.current) {
            //selectedItems.clear()
            println("viewModel.path.observe")
            println(it)
        }

        items = items.sortByCondition(sortType.value, sort.value, upDown.value)

        items = items.filter { it.fileName.contains(search.value, true) }

        LazyColumn(state = scrollState, modifier = Modifier.fillMaxSize()) {

            stickyHeader {
                Column(
                    modifier = Modifier.animateContentSize(
                        animationSpec = spring(
                            Spring.DampingRatioNoBouncy,
                            Spring.StiffnessLow
                        ),
                        finishedListener = null
                    )
                ) { if (dialog.value!!) Select() }
            }

            if (!emptyFolder.value) items(items) {

                val name = it.fileName

                Item(
                    item = it,
                    select = selectedItems.contains(name),
                    onClick = {
                        if (it.isDirectory) viewModel.path.value = "$path/$name" else swap(name)
                    },
                    onLongClick = {
                        if (selectedItems.contains(name)) selectedItems.remove(name)
                        else {
                            viewModel.dialog.value = true
                            fileName = name
                        }
                    })
            }
            else item { EmptyFolder() }

        }

        if (dialog.value!!) {
            viewModel.dialogSelect.value = 0
        } else when (dialogSelect.value) {
            1 -> selectedItems.addAll(items.takeWhile { it.fileName != fileName }
                .takeLastWhile { !selectedItems.contains(it.fileName) }.map { it.fileName }
                .plus(fileName))
            2 -> selectedItems.addAll(items.takeLastWhile { it.fileName != fileName }
                .takeWhile { !selectedItems.contains(it.fileName) }.map { it.fileName }
                .plus(fileName))
            3 -> swap(fileName)
            else -> {
            }
        }

    }

    @Composable
    fun EmptyFolder() {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Пустая папка",
                style = TextStyle(
                    fontSize = 40.sp,
                    fontWeight = FontWeight.W800,
                    shadow = Shadow(
                        Color.Red,
                        offset = Offset.Zero,
                        blurRadius = Float.fromBits(5)
                    ),
                    textDecoration = TextDecoration.Underline
                )
            )
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    private fun Item(
        item: FileItem,
        select: Boolean,
        onClick: () -> Unit,
        onLongClick: () -> Unit
    ) {

        val backgroundColor by animateColorAsState(targetValue = if (select) Color.LightGray else Color.White,
        animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing))

        Box(
            modifier = Modifier
                .combinedClickable(onClick = { onClick() }, onLongClick = { onLongClick() })
                .background(backgroundColor)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(20.dp, 5.dp)
            ) {
                FileIcon(drawable = painterResource(id = item.type))
                InfoText(item = item)
            }
        }
    }


    @Composable
    fun Select() {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                ButtonSelect(
                    name = "Выделение вверх",
                    onClick = {
                        viewModel.dialogSelect.value = 1
                        viewModel.dialog.value = false
                    },
                    drawable = painterResource(id = R.drawable.ic_arrow_upward)
                )
                ButtonSelect(
                    name = "Выделение данного элемента",
                    onClick = {
                        viewModel.dialogSelect.value = 2
                        viewModel.dialog.value = false
                    },
                    drawable = painterResource(id = R.drawable.ic_add)
                )
                ButtonSelect(
                    name = "Выделение вниз",
                    onClick = {
                        viewModel.dialogSelect.value = 3
                        viewModel.dialog.value = false
                    },
                    drawable = painterResource(id = R.drawable.ic_arrow_downward)
                )
                ButtonSelect(
                    name = "Закрыть",
                    onClick = { viewModel.dialog.value = false },
                    drawable = painterResource(id = R.drawable.ic_cancel)
                )
            }
        }
    }

    @Composable
    fun ButtonSelect(name: String, onClick: () -> Unit, drawable: Painter) {
        Box(
            modifier = Modifier
                .clickable { onClick() }
                .background(Color.LightGray)
                .height(50.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = drawable,
                contentDescription = name,
            )
        }
    }

    @Composable
    private fun InfoText(item: FileItem) {
        Column(
            modifier = Modifier
                .padding(20.dp, 5.dp)
                .fillMaxWidth(), horizontalAlignment = Alignment.Start
        ) {
            MainText(fileName = item.fileName)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SecondaryText(text = item.size)
                SecondaryText(text = formatter.format(item.dateChange))
            }
        }
    }

    @Composable
    private fun MainText(fileName: String) {
        Text(
            text = fileName,
            fontSize = 18.sp,
            fontStyle = FontStyle.Italic,
            fontFamily = FontFamily.SansSerif
        )
    }

    @Composable
    private fun SecondaryText(text: String) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontStyle = FontStyle.Normal,
            fontFamily = FontFamily.SansSerif,
            color = Color.Gray
        )
    }

    @Composable
    private fun FileIcon(drawable: Painter) {
        Image(
            painter = drawable,
            contentDescription = "Android Logo",
            modifier = Modifier.size(50.dp)
        )
    }

    companion object {

        @SuppressLint("WeekBasedYear")
        private val formatter = SimpleDateFormat("dd.MM.YYYY", Locale.ENGLISH)
    }
}