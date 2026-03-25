package com.example.task14recycler.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.task14recycler.databinding.ItemTaskBinding
import com.example.task14recycler.model.TaskItem

class TaskAdapter(
    private val items: MutableList<TaskItem>,
    private val onItemClicked: (Int) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    override fun getItemCount(): Int = items.size

    override fun getItemId(position: Int): Long = items[position].id

    fun addItem(item: TaskItem) {
        items.add(0, item)
        notifyItemInserted(0)
    }

    fun updateItem(position: Int, newTitle: String) {
        if (position !in items.indices) return
        items[position].title = newTitle
        notifyItemChanged(position)
    }

    fun removeItem(position: Int): TaskItem? {
        if (position !in items.indices) return null
        val removed = items.removeAt(position)
        notifyItemRemoved(position)
        return removed
    }

    fun moveItem(fromPosition: Int, toPosition: Int) {
        if (fromPosition !in items.indices || toPosition !in items.indices) return
        val item = items.removeAt(fromPosition)
        items.add(toPosition, item)
        notifyItemMoved(fromPosition, toPosition)
    }

    inner class TaskViewHolder(
        private val binding: ItemTaskBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TaskItem, position: Int) {
            binding.taskTitle.text = item.title
            binding.taskIndex.text = (position + 1).toString()
            binding.root.setOnClickListener {
                onItemClicked(bindingAdapterPosition)
            }
        }
    }
}
