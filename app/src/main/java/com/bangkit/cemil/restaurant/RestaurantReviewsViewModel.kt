package com.bangkit.cemil.restaurant

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.cemil.tools.ApiConfig
import com.bangkit.cemil.tools.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RestaurantReviewsViewModel: ViewModel() {

    private val _restoReviewsData = MutableLiveData<List<RestaurantReviewItem>?>()
    val restoReviewsData : LiveData<List<RestaurantReviewItem>?> = _restoReviewsData

    fun getRestaurantReviews(restaurantId: String){
        val client = ApiConfig.getApiService().getRestaurantById(restaurantId)
        client.enqueue(object: Callback<RestaurantItem> {
            override fun onResponse(call: Call<RestaurantItem>, response: Response<RestaurantItem>) {
                _restoReviewsData.value = response.body()?.review
            }

            override fun onFailure(call: Call<RestaurantItem>, t: Throwable) {
            }
        })
    }
}