package com.example.evagnelyrics.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.evagnelyrics.R
import com.example.evagnelyrics.databinding.FragmentSongBinding
import com.example.evagnelyrics.ui.viewmodel.SongFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SongFragment : Fragment() {

    private lateinit var _binding: FragmentSongBinding
    private val binding: FragmentSongBinding get() = _binding

    private val viewModel: SongFragmentViewModel by viewModels()

    private val args: SongFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSongBinding.inflate(inflater, container, false)

        initUi()
        subscribeUi()

        return binding.root
    }

    private fun initUi() {
        binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        binding.toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
    }

    private fun subscribeUi() {
        viewModel.onCreate(args.title)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.song.observe(viewLifecycleOwner) {
                if (it != null) {
                    binding.toolbar.title = it.title
                    binding.letter.text = it.letter
                }
            }
        }
    }
}