package com.example.filemanager.ui.components.bar.bottom

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

enum class BottomBarButtons(val number: Int, val icon: ImageVector, val description: String) {
    CONFIRM(number = 4, icon = Icons.Default.Check, description = "Подтвердить"),
    COPY(number = 3, icon = Icons.Default.FileCopy, description = "Копировать"),
    MOVE(number = 2, icon = Icons.Default.DriveFileMove, description = "Переместить"),
    DELETE(number = 1, icon = Icons.Default.Delete, description = "Удалить"),
    SELECT(number = 0, icon = Icons.Default.Cancel, description = "Режим выделения")
}