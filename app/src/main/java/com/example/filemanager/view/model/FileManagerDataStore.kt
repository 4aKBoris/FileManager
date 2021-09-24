package com.example.filemanager.view.model

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class FileManagerDataStore(private val dataStore: DataStore<Preferences>) {

    suspend fun save(key: String, value: Boolean) = withContext(Dispatchers.IO) {
        val booleanKey = booleanPreferencesKey(key)
        dataStore.edit { it[booleanKey] = value }
        println(value)
    }

    suspend fun save(key: String, value: String) = withContext(Dispatchers.IO) {
        val stringKey = stringPreferencesKey(key)
        dataStore.edit { it[stringKey] = value }
    }

    suspend fun save(key: String, value: List<String>) = withContext(Dispatchers.IO) {
        val setKey = stringSetPreferencesKey(key)
        dataStore.edit { it[setKey] = value.toSet() }
    }

    suspend fun getBoolean(key: String): Boolean = withContext(Dispatchers.IO) {
        val booleanKey = booleanPreferencesKey(key)
        val valueFlow: Flow<Boolean> = dataStore.data.map {
            it[booleanKey] ?: false
        }
        return@withContext valueFlow.first()
    }

    suspend fun getString(key: String): String = withContext(Dispatchers.IO) {
        val stringKey = stringPreferencesKey(key)
        val valueFlow: Flow<String> = dataStore.data.map {
            it[stringKey] ?: "DEFAULT"
        }
        return@withContext valueFlow.first()
    }

    suspend fun getSet(key: String): List<String> = withContext(Dispatchers.IO) {
        val setKey = stringSetPreferencesKey(key)
        val valueFlow: Flow<Set<String>> = dataStore.data.map {
            it[setKey] ?: setOf()
        }
        return@withContext valueFlow.first().toList()
    }

}