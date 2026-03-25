package com.example.kotlinrecyclerviewapp

import android.graphics.Color
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MVVMRecyclerActivityAdapter(
    private val viewModel: MainViewModel,
    private val data: MutableList<Pair<RecyclerData, Boolean>>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), ItemTouchHelperAdapter {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_EARTH -> EarthViewHolder(inflater.inflate(R.layout.activity_recycler_item_earth, parent, false))
            TYPE_MARS -> MarsViewHolder(inflater.inflate(R.layout.activity_recycler_item_mars, parent, false))
            else -> HeaderViewHolder(inflater.inflate(R.layout.activity_recycler_item_header, parent, false))
        }
    }

    fun updateData(newData: List<Pair<RecyclerData, Boolean>>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()  //Обновляем adapter
        Log.d("Adapter", "Обновлено ${data.size} элементов")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TYPE_EARTH -> (holder as EarthViewHolder).bind(data[position])
            TYPE_MARS -> (holder as MarsViewHolder).bind(data[position])
            else -> (holder as HeaderViewHolder).bind(data[position])
        }
    }

    override fun getItemCount(): Int = data.size

    override fun getItemViewType(position: Int): Int {
        return when {
            data[position].first.someText == "Header" -> TYPE_HEADER
            data[position].first.someDescription.isNullOrBlank() -> TYPE_MARS
            else -> TYPE_EARTH
        }
    }

    inner class EarthViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(dataItem: Pair<RecyclerData, Boolean>) {
            if (layoutPosition != RecyclerView.NO_POSITION) {
                itemView.findViewById<TextView>(R.id.descriptionTextView)?.text = dataItem.first.someDescription
                itemView.findViewById<ImageView>(R.id.wikiImageView)?.setOnClickListener {
                    viewModel.onItemClick(dataItem.first)
                }
            }
        }
    }

    inner class MarsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), ItemTouchHelperViewHolder {
        fun bind(dataItem: Pair<RecyclerData, Boolean>) {
            itemView.findViewById<ImageView>(R.id.marsImageView)?.setOnClickListener {
                viewModel.onItemClick(dataItem.first)
            }
            itemView.findViewById<ImageView>(R.id.moveItemDown)?.setOnClickListener { moveDown() }
            itemView.findViewById<ImageView>(R.id.moveItemUp)?.setOnClickListener { moveUp() }
            itemView.findViewById<TextView>(R.id.marsTextView)?.setOnClickListener { toggleText() }
            itemView.findViewById<TextView>(R.id.marsTextView)?.text = dataItem.first.someText
            val description = itemView.findViewById<TextView>(R.id.marsDescriptionTextView)
            description?.visibility = if (dataItem.second) View.VISIBLE else View.GONE
        }

        private fun toggleText() {
            viewModel.toggleItemText(layoutPosition)
        }

        private fun moveUp() {
            layoutPosition.takeIf { it > 1 }?.also { pos ->
                viewModel.onItemMoved(pos, pos - 1)
            }
        }

        private fun moveDown() {
            layoutPosition.takeIf { it < data.size - 1 }?.also { pos ->
                viewModel.onItemMoved(pos, pos + 1)
            }
        }

        override fun onItemSelected() { itemView.setBackgroundColor(Color.LTGRAY) }
        override fun onItemClear() { itemView.setBackgroundColor(0) }
    }

    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(dataItem: Pair<RecyclerData, Boolean>) {
            itemView.findViewById<TextView>(R.id.header)?.apply {
                text = dataItem.first.someText
                gravity = Gravity.CENTER
                setOnClickListener { viewModel.onItemClick(dataItem.first) }
            }
        }
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        viewModel.onItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {
        viewModel.onItemDismissed(position)
    }

    fun undoDelete() {
        viewModel.undoDelete()
    }

    companion object {
        const val TYPE_EARTH = 0
        const val TYPE_MARS = 1
        const val TYPE_HEADER = 2
    }
}