package com.example.evagnelyrics.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.evagnelyrics.databinding.FragmentMainBinding
import com.example.evagnelyrics.ui.compose.screen.main.MainScreen
import com.example.evagnelyrics.ui.compose.screen.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {
    private lateinit var _binding: FragmentMainBinding
    private val binding get() = _binding

    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMainBinding.inflate(inflater, container, false)

        binding.composeView.setContent {
            MainScreen {
                when (it) {
                    TO_SONGS -> {
                        findNavController().navigate(MainFragmentDirections.actionMainFragmentToListFragment())
                    }
                    TO_WALLPAPERS -> {
                        findNavController().navigate(MainFragmentDirections.actionMainFragmentToGalleryFragment())
                    }
                }
            }
        }
        return binding.root
    }
}

const val TO_SONGS = "to-songs"
const val TO_WALLPAPERS = "to-walls"