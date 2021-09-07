@file:Suppress("EXPERIMENTAL_IS_NOT_ENABLED")

package com.example.filemanager.ui.components.recyclerview

import android.annotation.SuppressLint
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.consumePositionChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.filemanager.item.FileItem
import com.example.filemanager.ui.theme.cardCollapsedBackgroundColor
import com.example.filemanager.ui.theme.cardExpandedBackgroundColor
import kotlin.math.roundToInt

const val ANIMATION_DURATION = 500

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun DraggableFile(
    item: FileItem,
    info: Boolean,
    edit: Boolean,
    selected: Boolean,
    cardHeight: Dp,
    isRevealed: Boolean,
    cardOffset: Float,
    onClose: () -> Unit,
    onCancelEdit: () -> Unit,
    onEdit: () -> Unit,
    onExpand: () -> Unit,
    onCollapse: () -> Unit,
    onItemClick: () -> Unit,
    onItemLongClick: () -> Unit
) {
    val offsetX = remember { mutableStateOf(0f) }
    val transitionState = remember {
        MutableTransitionState(isRevealed).apply {
            targetState = !isRevealed
        }
    }
    val transition = updateTransition(transitionState, "cardTransition")
    val cardBgColor by transition.animateColor(
        label = "cardBgColorTransition",
        transitionSpec = { tween(durationMillis = ANIMATION_DURATION) },
        targetValueByState = {
            if (selected) (if (isRevealed) Color.Cyan else Color.Magenta)
            else (if (isRevealed) cardExpandedBackgroundColor else cardCollapsedBackgroundColor)
        }
    )
    val offsetTransition by transition.animateFloat(
        label = "cardOffsetTransition",
        transitionSpec = { tween(durationMillis = ANIMATION_DURATION) },
        targetValueByState = { if (isRevealed) cardOffset - offsetX.value else -offsetX.value },

        )
    val cardElevation by transition.animateDp(
        label = "cardElevation",
        transitionSpec = { tween(durationMillis = ANIMATION_DURATION) },
        targetValueByState = { if (isRevealed) 40.dp else 2.dp }
    )

    val mod = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp)

    val modifier = mod
        .offset { IntOffset((offsetX.value + offsetTransition).roundToInt(), 0) }
        .pointerInput(Unit) {
            detectHorizontalDragGestures { change, dragAmount ->
                val original = Offset(offsetX.value, 0f)
                val summed = original + Offset(x = dragAmount, y = 0f)
                val newValue = Offset(x = summed.x.coerceIn(0f, cardOffset), y = 0f)
                if (newValue.x >= 10) {
                    onExpand()
                    return@detectHorizontalDragGestures
                } else if (newValue.x <= 0) {
                    onCollapse()
                    return@detectHorizontalDragGestures
                }
                change.consumePositionChange()
                offsetX.value = newValue.x
            }
        }

    Card(
        modifier = modifier.combinedClickable(
            onClick = onItemClick,
            onLongClick = onItemLongClick
        ),
        backgroundColor = cardBgColor,
        shape = RoundedCornerShape(5.dp),
        elevation = cardElevation,
        content = {
            FileTitle(
                item = item,
                info = info,
                edit = edit,
                onClose = onClose,
                onCancelEdit = onCancelEdit,
                onEdit = onEdit,
            )
        }
    )
}