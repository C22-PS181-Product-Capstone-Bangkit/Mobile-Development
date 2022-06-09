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

    private val _restoRecentlyVisitedData = MutableLiveData<List<RestaurantItem>>()
    val restoRecentlyVisitedData : LiveData<List<RestaurantItem>> = _restoRecentlyVisitedData

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

    fun requestRestoFromIds(restoIds : ArrayList<String>){
        val client = ApiConfig.getApiService().fetchRestaurantByListIds(restoIds)
        client.enqueue(object: Callback<List<RestaurantItem>>{
            override fun onResponse(call: Call<List<RestaurantItem>>, response: Response<List<RestaurantItem>>) {
                if(response.isSuccessful){
                    _restoRecentlyVisitedData.value = response.body()
                }
            }

            override fun onFailure(call: Call<List<RestaurantItem>>, t: Throwable) {
            }
        })
    }

}