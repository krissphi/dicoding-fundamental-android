package com.krissphi.id.navigation_module_app

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.krissphi.id.navigation_module_app.databinding.FragmentHomeBinding
import androidx.navigation.Navigation
import androidx.navigation.findNavController

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCategory.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_homeFragment_to_categoryFragment)
        )
        binding.btnProfile.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_profileActivity)
        }
        binding.btnOption.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_optionActivity)
        }
        binding.btnDrawer.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_drawerActivity)
        }
        binding.btnBottom.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_bottomActivity)
        }
        binding.btnTab.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_tabActivity)
        }



    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}