package com.example.myapplication.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class ProductResponse(

	@field:SerializedName("data")
	val data: List<DataItem>? = null,

	@field:SerializedName("success")
	val success: Boolean? = null
)

@Parcelize
data class DataItem(

	@field:SerializedName("Description")
	val description: String? = null,

	@field:SerializedName("RenterUserId")
	val renterUserId: Int? = null,

	@field:SerializedName("ProductName")
	val productName: String? = null,

	@field:SerializedName("Price")
	val price: String? = null,

	@field:SerializedName("StockQuantity")
	val stockQuantity: Int? = null,

	@field:SerializedName("ProductImageURL")
	val productImageURL: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
): Parcelable
