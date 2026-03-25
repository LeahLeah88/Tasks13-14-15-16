package com.example.task13recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.task13recycler.databinding.ItemHeaderBinding
import com.example.task13recycler.databinding.ItemTypeABinding
import com.example.task13recycler.databinding.ItemTypeBBinding

class MultiTypeAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    sealed interface UiItem {
        data class Header(val title: String) : UiItem
        data class TypeA(val title: String, val desc: String) : UiItem
        data class TypeB(val text: String) : UiItem
    }

    private val items = mutableListOf<UiItem>()

    private companion object {
        const val VIEW_TYPE_HEADER = 0
        const val VIEW_TYPE_A = 1
        const val VIEW_TYPE_B = 2
    }

    fun submitItems(newItems: List<UiItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    fun addItem(item: UiItem) {
        val insertPosition = items.size
        items.add(item)
        notifyItemInserted(insertPosition)
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is UiItem.Header -> VIEW_TYPE_HEADER
            is UiItem.TypeA -> VIEW_TYPE_A
            is UiItem.TypeB -> VIEW_TYPE_B
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_HEADER -> HeaderVH(ItemHeaderBinding.inflate(inflater, parent, false))
            VIEW_TYPE_A -> TypeAVH(ItemTypeABinding.inflate(inflater, parent, false))
            else -> TypeBVH(ItemTypeBBinding.inflate(inflater, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is UiItem.Header -> (holder as HeaderVH).bind(item)
            is UiItem.TypeA -> (holder as TypeAVH).bind(item)
            is UiItem.TypeB -> (holder as TypeBVH).bind(item)
        }
    }

    private class HeaderVH(
        val binding: ItemHeaderBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: UiItem.Header) {
            binding.headerTitle.text = item.title
        }
    }

    private class TypeAVH(
        val binding: ItemTypeABinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: UiItem.TypeA) {
            binding.titleText.text = item.title
            binding.descText.text = item.desc
        }
    }

    private class TypeBVH(
        val binding: ItemTypeBBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: UiItem.TypeB) {
            binding.rowText.text = item.text
        }
    }
}

