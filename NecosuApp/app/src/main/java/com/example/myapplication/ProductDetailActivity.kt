package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.data.response.DataItem
import com.example.myapplication.databinding.ActivityProductDetailBinding
import com.squareup.picasso.Picasso

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = intent.getParcelableExtra<DataItem>("data")

        if (data != null) {
            binding.tvProductname.text = data.productName.orEmpty()
            binding.tvDescription.text = data.description.orEmpty()
            binding.tvProductprice.text = data.price.orEmpty()

            Picasso.get().load(data.productImageURL).into(binding.imgProduct)
        }
    }
}
