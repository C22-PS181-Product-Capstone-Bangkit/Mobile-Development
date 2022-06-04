package com.bangkit.cemil.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.cemil.tools.ApiConfig
import com.bangkit.cemil.tools.model.RestaurantItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RestaurantViewModel: ViewModel() {

    private val _restoData = MutableLiveData<RestaurantItem>()
    val restoData : LiveData<RestaurantItem> = _restoData

    fun getRestaurantById(restaurantId: String){
        val client = ApiConfig.getApiService().getRestaurantById(restaurantId)
        client.enqueue(object: Callback<RestaurantItem>{
            override fun onResponse(call: Call<RestaurantItem>, response: Response<RestaurantItem>) {
                _restoData.value = response.body()
            }

            override fun onFailure(call: Call<RestaurantItem>, t: Throwable) {
            }
        })

    }
}