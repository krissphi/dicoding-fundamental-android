package com.krissphi.id.navigation_module_app.tab_layout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
//        return 2
        return 3
    }

    override fun createFragment(position: Int): Fragment {
//        var fragment: Fragment? = null
//        when (position) {
//            0 -> fragment = HomeFragment4()
//            1 -> fragment = ProfileFragment2()
//        }
//        return fragment as Fragment

        val fragment = HomeFragment4()
        fragment.arguments = Bundle().apply {
            putInt(HomeFragment4.ARG_SECTION_NUMBER, position + 1)
        }
        return fragment
    }

}