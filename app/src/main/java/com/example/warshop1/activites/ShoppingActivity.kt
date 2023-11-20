package com.example.warshop1.activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.warshop1.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShoppingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping)
    }
}