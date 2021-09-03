@file:Suppress("UNUSED_EXPRESSION")

package com.example.filemanager.components

import android.annotation.SuppressLint
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.filemanager.RecyclerViewModel
import com.example.filemanager.item.FileItem
import java.text.SimpleDateFormat
import java.util.*

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "EXPERIMENTAL_IS_NOT_ENABLED")
class RecyclerView(private val viewModel: RecyclerViewModel) {

    private var fileName = ""

    @OptIn(
        ExperimentalFoundationApi::class,
        ExperimentalAnimationApi::class
    )
    @SuppressLint("NewApi")
    @Composable
    fun RecyclerView() {
        val scrollState = rememberLazyListState()

        Crossfade(targetState = viewModel.files.isEmpty(),
        animationSpec = tween(durationMillis = 1000, easing = LinearEasing)) { it ->
            if (it) EmptyFolder()
            else LazyColumn(state = scrollState, modifier = Modifier.fillMaxSize()) {
                items(viewModel.files) { item -> Item(item = item) }
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
    private fun Item(item: FileItem) {

        val select = viewModel.isSelectedItem(item.fileName)

        val backgroundColor by animateColorAsState(
            targetValue = if (select) Color.LightGray else Color.White,
            animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing)
        )

        Box(
            modifier = Modifier
                .combinedClickable(onClick = {
                    if (viewModel.selectionMode) viewModel.checkItem(item.fileName)
                    else (if (item.isDirectory) viewModel.addPath(item.fileName) else TODO())
                }, onLongClick = { TODO() })
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