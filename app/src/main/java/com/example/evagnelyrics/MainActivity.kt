package com.example.evagnelyrics

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.evagnelyrics.databinding.ActivityMainBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setListenners()
    }

    private fun setListenners() {
        binding.myBut.setOnClickListener {
            showDialog().show()
        }
    }

    private fun showDialog(): BottomSheetDialog {
        val response = BottomSheetDialog(this)
        response.setContentView(R.layout.my_bottom_shet)

        return response
    }

}