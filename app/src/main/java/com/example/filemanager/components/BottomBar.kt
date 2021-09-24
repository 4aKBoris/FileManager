@file:Suppress("EXPERIMENTAL_IS_NOT_ENABLED", "PreviewMustBeTopLevelFunction")

package com.example.filemanager.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.*
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DriveFileMove
import androidx.compose.material.icons.filled.FileCopy
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.example.filemanager.RecyclerViewModel

class BottomBar(private val viewModel: RecyclerViewModel) {

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    fun BottomBar() {

        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
        ) {

            fun onClick(number: Int) {
                viewModel.action.value = number
            }

            BottomBarButton(
                icon = Icons.Default.Check,
                description = "Подтвердить",
                number = 0,
                onClick = { onClick(1) })

            BottomBarButton(
                icon = Icons.Default.FileCopy,
                description = "Копировать",
                number = 1,
                onClick = { onClick(2) })

            BottomBarButton(
                icon = Icons.Default.DriveFileMove,
                description = "Переместить",
                number = 2,
                onClick = { onClick(3) })

            BottomBarButton(
                icon = Icons.Default.Delete,
                description = "Удалить",
                number = 3,
                onClick = { onClick(4) })

            BottomBarButton(
                icon = Icons.Rounded.Cancel,
                description = "Отмена",
                number = 4,
                onClick = { viewModel.select.value = false })

        }
    }

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    private fun BottomBarButton(icon: ImageVector, description: String, number: Int, onClick: () -> Unit) {

        val select = viewModel.select.observeAsState()

        val time = number * 5000

        val infiniteTransition = rememberInfiniteTransition()

        var size by remember { mutableStateOf(Size.Zero)}

        val alpha by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 2500, easing = LinearOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            )
        )

        val borderFloat by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 0f,
            animationSpec = infiniteRepeatable(
                animation = keyframes {
                    durationMillis = 50000
                    0f at time with LinearOutSlowInEasing
                    1f at time + 3000 with LinearOutSlowInEasing
                    1f at 45000 - time with LinearOutSlowInEasing
                    0f at 45000 - time + 3000 with LinearOutSlowInEasing
                },
                repeatMode = RepeatMode.Restart
            )
        )

        val scaleFloat by infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = keyframes {
                    durationMillis = 50000
                    1f at time + 3000 with LinearOutSlowInEasing
                    0.5f at time + 3500 with FastOutLinearInEasing
                    1.7f at time + 4500 with LinearOutSlowInEasing
                    1f at time + 5000 with LinearOutSlowInEasing
                    1f at 48000 - time with LinearOutSlowInEasing
                    0.5f at 48500 - time with FastOutLinearInEasing
                    1.7f at 49500 - time with LinearOutSlowInEasing
                    1f at 50000 - time with LinearOutSlowInEasing
                },
                repeatMode = RepeatMode.Restart
            )
        )

        AnimatedVisibility(
            visible = select.value!!,
            enter = slideInHorizontally(
                initialOffsetX = { height -> -3 * (number + 1) * height },
                animationSpec = tween(
                    durationMillis = 2000,
                    easing = LinearOutSlowInEasing
                )
            ),
            exit = slideOutHorizontally(
                targetOffsetX = { height -> -3 * (number + 1) * height },
                animationSpec = tween(
                    durationMillis = 2000,
                    easing = FastOutLinearInEasing
                )
            )
        ) {

            Box(contentAlignment = Alignment.Center) {
                IconButton(
                    onClick = { onClick() },
                    modifier = Modifier
                        .padding(vertical = 5.dp)
                        .background(
                            color = Color.LightGray.copy(alpha),
                            shape = RoundedCornerShape(100)
                        ).onGloballyPositioned { coordinates ->
                            size = coordinates.size.toSize()
                        }
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = description,
                        modifier = Modifier.scale(scaleFloat)
                    )
                }

                val radius = size.width / 2

                Canvas(modifier = Modifier.padding(vertical = 5.dp), onDraw = {
                    drawArc(
                        color = Color.Black,
                        startAngle = 0f,
                        sweepAngle = 360f * borderFloat,
                        useCenter = false,
                        size = Size(
                            radius * 2,
                            radius * 2
                        ),
                        topLeft = Offset(-radius, -radius),
                        style = Stroke(5.0f)
                    )
                })
            }
        }

    }

    @Preview
    @Composable
    fun TestBottomBar() {
        BottomBar()
    }
}