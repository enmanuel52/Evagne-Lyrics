package com.example.evagnelyrics.ui.fragment

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.evagnelyrics.R
import com.example.evagnelyrics.databinding.FragmentListBinding
import com.example.evagnelyrics.ui.adapter.ListAdapter
import com.example.evagnelyrics.ui.compose.screen.list.ListScreen
import com.example.evagnelyrics.ui.viewmodel.ListFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListFragment : Fragment() {
    private lateinit var _binding: FragmentListBinding
    private val binding get() = _binding

    private val viewModel: ListFragmentViewModel by viewModels()


    private lateinit var adapter: ListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentListBinding.inflate(inflater, container, false)

        binding.composeView.setContent {
            ListScreen(navForward = { toLyrics(it) })
        }
//        initUi()
//        subscribeUi()

        return binding.root
    }

//    private fun subscribeUi() {
//        viewModel.getAllTitles()
//        viewLifecycleOwner.lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.STARTED) {
//                viewModel.songs.collectLatest {
//                    adapter.items = it
//                    adapter.notifyDataSetChanged()
//                }
//            }
//        }
//
//        viewLifecycleOwner.lifecycleScope.launch {
//            viewModel.fav.observe(viewLifecycleOwner) { fav ->
//                binding.listToolbar.menu.findItem(R.id.showFav).let { menu ->
//                    if (fav) {
//                        menu.icon.setTint(requireContext().getColor(R.color.pink))
//                    } else {
//                        menu.icon.setTint(Color.WHITE)
//                    }
//                }
//                binding.listToolbar.menu.findItem(R.id.searchItem).let { search ->
//                    search.isVisible = !fav
//                }
//
////                binding.emptyText.isVisible = fav && viewModel.songs.value.isEmpty()
//            }
//        }
//
//        viewLifecycleOwner.lifecycleScope.launch {
//            viewModel.uiState.flowWithLifecycle(lifecycle)
//                .collectLatest { state ->
//                    when (state) {
//                        is Resource.Error -> {
//                            Snackbar.make(
//                                binding.root,
//                                state.msg,
//                                Snackbar.LENGTH_SHORT
//                            ).show()
//                        }
//                        is Resource.Success -> {}
//                    }
//                }
//        }
//    }

//    private fun initUi() {
//        adapter = ListAdapter {
//            if (it is Action.Click) {
//                toLyrics(it.title)
//            } else if (it is Action.Favorite) {
//                viewModel.favAction(it.title)
//            }
//        }
//
//        binding.listRecycler.adapter = adapter
//        binding.listRecycler.layoutManager = LinearLayoutManager(requireContext())
//    }

    private fun toLyrics(title: String) {
        findNavController().navigate(ListFragmentDirections.actionListFragmentToSongsFragment(title))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.listToolbar.run {
            inflateMenu(R.menu.list_menu)
            search()
            setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
            setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.searchItem -> {
                        true
                    }
                    R.id.showFav -> {
                        viewModel.onFavMode()
                        true
                    }
                    else -> {
                        true
                    }
                }
            }
        }

    }

    /*
    * setting the search configuration
    * */
    private fun search() {
        //set the searchView
        val searchManager =
            requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (binding.listToolbar.menu.findItem(R.id.searchItem).actionView as SearchView).apply {
            // Assumes current activity is the searchable activity
            setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))


            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    //search songs
                    if (newText != null) {
                        viewModel.searchByName(title = newText.lowercase())
                    } else
                        viewModel.getAllSongs()

                    return true
                }

            })

            setOnQueryTextFocusChangeListener { _, b ->
                if (!b) {
                    //show songs
                    viewModel.getAllSongs()
                }
            }
        }
    }
}