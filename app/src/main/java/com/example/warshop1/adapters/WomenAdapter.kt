package com.example.warshop1.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.warshop1.data.Product
import com.example.warshop1.databinding.WomenRvItemBinding

class WomenAdapter: RecyclerView.Adapter<WomenAdapter.WomenViewHolder>() {

    inner class WomenViewHolder(private val binding: WomenRvItemBinding):
        RecyclerView.ViewHolder(binding.root){
            fun bind(product: Product){
                binding.apply{
                    Glide.with(itemView).load(product.images[0]).into(imgWomen)
                    tvPrice.text = "Rp. ${product.price}"
                    tvName.text = product.name
                }
                val screenWidth = itemView.resources.displayMetrics.widthPixels
                val layoutParams = itemView.layoutParams
                layoutParams.width = screenWidth / 2
                itemView.layoutParams = layoutParams
            }
        }

    val diffCallback = object : DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WomenViewHolder {
        return WomenViewHolder(
            WomenRvItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: WomenViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bind(product)
    }


}