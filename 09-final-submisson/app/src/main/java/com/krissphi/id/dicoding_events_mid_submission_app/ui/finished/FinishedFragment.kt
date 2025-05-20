package com.krissphi.id.dicoding_events_mid_submission_app.ui.finished

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.krissphi.id.dicoding_events_mid_submission_app.data.response.ListEventsItem
import com.krissphi.id.dicoding_events_mid_submission_app.databinding.FragmentFinishedBinding
import com.krissphi.id.dicoding_events_mid_submission_app.ui.detail.EventsAdapter

class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null

    private val binding get() = _binding!!
    private lateinit var finishedViewModel : FinishedViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        finishedViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[FinishedViewModel::class.java]

        setupRecycleView()
        observeViewModel()
    }

    private fun setupRecycleView(){
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvEvents.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvEvents.addItemDecoration(itemDecoration)
    }

    private fun observeViewModel() {
        finishedViewModel.listEvent.observe(viewLifecycleOwner) { setListEvents(it) }
        finishedViewModel.isLoading.observe(viewLifecycleOwner) { showLoading(it) }
        handleError(finishedViewModel)
    }

    private fun handleError(finishedViewModel: FinishedViewModel){
        with(binding) {
            finishedViewModel.errorMessage.observe(viewLifecycleOwner) { errorMsg ->
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
                finishedViewModel.retryFetch()
            }
        }
    }

    private fun setListEvents(eventsList: List<ListEventsItem>) {
        val adapter = EventsAdapter()
        adapter.submitList(eventsList)
        binding.rvEvents.adapter = adapter

        if (eventsList.isEmpty()) {
            binding.tvErrorMessage.visibility = View.VISIBLE
            binding.rvEvents.visibility = View.GONE
        } else {
            binding.tvErrorMessage.visibility = View.GONE
            binding.rvEvents.visibility = View.VISIBLE
        }
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