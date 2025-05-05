package com.krissphi.id.navigation_module_app.navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.krissphi.id.navigation_module_app.R
import com.krissphi.id.navigation_module_app.databinding.FragmentDetailCategoryBinding


class DetailCategoryFragment : Fragment() {
    private var _binding: FragmentDetailCategoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //using bundle
        val dataName = arguments?.getString(CategoryFragment.EXTRA_NAME)
        val dataDescription = arguments?.getLong(CategoryFragment.EXTRA_STOCK)

        binding.tvCategoryName.text = dataName
        binding.tvCategoryDescription.text = "Stock : $dataDescription"




        //using args
        val dataNameArgs = DetailCategoryFragmentArgs.fromBundle(arguments as Bundle).name
        val dataDescriptionArgs = DetailCategoryFragmentArgs.fromBundle(arguments as Bundle).stock

        binding.tvCategoryNameArgs.text = dataNameArgs
        binding.tvCategoryDescriptionArgs.text = "Stock : $dataDescriptionArgs"

        binding.btnHome.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_detailCategoryFragment_to_homeFragment)
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}