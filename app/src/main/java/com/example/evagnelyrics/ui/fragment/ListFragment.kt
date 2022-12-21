package com.example.evagnelyrics.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.evagnelyrics.databinding.FragmentListBinding
import com.example.evagnelyrics.ui.compose.screen.list.ListScreen
import com.example.evagnelyrics.ui.theme.EvagneLyricsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListFragment : Fragment() {
    private lateinit var _binding: FragmentListBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentListBinding.inflate(inflater, container, false)

        binding.composeView.setContent {
            EvagneLyricsTheme {
                ListScreen { route ->
                    when {
                        route == ACTION_BACK -> findNavController().popBackStack()
                        route.contains(ACTION_NEXT) -> {
                            val title = route.substring(route.indexOf("/") + 1)
                            toLyrics(title)
                        }
                    }
                }
            }
        }

        return binding.root
    }

    private fun toLyrics(title: String) {
        findNavController().navigate(ListFragmentDirections.actionListFragmentToSongsFragment(title))
    }
}

const val ACTION_BACK = "action_back"
const val ACTION_NEXT = "action_next"