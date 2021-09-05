@file:Suppress("UNUSED_EXPRESSION", "EXPERIMENTAL_IS_NOT_ENABLED")

package com.example.filemanager.ui.components.recyclerview

import android.annotation.SuppressLint
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.horizontalDrag
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.consumePositionChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.filemanager.RecyclerViewModel
import com.example.filemanager.item.FileItem
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.absoluteValue
import kotlin.math.roundToInt


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecyclerView(viewModel: RecyclerViewModel) {
    val scrollState = rememberLazyListState()

    Crossfade(
        targetState = viewModel.files.isEmpty(),
        animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
    ) {
        if (it) EmptyFolder()
        else LazyColumn(state = scrollState, modifier = Modifier.fillMaxSize()) {
            items(viewModel.files) { item ->
                Item(
                    item = item,
                    select = viewModel.isSelectedItem(item.fileName),
                    onClick = {
                        if (viewModel.selectionMode) viewModel.checkItem(item.fileName)
                        else (if (item.isDirectory) viewModel.addPath(item.fileName) else TODO())
                    }
                )
            }
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
            style = MaterialTheme.typography.h1,
            color = MaterialTheme.colors.primaryVariant
        )
    }
}

@ExperimentalFoundationApi
@Composable
private fun Item(
    item: FileItem,
    select: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (select) Color.LightGray else MaterialTheme.colors.background,
        animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing)
    )
    Box(
        modifier = Modifier
            .combinedClickable(onClick = { onClick() }, onLongClick = { TODO() })
            .background(Color.Cyan)
            .swipeToDismiss()
    ) { Item(item = item) }
}

@Composable
private fun Item(item: FileItem) {
    Column(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colors.background)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(20.dp, 5.dp)
        ) {
            FileIcon(drawable = painterResource(id = item.type))
            InfoText(item = item)
        }
        Divider(startIndent = 70.dp)
    }
}

@Composable
private fun InfoText(item: FileItem) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp), horizontalAlignment = Alignment.Start
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
        style = MaterialTheme.typography.subtitle2,
        color = MaterialTheme.colors.primaryVariant,
    )
}

@Composable
private fun SecondaryText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.body2,
        color = MaterialTheme.colors.primary,
    )
}

@Composable
private fun FileIcon(drawable: Painter) {
    Image(
        painter = drawable,
        contentDescription = "Тип файла",
        modifier = Modifier.size(50.dp)
    )
}

@SuppressLint("UnnecessaryComposedModifier")
private fun Modifier.swipeToDismiss(): Modifier = composed {
    val offsetX = remember { Animatable(0f) }
    val shape = remember { Animatable(0f) }
    pointerInput(Unit) {
        // Used to calculate a settling position of a fling animation.
        val decay = splineBasedDecay<Float>(this)
        // Wrap in a coroutine scope to use suspend functions for touch events and animation.
        coroutineScope {
            while (true) {
                // Wait for a touch down event.
                val pointerId = awaitPointerEventScope { awaitFirstDown().id }
                // Interrupt any ongoing animation.
                offsetX.stop()
                // Prepare for drag events and record velocity of a fling.
                val velocityTracker = VelocityTracker()
                // Wait for drag events.
                awaitPointerEventScope {
                    horizontalDrag(pointerId) { change ->
                        // Record the position after offset
                        val horizontalDragOffset = offsetX.value + change.positionChange().x
                        launch {
                            // Overwrite the `Animatable` value while the element is dragged.
                            offsetX.snapTo(horizontalDragOffset)
                        }
                        // Record the velocity of the drag.
                        velocityTracker.addPosition(change.uptimeMillis, change.position)
                        // Consume the gesture event, not passed to external
                        change.consumePositionChange()
                    }
                }
                // Dragging finished. Calculate the velocity of the fling.
                val velocity = velocityTracker.calculateVelocity().x
                // Calculate where the element eventually settles after the fling animation.
                val targetOffsetX = decay.calculateTargetValue(offsetX.value, velocity)
                // The animation should end as soon as it reaches these bounds.
                offsetX.updateBounds(
                    lowerBound = 0f,
                    upperBound = size.width.toFloat() / 2
                )

                shape.updateBounds(
                    lowerBound = 0f,
                    upperBound = 50f
                )
                launch {
                    if (targetOffsetX.absoluteValue <= size.width / 2) {
                        // Not enough velocity; Slide back to the default position.
                        offsetX.animateTo(targetValue = 0f, initialVelocity = velocity)
                        shape.animateTo(targetValue = 0f, initialVelocity = velocity)
                    } else {
                        // Enough velocity to slide away the element to the edge.
                        offsetX.animateDecay(velocity, decay)
                        shape.animateDecay(velocity, decay)
                        // The element was swiped away.
                    }
                }
            }
        }
    }
        // Apply the horizontal offset to the element.
        .offset { IntOffset(offsetX.value.roundToInt(), 0) }.clip(shape = RoundedCornerShape(shape.value))
}

@SuppressLint("WeekBasedYear")
private val formatter = SimpleDateFormat("dd.MM.YYYY", Locale.ENGLISH)