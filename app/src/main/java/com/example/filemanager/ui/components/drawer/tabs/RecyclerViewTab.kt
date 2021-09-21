package com.example.filemanager.ui.components.drawer.tabs

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.filemanager.R

data class DataTab(
    @DrawableRes val idPainter: Int,
    val name: String,
    val info: String,
    val description: String,
    val onDelete: () -> Unit
)

@Composable
fun RecyclerViewTab(
    listItems: List<DataTab>,
    imageVector: ImageVector,
    @StringRes description: Int,
    onItemClick: (String, () -> Unit) -> Unit,
    closeDrawer: () -> Unit,
    onBottomButtonClick: () -> Unit
) {
    val state = rememberLazyListState()

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
        LazyColumn(state = state) {
            items(listItems) {
                Item(item = it, onItemClick = onItemClick, closeDrawer = closeDrawer)
                Divider()
            }

        }
        BottomButton(
            imageVector = imageVector,
            description = description,
            onClick = onBottomButtonClick
        )
    }

}

@Composable
private fun Item(
    item: DataTab,
    onItemClick: (String, () -> Unit) -> Unit,
    closeDrawer: () -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
    ) {

        val (icon, text, button) = createRefs()

        TypeIcon(
            idPainter = item.idPainter,
            description = item.description,
            modifier = Modifier.constrainAs(icon) {
                start.linkTo(anchor = parent.start, margin = 16.dp)
                centerVerticallyTo(parent)
            })
        
        ItemInfo(name = item.name, info = item.info, modifier = Modifier.constrainAs(text) {
            start.linkTo(anchor = icon.end, margin = 16.dp)
            centerVerticallyTo(parent)
        })
        
        DeleteButton(onDelete = item.onDelete, description = R.string.delete_item, modifier = Modifier.constrainAs(button) {
            end.linkTo(anchor = parent.end, margin = 8.dp)
            centerVerticallyTo(parent)
        })
        
    }
}

@Composable
private fun TypeIcon(@DrawableRes idPainter: Int, description: String, modifier: Modifier) {
    Icon(
        painter = painterResource(id = idPainter),
        contentDescription = description,
        modifier = modifier.size(40.dp),
    )
}

@Composable
private fun ItemInfo(name: String, info: String, modifier: Modifier) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start,
        modifier = modifier
    ) {
        MainText(name = name)
        SecondaryText(text = info)
    }
}

@Composable
private fun DeleteButton(onDelete: () -> Unit, @StringRes description: Int, modifier: Modifier) {
    IconButton(modifier = modifier, onClick = onDelete) {
        Icon(
            imageVector = Icons.Default.Cancel,
            contentDescription = stringResource(id = description),
            tint = Color.Gray
        )
    }
}

@Composable
private fun BottomButton(
    imageVector: ImageVector,
    @StringRes description: Int,
    onClick: () -> Unit
) {
    IconButton(
        modifier = Modifier
            .fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = imageVector,
                contentDescription = stringResource(id = description)
            )
            Text(text = stringResource(id = description))
        }
    }
}

@Composable
private fun MainText(name: String) {
    Text(
        text = name,
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


