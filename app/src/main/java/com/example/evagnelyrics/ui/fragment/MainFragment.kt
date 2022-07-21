package com.example.evagnelyrics.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.evagnelyrics.core.Items
import com.example.evagnelyrics.databinding.FragmentMainBinding
import com.example.evagnelyrics.ui.viewmodel.MainFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {
    private lateinit var _binding: FragmentMainBinding
    private val binding get() = _binding

    private val viewModel: MainFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMainBinding.inflate(inflater, container, false)

        initUi()
        subscribeUi()

        return binding.root
    }

    private fun subscribeUi() {
        viewModel.setDatabase()
    }

    private fun initUi() {
        binding.songs.setOnClickListener { toSongs() }
    }

    fun toSongs() {
        findNavController().navigate(MainFragmentDirections.actionMainFragmentToListFragment())
    }
}