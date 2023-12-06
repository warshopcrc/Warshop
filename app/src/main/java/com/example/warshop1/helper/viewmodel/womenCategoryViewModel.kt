package com.example.warshop1.helper.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.warshop1.data.Product
import com.example.warshop1.util.Resource
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class womenCategoryViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
): ViewModel() {
    private val _womenProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val womenProduct: StateFlow<Resource<List<Product>>> = _womenProducts
    private val pagingInfo = PagingInfo()

    init {
        fetchWomenProduct()
    }

    fun fetchWomenProduct(){
        if (!pagingInfo.isPagingEnd) {
            viewModelScope.launch {
                _womenProducts.emit(Resource.Loading())
                firestore.collection("Products")
                    .whereEqualTo("category", "Women").limit(pagingInfo.mainpage * 10).get()
                    .addOnSuccessListener { result ->
                        val womenProductList = result.toObjects(Product::class.java)
                        pagingInfo.isPagingEnd = womenProductList == pagingInfo.oldWomenProducts
                        pagingInfo.oldWomenProducts = womenProductList
                        viewModelScope.launch {
                            _womenProducts.emit(Resource.Success(womenProductList))
                        }
                        pagingInfo.mainpage++
                    }.addOnFailureListener {
                        viewModelScope.launch {
                            _womenProducts.emit(Resource.Error(it.message.toString()))
                        }
                    }
            }
        }
    }
}

internal data class PagingInfo(
    var mainpage: Long = 1,
    var oldWomenProducts: List<Product> = emptyList(),
    var isPagingEnd: Boolean = false
)