package com.example.task13recycler

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.task13recycler.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Экран целиком находится во Fragment (см. FragmentContainerView в activity_main.xml).
    }
}

