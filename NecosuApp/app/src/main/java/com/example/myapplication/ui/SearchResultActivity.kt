package com.example.myapplication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.adapter.ProductAdapter
import com.example.myapplication.data.response.DataItem
import com.example.myapplication.data.response.ProductResponse
import com.example.myapplication.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchResultActivity : AppCompatActivity() {
    private lateinit var tvResultChara: TextView
    private lateinit var adapter: ProductAdapter
    private val characterProductIds = mapOf(
        "Ayaka" to listOf(1, 2),
        "Killua" to listOf(8),
        "Zoro" to listOf(6),
        "Asuka" to listOf(7),
        "Kurumi" to listOf(5)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)

        tvResultChara = findViewById(R.id.tv_resultChara)

        val prediction = intent.getStringExtra("prediction")
        tvResultChara.text = prediction

        val recyclerView: RecyclerView = findViewById(R.id.rvProductResult)
        adapter = ProductAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        // Get the character name from the intent
        val characterName = intent.getStringExtra("prediction")

        // Fetch data and filter by character name
        remoteGetFilteredProduct(characterName)
    }

    private fun remoteGetFilteredProduct(characterName: String?) {
        ApiConfig.getApiService().getAllProduct().enqueue(object : Callback<ProductResponse> {
            override fun onResponse(
                call: Call<ProductResponse>,
                response: Response<ProductResponse>
            ) {
                if (response.isSuccessful) {
                    val productResponseList = response.body()
                    Log.d("API Response", productResponseList.toString())
                    if (productResponseList != null) {
                        val data = productResponseList.data ?: emptyList()
                        val filteredData = filterDataByCharacter(data, characterName)
                        setDataToAdapter(filteredData)
                    }
                }
            }

            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                // Handle failure
            }
        })
    }

    private fun setDataToAdapter(data: List<DataItem>) {
        adapter.submitList(data)
    }

    private fun filterDataByCharacter(data: List<DataItem>, characterName: String?): List<DataItem> {
        val productIds = characterProductIds[characterName]
        return data.filter { product ->
            productIds?.contains(product.id) ?: false
        }
    }
}