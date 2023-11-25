package com.example.warshop1.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.warshop1.R
import com.example.warshop1.adapters.HomeViewpagerAdapter
import com.example.warshop1.databinding.FragmentHomeBinding
import com.example.warshop1.fragments.categories.KidsCategoryFragment
import com.example.warshop1.fragments.categories.ManCategoryFragment
import com.example.warshop1.fragments.categories.WomenCategoryFragment
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment: Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoriesFragment = arrayListOf<Fragment>(
            WomenCategoryFragment(),
            ManCategoryFragment(),
            KidsCategoryFragment()
        )
        val viewPager2Adapter = HomeViewpagerAdapter(categoriesFragment, childFragmentManager, lifecycle)
        binding.viewpagerHome.adapter = viewPager2Adapter
        TabLayoutMediator(binding.tabLayout, binding.viewpagerHome){tab, position ->
            when(position){
                0 -> tab.text = "Women"
                1 -> tab.text = "Man"
                2 -> tab.text = "Kids"
            }
        }.attach()
    }

}