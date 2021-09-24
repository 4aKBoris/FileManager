package com.example.filemanager

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RecyclerViewModel : ViewModel() {

    private val _path = MutableLiveData("/storage/emulated/0")
    private val _files = MutableLiveData(listOf<String>())
    private val _emptyFolder = MutableLiveData(false)
    private val _sort = MutableLiveData(0)
    private val _upDown = MutableLiveData(false)
    private val _typeSort = MutableLiveData(1)
    private val _request = MutableLiveData("")
    private val _select = MutableLiveData(false)
    private val _dialog = MutableLiveData(false)
    private val _dialogSelect = MutableLiveData(0)
    private val _storage = MutableLiveData(false)
    private val _action = MutableLiveData(0)
    private val _animationBottomBar = MutableLiveData(false)
    private val _topBarSortMenuVisible = MutableLiveData(false)

    val path: MutableLiveData<String> = _path
    val select: MutableLiveData<Boolean> = _select
    val emptyFolder: MutableLiveData<Boolean> = _emptyFolder
    val files: MutableLiveData<List<String>> = _files
    val sort: MutableLiveData<Int> = _sort
    val upDown: MutableLiveData<Boolean> = _upDown
    val typeSort: MutableLiveData<Int> = _typeSort
    val request: MutableLiveData<String> = _request
    val dialog: MutableLiveData<Boolean> = _dialog
    val dialogSelect: MutableLiveData<Int> = _dialogSelect
    val storage: MutableLiveData<Boolean> = _storage
    val action: MutableLiveData<Int> = _action
    val animationBottomBar: MutableLiveData<Boolean> = _animationBottomBar
    val topBarSortMenuVisible: MutableLiveData<Boolean> = _topBarSortMenuVisible


    fun SwapDialog() { _dialog.value = _dialog.value!!.not() }
}