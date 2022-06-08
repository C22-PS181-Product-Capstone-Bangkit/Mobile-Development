package com.bangkit.cemil.restaurant

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.cemil.tools.ApiConfig
import com.bangkit.cemil.tools.model.LikeDeleteResponse
import com.bangkit.cemil.tools.model.LikeResponse
import com.bangkit.cemil.tools.model.ProfileResponse
import com.bangkit.cemil.tools.model.RestaurantItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RestaurantViewModel: ViewModel() {

    private val _restoData = MutableLiveData<RestaurantItem>()
    val restoData : LiveData<RestaurantItem> = _restoData

    private val _restoLike = MutableLiveData<LikeResponse>()
    val restoLike : LiveData<LikeResponse> = _restoLike

    private val _profileData = MutableLiveData<ProfileResponse>()
    val profileData: LiveData<ProfileResponse> = _profileData

    private val _restoDeleteLike = MutableLiveData<LikeDeleteResponse>()
    val restoDeleteLike : LiveData<LikeDeleteResponse> = _restoDeleteLike

    fun fetchProfile(accessToken: String) {
        val client = ApiConfig.getApiService().getProfile("Bearer $accessToken")
        client.enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                _profileData.value = response.body()
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                _profileData.value = ProfileResponse(null, null, null, null, t.message, null)
            }
        })
    }

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

    fun deleteRestaurantLike(accessToken: String, likeId: String){
        val client = ApiConfig.getApiService().deleteLike("Bearer $accessToken", likeId)
        client.enqueue(object: Callback<LikeDeleteResponse>{
            override fun onResponse(call: Call<LikeDeleteResponse>, response: Response<LikeDeleteResponse>) {
                _restoDeleteLike.value = response.body()
            }

            override fun onFailure(call: Call<LikeDeleteResponse>, t: Throwable) {
                _restoDeleteLike.value = LikeDeleteResponse(t.message)
            }
        })
    }



}