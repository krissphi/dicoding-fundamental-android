package com.krissphi.id.navigation_module_app.bottom_navigation.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.krissphi.id.navigation_module_app.databinding.FragmentHome3Binding
import com.krissphi.id.navigation_module_app.databinding.FragmentHomeBinding

class HomeFragment3 : Fragment() {

    private var _binding: FragmentHome3Binding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHome3Binding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.tvHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}