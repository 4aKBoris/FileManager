package com.example.filemanager.ui.components.textfields

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.filemanager.ui.theme.FileManagerTheme

@Composable
fun MainText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.subtitle2,
        color = MaterialTheme.colors.primaryVariant,
    )
}

@Composable
fun MinorText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.subtitle2,
        color = MaterialTheme.colors.primary,
    )
}

@Composable
fun SecondaryText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.body2,
        color = MaterialTheme.colors.primary,
    )
}

@Composable
fun CancelText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.h6,
        color = MaterialTheme.colors.primaryVariant
    )
}

@Composable
fun TitleText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.h6,
        color = MaterialTheme.colors.secondaryVariant,
    )
}

@Preview
@Composable
private fun MainTextPreviewLight() {
    FileManagerTheme {
        MainText(text = "MainTextPreviewLight")
    }
}

@Preview
@Composable
private fun MainTextPreviewDark() {
    FileManagerTheme(darkTheme = true) {
        MainText(text = "MainTextPreviewDark")
    }
}


@Preview
@Composable
fun MinorTextPreviewLight() {
    FileManagerTheme {
        MinorText(text = "MinorTextPreviewLight")
    }
}

@Preview
@Composable
fun MinorTextPreviewDark() {
    FileManagerTheme(darkTheme = true) {
        MinorText(text = "MinorTextPreviewDark")
    }
}

@Preview
@Composable
fun SecondaryTextPreviewLight() {
    FileManagerTheme {
        SecondaryText(text = "SecondaryTextPreviewLight")
    }
}

@Preview
@Composable
fun SecondaryTextPreviewDark() {
    FileManagerTheme(darkTheme = true) {
        SecondaryText(text = "SecondaryTextPreviewDark")
    }
}

@Preview
@Composable
fun CancelTextPreviewLight() {
    FileManagerTheme {
        CancelText(text = "CancelTextPreviewLight")
    }
}

@Preview
@Composable
fun CancelTextPreviewDark() {
    FileManagerTheme(darkTheme = true) {
        CancelText(text = "CancelTextPreviewDark")
    }
}

@Preview
@Composable
fun TitleTextPreviewLight() {
    FileManagerTheme {
        TitleText(text = "TitleTextPreviewLight")
    }
}

@Preview
@Composable
fun TitleTextPreviewDark() {
    FileManagerTheme(darkTheme = true) {
        TitleText(text = "TitleTextPreviewDark")
    }
}