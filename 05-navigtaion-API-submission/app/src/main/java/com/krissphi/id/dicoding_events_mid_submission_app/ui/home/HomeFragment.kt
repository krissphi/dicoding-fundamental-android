package com.krissphi.id.dicoding_events_mid_submission_app.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayoutMediator
import com.krissphi.id.dicoding_events_mid_submission_app.data.response.ListEventsItem
import com.krissphi.id.dicoding_events_mid_submission_app.databinding.FragmentHomeBinding
import com.krissphi.id.dicoding_events_mid_submission_app.ui.EventsAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    private lateinit var mainViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[HomeViewModel::class.java]

        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFinished.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvFinished.addItemDecoration(itemDecoration)
    }

    private fun observeViewModel() {
        mainViewModel.upcomingEvents.observe(viewLifecycleOwner) { setUpcomingEvents(it) }
        mainViewModel.finishedEvents.observe(viewLifecycleOwner) { setFinishedEvents(it) }
        mainViewModel.isLoading.observe(viewLifecycleOwner) { showLoading(it) }
        handleError(mainViewModel)
    }

    private fun handleError(mainViewModel: HomeViewModel){
        with(binding) {
            mainViewModel.errorMessage.observe(viewLifecycleOwner) { errorMsg ->
                if (!errorMsg.isNullOrEmpty()) {
                    tvErrorMessage.text = errorMsg
                    tvErrorMessage.visibility = View.VISIBLE
                    contentLayout.visibility = View.GONE
                    btnRetry.visibility = View.VISIBLE
                } else {
                    tvErrorMessage.visibility = View.GONE
                    contentLayout.visibility = View.VISIBLE
                    btnRetry.visibility = View.GONE
                }
            }

            btnRetry.setOnClickListener {
                mainViewModel.retryFetch()
            }
        }
    }

    private fun setUpcomingEvents(eventsList: List<ListEventsItem>) {
        val adapter = CarouselAdapter()
        adapter.submitList(eventsList)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { _, _ -> }.attach()
    }

    private fun setFinishedEvents(eventsList: List<ListEventsItem>) {
        val adapter = EventsAdapter()
        adapter.submitList(eventsList)
        binding.rvFinished.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.contentLayout.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}