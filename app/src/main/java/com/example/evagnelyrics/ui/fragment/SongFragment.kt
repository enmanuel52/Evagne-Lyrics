package com.example.evagnelyrics.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.evagnelyrics.databinding.FragmentSongBinding
import com.example.evagnelyrics.ui.compose.screen.song.SongScreen
import com.example.evagnelyrics.ui.compose.screen.song.SongViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SongFragment : Fragment() {

    private lateinit var _binding: FragmentSongBinding
    private val binding: FragmentSongBinding get() = _binding

    private val viewModel: SongViewModel by viewModels()

    private val args: SongFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSongBinding.inflate(inflater, container, false)

        binding.composeView.setContent {
            SongScreen(title = args.title) { route ->
                when {
                    route == ACTION_BACK -> {
                        findNavController().popBackStack()
                    }
                }
            }
        }

        return binding.root
    }
}