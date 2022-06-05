package com.bangkit.cemil.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.cemil.tools.ApiConfig
import com.bangkit.cemil.tools.model.RestaurantItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel: ViewModel() {

    private val _restoData = MutableLiveData<List<RestaurantItem>>()
    val restoData : LiveData<List<RestaurantItem>> = _restoData

    fun requestRestoData(){
        val client = ApiConfig.getApiService().getRestaurant()
        client.enqueue(object: Callback<List<RestaurantItem>>{
            override fun onResponse(call: Call<List<RestaurantItem>>, response: Response<List<RestaurantItem>>) {
                _restoData.value = response.body()
            }

            override fun onFailure(call: Call<List<RestaurantItem>>, t: Throwable) {
            }
        })

    }

}