package com.krissphi.id.dicoding_events_mid_submission_app.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.krissphi.id.dicoding_events_mid_submission_app.data.response.ListEventsItem
import com.krissphi.id.dicoding_events_mid_submission_app.databinding.FragmentSearchBinding
import com.krissphi.id.dicoding_events_mid_submission_app.ui.EventsAdapter
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var searchViewModel: SearchViewModel
    private lateinit var eventsAdapter: EventsAdapter
    private var searchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchViewModel = ViewModelProvider(this)[SearchViewModel::class.java]

        setupRecyclerView()
        observeViewModel()
        setupSearchView()

        searchViewModel.resetSearch()
    }

    private fun setupRecyclerView() {
        eventsAdapter = EventsAdapter()
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvSearchResults.layoutManager = layoutManager
        binding.rvSearchResults.adapter = eventsAdapter
        binding.rvSearchResults.addItemDecoration(
            DividerItemDecoration(requireContext(), layoutManager.orientation)
        )
    }

    private fun observeViewModel() {
        searchViewModel.searchResults.observe(viewLifecycleOwner) { searchResults ->
            setSearchResults(searchResults)
        }
        searchViewModel.isLoading.observe(viewLifecycleOwner) { showLoading(it) }
        handleError(searchViewModel)
    }

    private fun handleError(mainViewModel: SearchViewModel){
        with(binding) {
            mainViewModel.errorMessage.observe(viewLifecycleOwner) { errorMsg ->
                if (!errorMsg.isNullOrEmpty()) {
                    tvErrorMessage.text = errorMsg
                    tvErrorMessage.visibility = View.VISIBLE
                    rvSearchResults.visibility = View.GONE
                    btnRetry.visibility = View.VISIBLE
                } else {
                    tvErrorMessage.visibility = View.GONE
                    rvSearchResults.visibility = View.VISIBLE
                    btnRetry.visibility = View.GONE
                }
            }
            btnRetry.setOnClickListener {
                mainViewModel.retryFetch()
            }
        }
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { searchViewModel.searchEvents(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchJob?.cancel()
                searchJob = viewLifecycleOwner.lifecycleScope.launch {
                    delay(300)
                    if (newText.isNullOrEmpty()) {
                        searchViewModel.resetSearch()
                    } else {
                        searchViewModel.searchEvents(newText)
                    }
                }
                return true
            }
        })
    }

    private fun setSearchResults(eventsList: List<ListEventsItem>) {
        eventsAdapter.submitList(eventsList)

        if (eventsList.isEmpty()) {
            binding.tvEmptyMessage.visibility = View.VISIBLE
            binding.rvSearchResults.visibility = View.GONE
        } else {
            binding.tvEmptyMessage.visibility = View.GONE
            binding.rvSearchResults.visibility = View.VISIBLE
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.rvSearchResults.visibility = if (isLoading) View.GONE else View.VISIBLE
        binding.tvEmptyMessage.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
