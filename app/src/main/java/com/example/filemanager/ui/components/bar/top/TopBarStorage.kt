@file:Suppress("EXPERIMENTAL_IS_NOT_ENABLED")

package com.example.filemanager.ui.components.bar.top

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material.icons.filled.SdCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.filemanager.constants.STORAGE
import com.example.filemanager.view.model.FileManagerViewModel
import kotlinx.coroutines.launch

private const val ANIMATION_DURATION = 500

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TopBarStorage(viewModel: FileManagerViewModel) {

    AnimatedVisibility(
        visible = viewModel.storageMenuVisible,
        enter = expandIn(
            expandFrom = Alignment.TopStart,
            animationSpec = tween(
                durationMillis = ANIMATION_DURATION,
                easing = LinearOutSlowInEasing
            )
        ),
        exit = shrinkOut(
            shrinkTowards = Alignment.TopStart,
            animationSpec = tween(
                durationMillis = ANIMATION_DURATION,
                easing = LinearOutSlowInEasing
            )
        )
    ) { PathToFiles(viewModel = viewModel) }
}

@Composable
private fun PathToFiles(viewModel: FileManagerViewModel) {

    val path = viewModel.path

    val newPath = path.removePrefix(STORAGE).split('/').filter { it.isNotBlank() }

    val state = rememberLazyListState()

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
            .border(
                width = 2.dp,
                brush = Brush.verticalGradient(listOf(Color.White, Color.Black)),
                shape = RoundedCornerShape(bottomEnd = 16.dp, bottomStart = 16.dp)
            )
    ) {
        LazyRow(
            state = state,
            verticalAlignment = Alignment.CenterVertically,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            item {
                MenuIcon(
                    imageVector = Icons.Default.SdCard,
                    modifier = Modifier.clickable { viewModel.newPath(STORAGE) })
            }
            newPath.fold(STORAGE) { total, next ->

                val p = "$total/$next"

                item {
                    MenuIcon(imageVector = Icons.Default.NavigateNext)
                }

                item {
                    PathText(path = next, modifier = Modifier.clickable { viewModel.newPath(p) })
                }

                scope.launch {
                    state.animateScrollBy(
                        500f,
                        animationSpec = spring(stiffness = Spring.StiffnessVeryLow)
                    )
                }

                p
            }
        }
        Divider()
    }
}

@Composable
private fun PathText(path: String, modifier: Modifier) {
    Text(
        text = path,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.subtitle1,
        color = MaterialTheme.colors.primaryVariant,
        modifier = modifier
    )
}

@Composable
private fun MenuIcon(imageVector: ImageVector, modifier: Modifier = Modifier) {
    Icon(
        imageVector = imageVector,
        contentDescription = "",
        tint = Color.DarkGray,
        modifier = modifier.size(36.dp)
    )
}

