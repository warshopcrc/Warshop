package com.example.warshop1.fragments.categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.warshop1.R
import com.example.warshop1.adapters.WomenAdapter
import com.example.warshop1.databinding.FragmentWomenCategoryBinding
import com.example.warshop1.helper.viewmodel.womenCategoryViewModel
import com.example.warshop1.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private val TAG = "WomenCategoryFragment"
@AndroidEntryPoint
class WomenCategoryFragment: Fragment(R.layout.fragment_women_category) {
    private lateinit var binding: FragmentWomenCategoryBinding
    private lateinit var womenProductsAdapter: WomenAdapter
    private val viewModel by viewModels<womenCategoryViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWomenCategoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupWomenProductsRv()
        lifecycleScope.launch {
            viewModel.womenProduct.collectLatest {
                when(it){
                    is Resource.Loading ->{
                        binding.womenProductsProgressbar.visibility = View.VISIBLE
                    }
                    is Resource.Success ->{
                        womenProductsAdapter.differ.submitList(it.data)
                        binding.womenProductsProgressbar.visibility = View.GONE
                    }
                    is Resource.Error ->{
                        Log.e(TAG,it.message.toString())
                        Toast.makeText(requireContext(),it.message,Toast.LENGTH_SHORT).show()
                        binding.womenProductsProgressbar.visibility = View.GONE
                    }
                    else -> Unit
                }
            }
        }
        binding.nestedScrollWomenCategory.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener{ v,_,scrollY,_,_ ->
            if (v.getChildAt(0).bottom <= v.height + scrollY){
                viewModel.fetchWomenProduct()
            }
        })
    }

    private fun hideLoading() {
        binding.womenCategoryProgressbar.visibility = View.GONE
    }

    private fun showLoading() {
        binding.womenCategoryProgressbar.visibility = View.VISIBLE
    }

    private fun setupWomenProductsRv() {
        womenProductsAdapter = WomenAdapter()
        binding.rvWomenProducts.apply {
            layoutManager = GridLayoutManager(requireContext(), 2,GridLayoutManager.VERTICAL, false)
            adapter = womenProductsAdapter
        }


    }
}