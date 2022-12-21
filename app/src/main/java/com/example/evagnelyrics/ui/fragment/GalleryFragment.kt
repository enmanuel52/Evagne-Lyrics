package com.example.evagnelyrics.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.evagnelyrics.databinding.FragmentGalleryBinding
import com.example.evagnelyrics.ui.compose.screen.gallery.GalleryScreen

class GalleryFragment : Fragment() {
    private lateinit var _binding: FragmentGalleryBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)

        binding.composeView.setContent {
            GalleryScreen { route ->
                when {
                    route == ACTION_BACK -> findNavController().popBackStack()
                    route.contains(ACTION_NEXT) -> {
                        val index = route.substring(route.indexOf("/") + 1)
                        findNavController().navigate(
                            GalleryFragmentDirections.actionGalleryFragmentToImageFragment(
                                index.toInt()
                            )
                        )
                    }
                }
            }
        }

        return binding.root
    }
}