package com.krissphi.id.dicoding_events_mid_submission_app.ui.favorites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.krissphi.id.dicoding_events_mid_submission_app.data.repository.ViewModelFactory
import com.krissphi.id.dicoding_events_mid_submission_app.data.response.ListEventsItem
import com.krissphi.id.dicoding_events_mid_submission_app.databinding.FragmentFavoritesBinding
import com.krissphi.id.dicoding_events_mid_submission_app.ui.detail.EventsAdapter

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null

    private val binding get() = _binding!!
    private lateinit var favoriteViewModel : FavoritesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireActivity().application)
        favoriteViewModel = ViewModelProvider(this, factory)[FavoritesViewModel::class.java]

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
        favoriteViewModel.favorites.observe(viewLifecycleOwner) { favoriteList ->
            val mappedList = favoriteList.map {
                ListEventsItem(
                    id = it.id,
                    name = it.name,
                    beginTime = it.beginTime,
                    endTime = it.endTime,
                    category = it.category,
                    cityName = it.cityName,
                    description = it.description,
                    imageLogo = it.imageLogo,
                    link = it.link,
                    mediaCover = it.mediaCover,
                    ownerName = it.ownerName,
                    quota = it.quota,
                    registrants = it.registrants,
                    summary = it.summary,
                )
            }
            setListEvents(mappedList)
        }
    }

    private fun setListEvents(eventsList: List<ListEventsItem>) {
        val adapter = EventsAdapter()
        adapter.submitList(eventsList)
        binding.rvEvents.adapter = adapter

        if (eventsList.isEmpty()) {
            binding.tvEmptyMessage.visibility = View.VISIBLE
            binding.rvEvents.visibility = View.GONE
            binding.progressBar.visibility = View.GONE
        } else {
            binding.tvEmptyMessage.visibility = View.GONE
            binding.rvEvents.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE

        }
    }


}