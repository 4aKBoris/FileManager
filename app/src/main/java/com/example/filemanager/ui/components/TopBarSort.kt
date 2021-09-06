@file:Suppress("EXPERIMENTAL_IS_NOT_ENABLED")

package com.example.filemanager.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.example.filemanager.R
import com.example.filemanager.RecyclerViewModel
import com.example.filemanager.sort.SortingOrder
import com.example.filemanager.sort.SortingType

class TopBarSort(private val viewModel: RecyclerViewModel, private val widthWindow: Float) {

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    fun TopBarSort(modifier: Modifier) {

        Box(modifier = modifier) {

            AnimatedVisibility(
                visible = viewModel.sortMenuVisible,
                enter = expandHorizontally(
                    expandFrom = Alignment.CenterHorizontally,
                    animationSpec = tween(
                        durationMillis = 1000,
                        easing = FastOutLinearInEasing
                    )
                ),
                exit = shrinkHorizontally(
                    shrinkTowards = Alignment.CenterHorizontally,
                    animationSpec = tween(
                        durationMillis = 1000,
                        easing = LinearOutSlowInEasing
                    )
                )
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    fun OnClick(number: Int) {
                        viewModel.sortingType.value = SortingType.values()[number]
                        viewModel.swapSortingOrder()
                    }

                    for (i in 1..5) MenuSortItem(
                        number = i - 1,
                        onClick = { OnClick(i) })
                }
            }
        }

    }

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    private fun MenuSortItem(number: Int, onClick: () -> Unit) {

        val time = number * 5000 + 1000

        val infiniteTransition = rememberInfiniteTransition()

        val text = typesOfSort[number]

        val length = text.length

        val visibleArrow = SortingType.values()[number + 1] == viewModel.sortingType.value

        var s by remember { mutableStateOf(Size.Zero) }

        val degrees by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 0f,
            animationSpec = infiniteRepeatable(
                animation = keyframes {
                    durationMillis = animateDuration
                    0f at time with LinearOutSlowInEasing
                    360f at time + 3000 with LinearOutSlowInEasing
                    360f at animateDuration - 7000 - time with LinearOutSlowInEasing
                    0f at animateDuration - 4000 - time with LinearOutSlowInEasing
                },
                repeatMode = RepeatMode.Restart
            )
        )

        val count by infiniteTransition.animateValue(
            initialValue = 0,
            targetValue = 0,
            typeConverter = TwoWayConverter(
                convertToVector = { AnimationVector1D(it.toFloat()) },
                convertFromVector = { it.value.toInt() }),
            animationSpec = infiniteRepeatable(
                animation = keyframes {
                    durationMillis = animateDuration
                    0 at 0
                    0 at time + 5000 with LinearOutSlowInEasing
                    length at time + 7000 with LinearOutSlowInEasing
                    length at animateDuration - 2000 - time with LinearOutSlowInEasing
                    0 at animateDuration - time with LinearOutSlowInEasing
                },
                repeatMode = RepeatMode.Restart
            )
        )

        val height by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 0f,
            animationSpec = infiniteRepeatable(
                animation = keyframes {
                    durationMillis = animateDuration
                    0f at time + 3000 with LinearOutSlowInEasing
                    32f at time + 4000 with LinearOutSlowInEasing
                    0f at time + 5000 with FastOutLinearInEasing
                    0f at animateDuration - 4000 - time with LinearOutSlowInEasing
                    32f at animateDuration - 3000 - time with LinearOutSlowInEasing
                    0f at animateDuration - 2000 - time with FastOutLinearInEasing
                },
                repeatMode = RepeatMode.Restart
            )
        )

        val mixing by animateFloatAsState(
            targetValue = if (visibleArrow) 53f else 0f,
            animationSpec = tween(durationMillis = 1000, easing = LinearOutSlowInEasing)
        )

        val rotateIconUpDown by animateFloatAsState(
            targetValue = if (viewModel.sortingOrder.value == SortingOrder.ASCENDING) 0f else 180f,
            animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
        )

        Box(
            contentAlignment = Alignment.Center, modifier = Modifier
                .rotate(degrees)
                .padding(bottom = 10.dp)
                .clickable { onClick() }
        ) {

            Row(
                modifier = Modifier
                    .background(Color.LightGray)
                    .onGloballyPositioned { coordinates ->
                        s = coordinates.size.toSize() }
                    .padding(horizontal = 10.dp, vertical = 5.dp)
                    .animateContentSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AnimatedVisibility(
                    visible = visibleArrow,
                    enter = expandIn(
                        expandFrom = Alignment.Center,
                        animationSpec = tween(
                            durationMillis = 1000,
                            easing = LinearOutSlowInEasing
                        ),
                        clip = true
                    ),
                    exit = shrinkOut(
                        shrinkTowards = Alignment.Center,
                        animationSpec = tween(
                            durationMillis = 1000,
                            easing = LinearOutSlowInEasing
                        ),
                        clip = true
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowDownward,
                        contentDescription = "Порядок сортировки",
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .rotate(rotateIconUpDown)
                    )
                }

                Text(
                    text = buildAnnotatedString {

                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Bold,
                                color = colorResource(id = R.color.purple_700),
                                fontStyle = FontStyle.Italic,
                                fontSize = 20.sp,
                                textDecoration = TextDecoration.Underline
                            )
                        ) {
                            append(text.take(count))
                        }

                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Normal,
                                color = Color.Black,
                                fontStyle = FontStyle.Italic,
                                fontSize = 20.sp
                            )
                        ) {
                            append(text.takeLast(length - count))
                        }
                    }
                )
            }

            val width by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 0f,
                animationSpec = infiniteRepeatable(
                    animation = keyframes {
                        durationMillis = animateDuration
                        0f at time + 3000 with LinearOutSlowInEasing
                        widthWindow - s.width / 2 at time + 4000 with LinearOutSlowInEasing
                        0f at time + 5000 with FastOutLinearInEasing
                        0f at animateDuration - 4000 - time with LinearOutSlowInEasing
                        widthWindow - s.width / 2 at animateDuration - 3000 - time with LinearOutSlowInEasing
                        0f at animateDuration - 2000 - time with FastOutLinearInEasing
                    },
                    repeatMode = RepeatMode.Restart
                )
            )

            Canvas(
                modifier = Modifier
                    .padding()
                    .fillMaxWidth()
            ) {

                val w = size.width / 2

                drawRect(
                    color = Color.Black,
                    topLeft = Offset(w - s.width / 2 - width - mixing, -s.height / 2 - height),
                    size = Size((s.width / 2 + width + mixing) * 2, s.height + 2 * height),
                    style = Stroke(width = 5f)
                )
            }
        }
    }

    companion object {

        private const val animateDuration = 56000

        private val typesOfSort = listOf(
            "По алфавиту",
            "По размеру",
            "По дате создания",
            "По дате изменения",
            "По дате последнего открытия"
        )
    }
}