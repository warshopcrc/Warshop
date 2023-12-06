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
class ManCategoryViewModel@Inject constructor(
    private val firestore: FirebaseFirestore
): ViewModel()  {
    private val _manProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val manProduct: StateFlow<Resource<List<Product>>> = _manProducts

    init {
        fetchManProduct()
    }

    fun fetchManProduct(){
        viewModelScope.launch {
            _manProducts.emit(Resource.Loading())
        }
        firestore.collection("Products")
            .whereEqualTo("category","Man").get().addOnSuccessListener { result ->
                val manProductList = result.toObjects(Product::class.java)
                viewModelScope.launch {
                    _manProducts.emit(Resource.Success(manProductList))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _manProducts.emit(Resource.Error(it.message.toString()))
                }
            }
    }
}