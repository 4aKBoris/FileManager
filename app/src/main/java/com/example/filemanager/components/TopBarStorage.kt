@file:Suppress("EXPERIMENTAL_IS_NOT_ENABLED")

package com.example.filemanager.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.*
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.filemanager.R
import com.example.filemanager.RecyclerViewModel
import com.example.filemanager.constants.STORAGE

class TopBarStorage(private val viewModel: RecyclerViewModel) {

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    fun TopBarStorage() {

        AnimatedVisibility(
            visible = viewModel.storageMenuVisible,
            enter = slideInVertically(
                initialOffsetY = { fullHeight -> -fullHeight },
                animationSpec = tween(
                    durationMillis = 1000,
                    easing = LinearOutSlowInEasing
                )
            ),
            exit = slideOutVertically(
                targetOffsetY = { fullHeight -> -fullHeight },
                animationSpec = tween(
                    durationMillis = 1000,
                    easing = LinearOutSlowInEasing
                )
            )
        ) { Path() }
    }

    @Composable
    private fun Path() {

        val dp: Dp by animateDpAsState(
            targetValue = if (viewModel.storageMenuVisible) 0.dp else (-10).dp,
            animationSpec = spring(Spring.DampingRatioNoBouncy, Spring.StiffnessLow)
        )

        val angle: Float by animateFloatAsState(
            targetValue = if (viewModel.storageMenuVisible) 180F else 0F,
            animationSpec = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            ),
            finishedListener = {}
        )

        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth()
        ) {
            Box(
                Modifier
                    .height(50.dp)
                    .padding(start = 20.dp)
                    .fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .horizontalScroll(ScrollState(0)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val p = viewModel.path.removePrefix(STORAGE).split('/').filterNot { it.isBlank() }
                    Image(
                        painter = painterResource(id = R.drawable.ic_storage),
                        contentDescription = "",
                        modifier = Modifier
                            .size(40.dp)
                            .clickable { viewModel.path = STORAGE }
                    )
                    if (p.isNotEmpty()) p.forEachIndexed { index, s ->
                        Next()
                        PointPath(
                            s,
                            p.take(index + 1).fold(STORAGE) { total, next -> "$total/$next" })
                    }
                }
                IconButton(onClick = { viewModel.storageMenuChangeVisible() },
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .absoluteOffset(y = dp)
                        .rotate(angle)) {
                    Icon(
                        imageVector = Icons.Default.ArrowDownward,
                        contentDescription = "Путь к файлам",
                    )
                }
            }
            Box(
                modifier = Modifier
                    .height(1.dp)
                    .background(Color.Black)
                    .fillMaxWidth()
            )
        }
    }

    @Composable
    private fun PointPath(name: String, path: String) {
        Text(
            text = name,
            fontSize = 20.sp,
            fontStyle = FontStyle.Italic,
            color = colorResource(id = R.color.gray_point_path),
            modifier = Modifier.clickable { viewModel.path = path })
    }

    @Composable
    private fun Next() {
        Image(
            painter = painterResource(id = R.drawable.ic_next),
            contentDescription = "",
            modifier = Modifier.size(30.dp)
        )
    }

}