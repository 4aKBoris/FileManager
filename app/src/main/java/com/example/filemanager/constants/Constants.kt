package com.example.filemanager.constants

import android.annotation.SuppressLint
import android.os.Environment

internal const val PATH = "PATH"
internal const val LOG_TAG = "LOG"
internal const val DIRECTORY = "Directory"
internal const val FILE = "File"
internal val STORAGE = Environment.getExternalStorageDirectory().absolutePath
@SuppressLint("SdCardPath")
internal const val pathDeleteFiles = "/data/user/0/com.example.filemanager/files"