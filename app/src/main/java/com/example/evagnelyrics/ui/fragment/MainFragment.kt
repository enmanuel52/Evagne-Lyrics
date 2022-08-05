package com.example.evagnelyrics.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.evagnelyrics.R
import com.example.evagnelyrics.databinding.FragmentMainBinding
import com.example.evagnelyrics.ui.viewmodel.MainFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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

        /*//here I'll call datastore for the last value of the dark mode
        viewModel.initNightMode(false)*/

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.nightMode.observe(viewLifecycleOwner) {
                if (! (it!!)) {
                    //set light mode, and icon for it
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    binding.mainToolbar.menu.findItem(R.id.nightMode)
                        .setIcon(R.drawable.ic_baseline_bedtime_24)
                } else {
                    //set night mode
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    binding.mainToolbar.menu.findItem(R.id.nightMode)
                        .setIcon(R.drawable.ic_baseline_wb_sunny_24)
                }
            }
        }
    }

    private fun initUi() {
        binding.songs.setOnClickListener { toSongs() }
        binding.wallpapers.setOnClickListener { toGallery() }
    }

    private fun toGallery() {
        findNavController().navigate(MainFragmentDirections.actionMainFragmentToGalleryFragment())
    }

    private fun toSongs() {
        findNavController().navigate(MainFragmentDirections.actionMainFragmentToListFragment())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mainToolbar.inflateMenu(R.menu.main_menu)
        binding.mainToolbar.setOnMenuItemClickListener { menu ->
            when (menu.itemId) {
                R.id.nightMode -> {
                    viewModel.changeLightMode()
                    true
                }
                else -> {
                    true
                }
            }
        }
    }
}