package com.example.evagnelyrics.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.evagnelyrics.R
import com.example.evagnelyrics.databinding.FragmentGalleryBinding
import com.example.evagnelyrics.ui.adapter.GalleryAdapter
import com.example.evagnelyrics.ui.listener.OnClickListener

class GalleryFragment : Fragment() {
    private lateinit var _binding: FragmentGalleryBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)

        initUi()

        return binding.root
    }

    private fun initUi() {
        binding.imageRecycler.apply {
            adapter = GalleryAdapter(object : OnClickListener<Int> {
                override fun onClick(model: Int) {
                    findNavController().navigate(
                        GalleryFragmentDirections.actionGalleryFragmentToImageFragment(
                            model
                        )
                    )
                }

            })
            layoutManager = GridLayoutManager(requireContext(), 2)
        }

        binding.galleryToolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        binding.galleryToolbar.setNavigationOnClickListener { findNavController().popBackStack() }
    }
}