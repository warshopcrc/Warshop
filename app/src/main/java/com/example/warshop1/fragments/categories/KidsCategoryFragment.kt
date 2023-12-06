package com.example.warshop1.fragments.categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.warshop1.R
import com.example.warshop1.adapters.KidsAdapter
import com.example.warshop1.adapters.WomenAdapter
import com.example.warshop1.databinding.FragmentKidsCategoryBinding
import com.example.warshop1.databinding.FragmentWomenCategoryBinding
import com.example.warshop1.helper.viewmodel.KidsCategoryViewModel
import com.example.warshop1.helper.viewmodel.womenCategoryViewModel
import com.example.warshop1.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private val TAG = "WomenCategoryFragment"
@AndroidEntryPoint
class KidsCategoryFragment: Fragment(R.layout.fragment_kids_category) {
    private lateinit var binding: FragmentKidsCategoryBinding
    private lateinit var kidsProductsAdapter: KidsAdapter
    private val viewModel by viewModels<KidsCategoryViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentKidsCategoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupKidsProductsRv()
        lifecycleScope.launch {
            viewModel.kidsProduct.collectLatest {
                when(it){
                    is Resource.Loading ->{
                        showLoading()
                    }
                    is Resource.Success ->{
                        kidsProductsAdapter.differ.submitList(it.data)
                        hideLoading()
                    }
                    is Resource.Error ->{
                        hideLoading()
                        Log.e(TAG,it.message.toString())
                        Toast.makeText(requireContext(),it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun hideLoading() {
        binding.kidsCategoryProgressbar.visibility = View.GONE
    }

    private fun showLoading() {
        binding.kidsCategoryProgressbar.visibility = View.VISIBLE
    }

    private fun setupKidsProductsRv() {
        kidsProductsAdapter = KidsAdapter()
        binding.rvKidsProducts.apply {
            layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            adapter = kidsProductsAdapter
        }


    }
}