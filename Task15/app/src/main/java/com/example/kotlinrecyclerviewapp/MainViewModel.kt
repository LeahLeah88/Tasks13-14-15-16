package com.example.kotlinrecyclerviewapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private val _dataList = MutableLiveData<MutableList<Pair<RecyclerData, Boolean>>>()
    val dataList: LiveData<MutableList<Pair<RecyclerData, Boolean>>> = _dataList

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> = _toastMessage
    private var lastDeletedItem: Pair<Int, Pair<RecyclerData, Boolean>>? = null
    private var addedItemsCount = 1

    init {
        initializeData()
    }

    private fun initializeData() {
        val initialData = mutableListOf(
            Pair(RecyclerData("Earth"), false),
            Pair(RecyclerData("Earth"), false),
            Pair(RecyclerData("Mars", ""), false),
            Pair(RecyclerData("Earth"), false),
            Pair(RecyclerData("Earth"), false),
            Pair(RecyclerData("Earth"), false),
            Pair(RecyclerData("Mars", null), false)
        )
        initialData.add(0, Pair(RecyclerData("Header"), false))
        _dataList.value = initialData
    }

    fun onItemClick(data: RecyclerData) {
        _toastMessage.value = data.someText
    }

    fun onFabClick() {
        val currentData = _dataList.value ?: mutableListOf()
        val isEarth = addedItemsCount % 2 == 0
        val item = if (isEarth) {
            RecyclerData(
                "Earth #$addedItemsCount",
                "Новая заметка про Землю #$addedItemsCount"
            )
        } else {
            RecyclerData("Mars #$addedItemsCount", null)
        }
        currentData.add(Pair(item, false))
        addedItemsCount++
        _dataList.value = currentData
    }

    fun onItemMoved(fromPosition: Int, toPosition: Int) {
        val currentData = _dataList.value ?: return
        if (fromPosition <= 0 || toPosition <= 0) return
        val movedItem = currentData.removeAt(fromPosition)
        currentData.add(if (toPosition > fromPosition) toPosition - 1 else toPosition, movedItem)
        _dataList.value = ArrayList(currentData)
    }

    fun onItemDismissed(position: Int) {
        if (position <= 0) return
        val currentData = _dataList.value ?: return
        val removed = currentData.removeAt(position)
        lastDeletedItem = position to removed
        _dataList.value = ArrayList(currentData)
    }

    fun toggleItemText(position: Int) {
        val currentData = _dataList.value ?: return
        if (position in currentData.indices) {
            val currentItem = currentData[position]
            currentData[position] = Pair(currentItem.first, !currentItem.second)
            _dataList.value = ArrayList(currentData)
        }
    }

    fun undoDelete() {
        val currentData = _dataList.value ?: return
        val deleted = lastDeletedItem ?: return
        val index = deleted.first.coerceIn(1, currentData.size)
        currentData.add(index, deleted.second)
        _dataList.value = ArrayList(currentData)
        lastDeletedItem = null
    }
}