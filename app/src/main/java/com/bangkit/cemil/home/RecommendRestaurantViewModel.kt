package com.bangkit.cemil.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.cemil.tools.ApiConfig
import com.bangkit.cemil.tools.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RecommendRestaurantViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val _recommendationList = MutableLiveData<List<RestaurantItem>>()
    val recommendationList : LiveData<List<RestaurantItem>> = _recommendationList

    private val _profileData = MutableLiveData<ProfileResponse>()
    val profileData: LiveData<ProfileResponse> = _profileData

    private val _restoLike = MutableLiveData<LikeResponse>()
    val restoLike : LiveData<LikeResponse> = _restoLike

    private val _restoDeleteLike = MutableLiveData<LikeDeleteResponse>()
    val restoDeleteLike : LiveData<LikeDeleteResponse> = _restoDeleteLike

    private val _historyResponse = MutableLiveData<HistoryResponseItem>()
    val historyResponse : LiveData<HistoryResponseItem> = _historyResponse

    fun fetchRecommendations(accessToken: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().fetchRecommendations("Bearer $accessToken")
        client.enqueue(object: Callback<List<RestaurantItem>>{
            override fun onResponse(call: Call<List<RestaurantItem>>, response: Response<List<RestaurantItem>>) {
                if(response.isSuccessful){
                    _recommendationList.value = response.body()
                }
                _isLoading.value = false
            }

            override fun onFailure(call: Call<List<RestaurantItem>>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }

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

    fun postHistory(accessToken: String, idRestaurant: String){
        val client = ApiConfig.getApiService().postHistory("Bearer $accessToken", idRestaurant)
        client.enqueue(object: Callback<HistoryResponseItem>{
            override fun onResponse(call: Call<HistoryResponseItem>, response: Response<HistoryResponseItem>) {
                _historyResponse.value = response.body()
            }

            override fun onFailure(call: Call<HistoryResponseItem>, t: Throwable) {
            }
        })
    }


}