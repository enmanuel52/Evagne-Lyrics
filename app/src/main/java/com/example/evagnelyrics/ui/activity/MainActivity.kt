package com.example.evagnelyrics.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.evagnelyrics.R
import com.example.evagnelyrics.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_EvagneLyrics)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}