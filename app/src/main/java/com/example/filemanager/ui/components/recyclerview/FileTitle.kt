@file:Suppress("EXPERIMENTAL_IS_NOT_ENABLED")

package com.example.filemanager.ui.components.recyclerview

import androidx.compose.animation.*
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleUp
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.filemanager.item.FileItem

@ExperimentalFoundationApi
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FileTitle(
    item: FileItem,
    info: Boolean,
    edit: Boolean,
    text: String,
    onClose: () -> Unit,
    onCancelEdit: () -> Unit,
    onEdit: () -> Unit,
    setText: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(20.dp, 5.dp)
        ) {
            FileIcon(drawable = painterResource(id = item.expansion))
            Crossfade(
                targetState = edit,
                animationSpec = tween(durationMillis = 500, easing = LinearEasing)
            ) {
                if (it) EditFileName(
                    text = text,
                    setText = setText,
                    onCancelEdit = onCancelEdit,
                    onEdit = onEdit
                ) else InfoText(item = item)
            }
        }
        Divider(startIndent = 70.dp)
        AnimatedVisibility(
            visible = info,
            enter = expandVertically(
                expandFrom = Alignment.Top,
                animationSpec = tween(
                    delayMillis = 500,
                    durationMillis = 1000,
                    easing = LinearOutSlowInEasing
                )
            ),
            exit = shrinkVertically(
                shrinkTowards = Alignment.Top,
                animationSpec = tween(
                    durationMillis = 1000,
                    easing = LinearOutSlowInEasing
                )
            )
        ) {
            InfoAboutFile(item = item, onClose = onClose)
        }
    }
}

@Composable
private fun EditFileName(
    text: String,
    setText: (String) -> Unit,
    onCancelEdit: () -> Unit,
    onEdit: () -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        TextField(
            value = text, onValueChange = setText,
            leadingIcon = {
                TextFieldIcon(
                    imageVector = Icons.Default.Cancel,
                    description = "Закрыть",
                    onClick = onCancelEdit
                )
            },
            trailingIcon = {
                TextFieldIcon(
                    imageVector = Icons.Default.Check,
                    description = "Сохранить изменения",
                    onClick = onEdit
                )
            },
            textStyle = MaterialTheme.typography.subtitle2,
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White,
                focusedIndicatorColor = Color.White,
                disabledIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.White,
            )
        )
    }
}

@Composable
private fun TextFieldIcon(imageVector: ImageVector, description: String, onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(imageVector = imageVector, contentDescription = description)
    }
}

@Composable
private fun InfoAboutFile(item: FileItem, onClose: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 16.dp)
    ) {
        ButtonClose("Тип: ", item.type.name, onClose = onClose)
        InfoAboutFileText(
            if (item.isDirectory) "Количество объектов в папке" else "Размер файла: ",
            item.stringSize
        )
        InfoAboutFileText("Размер в байтах: ", item.size.toString())
        InfoAboutFileText("Полный путь: ", item.path)
        InfoAboutFileText("Дата создания: ", item.fullDateCreate)
        InfoAboutFileText("Дата последнего изменения: ", item.partialDateChange)
        InfoAboutFileText("Дата последнего открытия: ", item.fullDateAccess)
    }
}

@Composable
private fun ButtonClose(title: String, text: String, onClose: () -> Unit) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        InfoAboutFileText(title = title, text = text)
        IconButton(onClick = onClose) {
            Icon(
                imageVector = Icons.Default.ArrowCircleUp,
                contentDescription = "Закрыть информацию о файле",
                modifier = Modifier.size(48.dp)
            )
        }
    }

}

@Composable
private fun InfoAboutFileText(title: String, text: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)) {
        MainText(fileName = title)
        SecondaryText(text = text)
    }
}

@Composable
private fun InfoText(item: FileItem) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp), horizontalAlignment = Alignment.Start
    ) {
        MainText(fileName = item.name)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SecondaryText(text = item.stringSize)
            SecondaryText(text = item.partialDateChange)
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