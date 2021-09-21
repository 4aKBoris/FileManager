package com.example.filemanager.view.model

import android.content.Context
import android.content.res.Resources
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.util.*

class FileManagerViewModelFactory(
   private val dataStore: DataStore<Preferences>,
   private val resources: Resources
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FileManagerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FileManagerViewModel(dataStore = dataStore, resources = resources) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}