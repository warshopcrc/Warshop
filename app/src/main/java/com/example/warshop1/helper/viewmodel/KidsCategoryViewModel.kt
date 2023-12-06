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
class KidsCategoryViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
): ViewModel()  {
    private val _kidsProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val kidsProduct: StateFlow<Resource<List<Product>>> = _kidsProducts

    init {
        fetchKidsProduct()
    }

    fun fetchKidsProduct(){
        viewModelScope.launch {
            _kidsProducts.emit(Resource.Loading())
        }
        firestore.collection("Products")
            .whereEqualTo("category","Kids").get().addOnSuccessListener { result ->
                val kidsProductList = result.toObjects(Product::class.java)
                viewModelScope.launch {
                    _kidsProducts.emit(Resource.Success(kidsProductList))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _kidsProducts.emit(Resource.Error(it.message.toString()))
                }
            }
    }
}