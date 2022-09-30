package com.example.evagnelyrics.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.evagnelyrics.R
import com.example.evagnelyrics.databinding.FragmentPictureBinding
import com.example.evagnelyrics.ui.adapter.PictureAdapter

class PictureFragment : Fragment() {

    private lateinit var _binding: FragmentPictureBinding
    private val binding get() = _binding

    val navArgs: PictureFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPictureBinding.inflate(inflater, container, false)

        val position = navArgs.image
        val myAdapter = PictureAdapter()
        binding.viewPager.run {
            adapter = myAdapter
            currentItem = position
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        binding.toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
    }
}