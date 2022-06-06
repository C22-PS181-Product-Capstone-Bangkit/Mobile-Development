package com.bangkit.cemil.restaurant

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.cemil.tools.ApiConfig
import com.bangkit.cemil.tools.model.LikeResponse
import com.bangkit.cemil.tools.model.RestaurantItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RestaurantViewModel: ViewModel() {

    private val _restoData = MutableLiveData<RestaurantItem>()
    val restoData : LiveData<RestaurantItem> = _restoData

    private val _restoLike = MutableLiveData<LikeResponse>()
    val restoLike : LiveData<LikeResponse> = _restoLike

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

    fun postRestaurantLike(accessToken: String, idRestaurant: String){
        val client = ApiConfig.getApiService().postLike("Bearer $accessToken", idRestaurant)
        client.enqueue(object: Callback<LikeResponse>{
            override fun onResponse(call: Call<LikeResponse>, response: Response<LikeResponse>) {
                if(response.code() == 400){
                    _restoLike.value = LikeResponse(null, null, null, "Already Favorited")
                }else _restoLike.value = response.body()
            }

            override fun onFailure(call: Call<LikeResponse>, t: Throwable) {
                _restoLike.value = LikeResponse(null, null, null, "Server Error")
            }
        })

    }
}