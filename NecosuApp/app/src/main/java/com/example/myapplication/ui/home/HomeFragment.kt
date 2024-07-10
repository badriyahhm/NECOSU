package com.example.myapplication.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.adapter.ProductAdapter
import com.example.myapplication.adapter.SpinnerAdapter
import com.example.myapplication.data.response.DataItem
import com.example.myapplication.data.response.ProductResponse
import com.example.myapplication.data.retrofit.ApiConfig
import com.example.myapplication.databinding.FragmentHomeBinding
import com.example.myapplication.ui.SearchProductImgActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var spinnerLocation: Spinner
    private lateinit var adapter: ProductAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        (activity as AppCompatActivity).supportActionBar?.hide()

        spinnerLocation = root.findViewById(R.id.spinner_location)

        val spinnerAdapter = SpinnerAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            android.R.id.text1,  // The ID of the TextView in simple_spinner_dropdown_item
            resources.getStringArray(R.array.spinner_items).toList()
        )
        spinnerLocation.adapter = spinnerAdapter

        adapter = ProductAdapter()
        binding.rvProduct.adapter = adapter
        remoteGetProduct()

        val imgSearchIcon: ImageView = root.findViewById(R.id.imgSearch_icon)
        imgSearchIcon.setOnClickListener {
            val intent = Intent(requireContext(), SearchProductImgActivity::class.java)
            startActivity(intent)
        }

        return root
    }

    fun remoteGetProduct() {
        ApiConfig.getApiService().getAllProduct().enqueue(object: Callback<ProductResponse> {
            override fun onResponse(
                call: Call<ProductResponse>,
                response: Response<ProductResponse>
            ) {
                if (response.isSuccessful) {
                    val productResponseList = response.body()
                    Log.d("API Response", productResponseList.toString())
                    if (productResponseList != null) {
                        val data = productResponseList.data ?: emptyList()
                        setDataToAdapter(data)
                    }
                }
            }

            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    fun setDataToAdapter(data: List<DataItem>) {
        Log.d("Adapter Data", data.toString())
        adapter.submitList(data)
    }


    override fun onDestroyView() {
        // Show the activity's action bar when the fragment is destroyed
        (activity as AppCompatActivity).supportActionBar?.show()
        super.onDestroyView()
    }
}