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
import com.example.warshop1.adapters.ManAdapter
import com.example.warshop1.adapters.WomenAdapter
import com.example.warshop1.databinding.FragmentManCategoryBinding
import com.example.warshop1.helper.viewmodel.ManCategoryViewModel
import com.example.warshop1.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private val TAG = "ManCategoryFragment"
@AndroidEntryPoint
class ManCategoryFragment: Fragment(R.layout.fragment_man_category) {
    private lateinit var binding: FragmentManCategoryBinding
    private lateinit var manProductsAdapter: ManAdapter
    private val viewModel by viewModels<ManCategoryViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentManCategoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupManProductsRv()
        lifecycleScope.launch {
            viewModel.manProduct.collectLatest {
                when(it){
                    is Resource.Loading ->{
                        showLoading()
                    }
                    is Resource.Success ->{
                        manProductsAdapter.differ.submitList(it.data)
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
        binding.manCategoryProgressbar.visibility = View.GONE
    }

    private fun showLoading() {
        binding.manCategoryProgressbar.visibility = View.VISIBLE
    }

    private fun setupManProductsRv() {
        manProductsAdapter = ManAdapter()
        binding.rvManProducts.apply {
            layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            adapter = manProductsAdapter
        }


    }
}