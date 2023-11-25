package com.example.warshop1.activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.warshop1.R
import com.example.warshop1.databinding.ActivityShoppingBinding
import dagger.hilt.android.AndroidEntryPoint

class ShoppingActivity : AppCompatActivity() {

    val binding by  lazy {
        ActivityShoppingBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping)
        try {
            val navController = findNavController(R.id.shoppingHostFragment)
            binding.bottomNavigation.setupWithNavController(navController)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}