package com.krissphi.id.dicoding_events_mid_submission_app.ui.upcoming

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.krissphi.id.dicoding_events_mid_submission_app.data.response.ListEventsItem
import com.krissphi.id.dicoding_events_mid_submission_app.databinding.FragmentUpcomingBinding
import com.krissphi.id.dicoding_events_mid_submission_app.ui.EventsAdapter

class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!
    private lateinit var upcomingViewModel: UpcomingViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        upcomingViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[UpcomingViewModel::class.java]

        setupRecylerView()
        observeViewModel()

    }

    private fun setupRecylerView(){
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvEvents.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvEvents.addItemDecoration(itemDecoration)
    }

    private fun observeViewModel() {
        upcomingViewModel.listEvent.observe(viewLifecycleOwner) { setListEvents(it) }
        upcomingViewModel.isLoading.observe(viewLifecycleOwner) { showLoading(it) }
        handleError(upcomingViewModel)
    }

    private fun handleError(upcomingViewModel: UpcomingViewModel){
        with(binding) {
            upcomingViewModel.errorMessage.observe(viewLifecycleOwner) { errorMsg ->
                if (!errorMsg.isNullOrEmpty()) {
                    tvErrorMessage.text = errorMsg
                    tvErrorMessage.visibility = View.VISIBLE
                    rvEvents.visibility = View.GONE
                    btnRetry.visibility = View.VISIBLE
                } else {
                    tvErrorMessage.visibility = View.GONE
                    rvEvents.visibility = View.VISIBLE
                    btnRetry.visibility = View.GONE
                }
                }
            btnRetry.setOnClickListener {
                upcomingViewModel.retryFetch()
            }
        }
    }



    private fun setListEvents(eventsList: List<ListEventsItem>) {
        val adapter = EventsAdapter()
        adapter.submitList(eventsList)
        binding.rvEvents.adapter = adapter
    }
    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}