package com.example.filemanager.ui.components.drawer.tabs.settings

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.filemanager.R
import com.example.filemanager.sort.GroupingType
import com.example.filemanager.sort.SortingOrder
import com.example.filemanager.view.model.FileManagerViewModel

enum class SortingSections(@StringRes val id: Int) {
    Grouping(R.string.grouping) {

        override fun Items() = GroupingType.values().map { it.id }

        override fun onClick(viewModel: FileManagerViewModel) = viewModel::setGroupingType

        @StringRes
        override fun getIdRes(viewModel: FileManagerViewModel) = viewModel.groupingType.value.id
    },

    SortType(R.string.sorting_type) {
        override fun Items() = com.example.filemanager.sort.SortingType.values().map { it.id }

        override fun onClick(viewModel: FileManagerViewModel) = viewModel::setSortingType

        @StringRes
        override fun getIdRes(viewModel: FileManagerViewModel) = viewModel.sortingType.value.id
    },

    SortOrder(R.string.sorting_order) {
        override fun Items() = SortingOrder.values().map { it.id }

        override fun onClick(viewModel: FileManagerViewModel) = viewModel::setSortingOrder

        @StringRes
        override fun getIdRes(viewModel: FileManagerViewModel) = viewModel.sortingOrder.value.id
    };

    abstract fun Items(): List<Int>

    abstract fun onClick(viewModel: FileManagerViewModel): (Int) -> Unit

    @StringRes
    abstract fun getIdRes(viewModel: FileManagerViewModel): Int

    @Composable
    fun Body(onClick: () -> Unit, viewModel: FileManagerViewModel) {
        SortingType(
            id = getIdRes(viewModel),
            onClick = onClick,
            array = Items(),
            onClickTwo = onClick(viewModel = viewModel)
        )
    }

    @Composable
    private fun SortingType(
        @StringRes id: Int,
        onClick: () -> Unit,
        array: List<Int>,
        onClickTwo: (Int) -> Unit
    ) {
        LazyColumn {
            items(items = array) { item ->
                AlertDialogListItem(
                    id = item,
                    onClick = {
                        onClick()
                        onClickTwo(item)
                    },
                    isSelected = item == id
                )
                if (item != array.last()) Divider()
            }
        }
    }

    @Composable
    private fun AlertDialogListItem(@StringRes id: Int, onClick: () -> Unit, isSelected: Boolean) {
        Row(
            modifier = Modifier
                .fillMaxWidth().clickable(onClick = onClick)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            MainText(text = id)
            RadioButton(selected = isSelected, onClick = onClick, colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colors.secondaryVariant,
                unselectedColor = MaterialTheme.colors.primaryVariant
            ))
        }
    }

    @Composable
    private fun MainText(@StringRes text: Int) {
        Text(
            text = stringResource(id = text),
            style = MaterialTheme.typography.subtitle2,
            color = MaterialTheme.colors.primaryVariant,
        )
    }
}