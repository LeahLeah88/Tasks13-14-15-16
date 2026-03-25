package com.example.task14recycler.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.task14recycler.R
import com.example.task14recycler.databinding.FragmentMainBinding
import com.example.task14recycler.model.TaskItem
import java.util.concurrent.atomic.AtomicLong

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val idGenerator = AtomicLong(4)
    private val tasks = mutableListOf(
        TaskItem(1, "Купить продукты"),
        TaskItem(2, "Подготовить задание по Kotlin"),
        TaskItem(3, "Прочитать главу по RecyclerView")
    )

    private lateinit var adapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TaskAdapter(tasks, onItemClicked = ::showEditDialog)
        setupRecycler()
        setupFab()
    }

    private fun setupRecycler() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
        ItemTouchHelper(touchCallback).attachToRecyclerView(binding.recyclerView)
    }

    private fun setupFab() {
        binding.fab.setOnClickListener {
            showAddDialog()
        }
    }

    private fun showAddDialog() {
        val input = EditText(requireContext()).apply {
            hint = getString(R.string.dialog_hint_task)
        }

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(R.string.dialog_title_add)
            .setView(input)
            .setPositiveButton(R.string.action_add, null)
            .setNegativeButton(R.string.action_cancel, null)
            .create()

        dialog.setOnShowListener {
            val positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positive.isEnabled = false
            input.doOnTextChanged { text, _, _, _ ->
                positive.isEnabled = !text.isNullOrBlank()
            }
            positive.setOnClickListener {
                val title = input.text.toString().trim()
                if (title.isEmpty()) return@setOnClickListener
                adapter.addItem(TaskItem(idGenerator.getAndIncrement(), title))
                binding.recyclerView.scrollToPosition(0)
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    private fun showEditDialog(position: Int) {
        if (position !in tasks.indices) return
        val task = tasks[position]
        val input = EditText(requireContext()).apply {
            setText(task.title)
            setSelection(task.title.length)
        }

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(R.string.dialog_title_edit)
            .setView(input)
            .setPositiveButton(R.string.action_save, null)
            .setNegativeButton(R.string.action_cancel, null)
            .create()

        dialog.setOnShowListener {
            val positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positive.isEnabled = task.title.isNotBlank()
            input.doOnTextChanged { text, _, _, _ ->
                positive.isEnabled = !text.isNullOrBlank()
            }
            positive.setOnClickListener {
                val updated = input.text.toString().trim()
                if (updated.isEmpty()) return@setOnClickListener
                adapter.updateItem(position, updated)
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    private val touchCallback = object : ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP or ItemTouchHelper.DOWN,
        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val fromPosition = viewHolder.bindingAdapterPosition
            val toPosition = target.bindingAdapterPosition
            adapter.moveItem(fromPosition, toPosition)
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.bindingAdapterPosition
            val removedTask = adapter.removeItem(position)
            if (removedTask != null) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.task_deleted, removedTask.title),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
