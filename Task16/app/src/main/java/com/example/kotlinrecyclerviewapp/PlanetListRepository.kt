package com.example.kotlinrecyclerviewapp

interface PlanetListRepository {
    fun getInitialData(): MutableList<Pair<RecyclerData, Boolean>>
    fun addItem(items: MutableList<Pair<RecyclerData, Boolean>>, itemNumber: Int): MutableList<Pair<RecyclerData, Boolean>>
    fun moveItem(
        items: MutableList<Pair<RecyclerData, Boolean>>,
        fromPosition: Int,
        toPosition: Int
    ): MutableList<Pair<RecyclerData, Boolean>>
    fun removeItem(
        items: MutableList<Pair<RecyclerData, Boolean>>,
        position: Int
    ): Pair<MutableList<Pair<RecyclerData, Boolean>>, Pair<Int, Pair<RecyclerData, Boolean>>?>
    fun restoreItem(
        items: MutableList<Pair<RecyclerData, Boolean>>,
        deletedItem: Pair<Int, Pair<RecyclerData, Boolean>>
    ): MutableList<Pair<RecyclerData, Boolean>>
    fun toggleDescription(
        items: MutableList<Pair<RecyclerData, Boolean>>,
        position: Int
    ): MutableList<Pair<RecyclerData, Boolean>>
}

class InMemoryPlanetListRepository : PlanetListRepository {
    override fun getInitialData(): MutableList<Pair<RecyclerData, Boolean>> {
        val initialData = mutableListOf(
            RecyclerData("Earth") to false,
            RecyclerData("Earth") to false,
            RecyclerData("Mars", "") to false,
            RecyclerData("Earth") to false,
            RecyclerData("Earth") to false,
            RecyclerData("Earth") to false,
            RecyclerData("Mars", null) to false
        )
        initialData.add(0, RecyclerData("Header") to false)
        return initialData
    }

    override fun addItem(
        items: MutableList<Pair<RecyclerData, Boolean>>,
        itemNumber: Int
    ): MutableList<Pair<RecyclerData, Boolean>> {
        val isEarth = itemNumber % 2 == 0
        val newItem = if (isEarth) {
            RecyclerData("Earth #$itemNumber", "Новая заметка про Землю #$itemNumber")
        } else {
            RecyclerData("Mars #$itemNumber", null)
        }
        return ArrayList(items).apply { add(newItem to false) }
    }

    override fun moveItem(
        items: MutableList<Pair<RecyclerData, Boolean>>,
        fromPosition: Int,
        toPosition: Int
    ): MutableList<Pair<RecyclerData, Boolean>> {
        if (fromPosition <= 0 || toPosition <= 0) return ArrayList(items)
        if (fromPosition !in items.indices || toPosition !in items.indices) return ArrayList(items)

        val updated = ArrayList(items)
        val movedItem = updated.removeAt(fromPosition)
        updated.add(if (toPosition > fromPosition) toPosition - 1 else toPosition, movedItem)
        return updated
    }

    override fun removeItem(
        items: MutableList<Pair<RecyclerData, Boolean>>,
        position: Int
    ): Pair<MutableList<Pair<RecyclerData, Boolean>>, Pair<Int, Pair<RecyclerData, Boolean>>?> {
        if (position <= 0 || position !in items.indices) return ArrayList(items) to null
        val updated = ArrayList(items)
        val removed = updated.removeAt(position)
        return updated to (position to removed)
    }

    override fun restoreItem(
        items: MutableList<Pair<RecyclerData, Boolean>>,
        deletedItem: Pair<Int, Pair<RecyclerData, Boolean>>
    ): MutableList<Pair<RecyclerData, Boolean>> {
        val updated = ArrayList(items)
        val index = deletedItem.first.coerceIn(1, updated.size)
        updated.add(index, deletedItem.second)
        return updated
    }

    override fun toggleDescription(
        items: MutableList<Pair<RecyclerData, Boolean>>,
        position: Int
    ): MutableList<Pair<RecyclerData, Boolean>> {
        if (position !in items.indices) return ArrayList(items)
        val updated = ArrayList(items)
        val current = updated[position]
        updated[position] = current.first to !current.second
        return updated
    }
}
