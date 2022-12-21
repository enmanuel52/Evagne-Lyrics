package com.example.evagnelyrics.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.evagnelyrics.databinding.FragmentPictureBinding
import com.example.evagnelyrics.ui.compose.screen.picture.PictureScreen

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
        binding.composeView.setContent {
            PictureScreen(page = position) {
                if (it == ACTION_BACK) {
                    findNavController().popBackStack()
                }
            }
        }

        return binding.root
    }
}