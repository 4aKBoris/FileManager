@file:Suppress("EXPERIMENTAL_IS_NOT_ENABLED", "PreviewMustBeTopLevelFunction")

package com.example.filemanager.ui.components.bar

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.ChangeCircle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.example.filemanager.RecyclerViewModel
import com.example.filemanager.ui.components.bar.bottom.BottomBarButtons

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BottomBar(viewModel: RecyclerViewModel, displayWidth: Dp) {

    val infiniteTransition = rememberInfiniteTransition()
    val borderFloat = FloatArray(5)
    val scaleFloat = FloatArray(5)
    for (i in 0 until 5) {
        val time = 5000 * i
        val sF by infiniteTransition.animateFloat(
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
        scaleFloat[i] = sF
        val bF by infiniteTransition.animateFloat(
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
        borderFloat[i] = bF
    }

    val selectMode = viewModel.selectionMode

    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.CenterEnd
    ) {
        CircleButton(
            selectMode = selectMode,
            borderFloat = borderFloat[BottomBarButtons.CONFIRM.number],
            scaleFloat = scaleFloat[BottomBarButtons.CONFIRM.number],
            displayWidth = displayWidth,
            properties = BottomBarButtons.CONFIRM,
            onClick = {})
        CircleButton(
            selectMode = selectMode,
            borderFloat = borderFloat[BottomBarButtons.COPY.number],
            scaleFloat = scaleFloat[BottomBarButtons.COPY.number],
            displayWidth = displayWidth,
            properties = BottomBarButtons.COPY,
            onClick = {})
        CircleButton(
            selectMode = selectMode,
            borderFloat = borderFloat[BottomBarButtons.MOVE.number],
            scaleFloat = scaleFloat[BottomBarButtons.MOVE.number],
            displayWidth = displayWidth,
            properties = BottomBarButtons.MOVE,
            onClick = {})
        CircleButton(
            selectMode = selectMode,
            borderFloat = borderFloat[BottomBarButtons.DELETE.number],
            scaleFloat = scaleFloat[BottomBarButtons.DELETE.number],
            displayWidth = displayWidth,
            properties = BottomBarButtons.DELETE,
            onClick = {})
        CircleButton(
            borderFloat = borderFloat[BottomBarButtons.SELECT.number],
            displayWidth = displayWidth,
            properties = BottomBarButtons.SELECT,
            onClick = viewModel::swapSelectionMode
        ) {
            ButtonIcon(
                selectMode = selectMode,
                scaleFloat = scaleFloat[BottomBarButtons.SELECT.number]
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun CircleButton(
    selectMode: Boolean,
    borderFloat: Float,
    scaleFloat: Float,
    displayWidth: Dp,
    properties: BottomBarButtons,
    onClick: () -> Unit,
) {
    AnimatedVisibility(
        visible = selectMode,
        enter = slideInHorizontally(
            initialOffsetX = { width -> width * properties.number },
            animationSpec = tween(
                durationMillis = 1000,
                easing = LinearOutSlowInEasing
            )
        ),
        exit = slideOutHorizontally(
            targetOffsetX = { width -> width * properties.number },
            animationSpec = tween(
                durationMillis = 1000,
                easing = LinearOutSlowInEasing
            )
        )
    ) {
        CircleButton(
            borderFloat = borderFloat,
            displayWidth = displayWidth,
            properties = properties,
            onClick = onClick
        ) {
            ButtonIcon(properties = properties, scaleFloat = scaleFloat)
        }
    }
}

@Composable
private fun CircleButton(
    borderFloat: Float,
    displayWidth: Dp,
    properties: BottomBarButtons,
    onClick: () -> Unit,
    icon: @Composable () -> Unit
) {

    var size by remember { mutableStateOf(Size.Zero) }

    val infiniteTransition = rememberInfiniteTransition()

    val alpha by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2500, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth(fraction = 0.2f)
            .offset(x = -(displayWidth / 5 * (properties.number)))
    ) {
        IconButton(
            onClick = { onClick() },
            modifier = Modifier
                .padding(vertical = 5.dp)
                .background(
                    color = MaterialTheme.colors.primary.copy(alpha),
                    shape = RoundedCornerShape(100)
                )
                .onGloballyPositioned { coordinates ->
                    size = coordinates.size.toSize()
                }
        ) { icon() }

        ButtonBorder(radius = size.width / 2, borderFloat = borderFloat)
    }
}

@Composable
private fun ButtonBorder(radius: Float, borderFloat: Float) {

    val color = MaterialTheme.colors.onBackground

    Canvas(modifier = Modifier.padding(vertical = 5.dp), onDraw = {
        drawArc(
            color = color,
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

@Composable
private fun ButtonIcon(properties: BottomBarButtons, scaleFloat: Float) {
    Icon(
        imageVector = properties.icon,
        contentDescription = properties.description,
        modifier = Modifier.scale(scaleFloat)
    )
}

@Composable
private fun ButtonIcon(selectMode: Boolean, scaleFloat: Float) {
    Crossfade(targetState = if (selectMode) Icons.Default.Cancel else Icons.Default.ChangeCircle) {
        Icon(
            imageVector = it,
            contentDescription = "Режим выделения",
            modifier = Modifier.scale(scaleFloat)
        )
    }
}