package com.bangkit.cemil.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.cemil.tools.ApiConfig
import com.bangkit.cemil.tools.model.RestaurantItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel: ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val _listRestos = MutableLiveData<List<RestaurantItem>>()
    val listRestos : LiveData<List<RestaurantItem>> = _listRestos

    fun searchRestos(searchQuery: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getRestaurantByName(searchQuery)
        client.enqueue(object: Callback<List<RestaurantItem>>{
            override fun onResponse(call: Call<List<RestaurantItem>>, response: Response<List<RestaurantItem>>) {
                if(response.isSuccessful){
                    if(response.body() != null){
                        _listRestos.value = response.body()
                        _isLoading.value = false
                    }
                }
            }
            override fun onFailure(call: Call<List<RestaurantItem>>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }

    fun fetchRestosByCategory(category: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getRestaurantByCategory(category)
        client.enqueue(object: Callback<List<RestaurantItem>>{
            override fun onResponse(call: Call<List<RestaurantItem>>, response: Response<List<RestaurantItem>>) {
                if(response.isSuccessful){
                    if(response.body() != null){
                        _listRestos.value = response.body()
                        _isLoading.value = false
                    }
                }
            }
            override fun onFailure(call: Call<List<RestaurantItem>>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }
}