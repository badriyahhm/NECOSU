package com.example.myapplication.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.ProductDetailActivity
import com.example.myapplication.data.response.DataItem
import com.example.myapplication.databinding.ItemProductBinding
import com.squareup.picasso.Picasso

class ProductAdapter : ListAdapter<DataItem, ProductAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val product = getItem(position)
        val context = holder.itemView.context
        holder.bind(product)
        Log.d("name", product.productName.orEmpty())
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProductDetailActivity::class.java)
            intent.putExtra("data", product)
            context.startActivity(intent)
        }
    }

    class MyViewHolder(val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DataItem){
            binding.tvProductname.text = data.productName
            binding.tvProductprice.text = data.price
            Picasso.get().load(data.productImageURL).into(binding.imgProduct)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItem>() {
            override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
