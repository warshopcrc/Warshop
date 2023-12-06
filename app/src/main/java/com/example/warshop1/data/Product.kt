package com.example.warshop1.data

data class Product (
    val id: String,
    val name: String,
    val category: String,
    val price: Int,
    val condition: Int,
    val description: String? = null,
    val sizes: List<String>? = null,
    val images: List<String>
){
    constructor(): this("0","","",0,0,images = emptyList())
}