package com.example.kotlinrecyclerviewapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private val repository: PlanetListRepository = InMemoryPlanetListRepository()

    private val _dataList = MutableLiveData<MutableList<Pair<RecyclerData, Boolean>>>()
    val dataList: LiveData<MutableList<Pair<RecyclerData, Boolean>>> = _dataList

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> = _toastMessage
    private var lastDeletedItem: Pair<Int, Pair<RecyclerData, Boolean>>? = null
    private var addedItemsCount = 1

    init {
        _dataList.value = repository.getInitialData()
    }

    fun onItemClick(data: RecyclerData) {
        _toastMessage.value = data.someText
    }

    fun onFabClick() {
        val currentData = _dataList.value ?: mutableListOf()
        _dataList.value = repository.addItem(currentData, addedItemsCount)
        addedItemsCount++
    }

    fun onItemMoved(fromPosition: Int, toPosition: Int) {
        val currentData = _dataList.value ?: return
        _dataList.value = repository.moveItem(currentData, fromPosition, toPosition)
    }

    fun onItemDismissed(position: Int) {
        val currentData = _dataList.value ?: return
        val result = repository.removeItem(currentData, position)
        _dataList.value = result.first
        lastDeletedItem = result.second
    }

    fun toggleItemText(position: Int) {
        val currentData = _dataList.value ?: return
        _dataList.value = repository.toggleDescription(currentData, position)
    }

    fun undoDelete() {
        val currentData = _dataList.value ?: return
        val deleted = lastDeletedItem ?: return
        _dataList.value = repository.restoreItem(currentData, deleted)
        lastDeletedItem = null
    }
}