package com.example.task13recycler.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.task13recycler.MultiTypeAdapter
import com.example.task13recycler.databinding.FragmentMainBinding
import com.google.android.material.snackbar.Snackbar

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val adapter = MultiTypeAdapter()

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

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.adapter = adapter

        adapter.submitItems(
            listOf(
                MultiTypeAdapter.UiItem.Header("Категории"),
                MultiTypeAdapter.UiItem.TypeA(
                    title = "Единый список",
                    desc = "Есть заголовок и элементы разных типов"
                ),
                MultiTypeAdapter.UiItem.TypeB(text = "Нажми FAB, чтобы добавить строку B")
            )
        )

        binding.fab.setOnClickListener {
            val count = adapter.itemCount - 1 // без учета header (упрощенно)
            adapter.addItem(
                MultiTypeAdapter.UiItem.TypeB(
                    text = "Новая строка B #${maxOf(1, count)}"
                )
            )
            Snackbar.make(binding.root, "Добавлено!", Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

