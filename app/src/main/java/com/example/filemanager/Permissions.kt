package com.example.filemanager

import android.Manifest
import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat

@RequiresApi(Build.VERSION_CODES.P)
fun requestMultiplePermissions(activity: Activity, PERMISSION_REQUEST_CODE: Int) {
    ActivityCompat.requestPermissions(
        activity, arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
            Manifest.permission.GET_ACCOUNTS,
            Manifest.permission.USE_BIOMETRIC
        ),
        PERMISSION_REQUEST_CODE
    )
}