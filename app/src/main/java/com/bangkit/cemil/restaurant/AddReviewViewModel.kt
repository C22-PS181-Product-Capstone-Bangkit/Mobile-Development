package com.bangkit.cemil.restaurant

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.cemil.tools.ApiConfig
import com.bangkit.cemil.tools.model.AddReviewResponse
import com.bangkit.cemil.tools.model.RestaurantItem
import com.bangkit.cemil.tools.model.RestaurantReviewItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddReviewViewModel: ViewModel() {

    private val _addReviewResponse = MutableLiveData<AddReviewResponse>()
    val addReviewResponse : LiveData<AddReviewResponse> = _addReviewResponse

    private val _restoData = MutableLiveData<RestaurantItem>()
    val restoData : LiveData<RestaurantItem> = _restoData

    fun postReview(accessToken: String, restoId: String, rating: Double, description: String){
        val client = ApiConfig.getApiService().postReview("Bearer $accessToken", restoId, rating, description)
        client.enqueue(object: Callback<AddReviewResponse>{
            override fun onResponse(call: Call<AddReviewResponse>, response: Response<AddReviewResponse>) {
                if(response.code() == 400){
                    _addReviewResponse.value = AddReviewResponse(null, null,
                        null, null, null, null, null, "You already reviewed this restaurant.")
                }else{
                    _addReviewResponse.value = response.body()
                }
            }

            override fun onFailure(call: Call<AddReviewResponse>, t: Throwable) {
                _addReviewResponse.value =
                    AddReviewResponse(null, null,
                        null, null, null, null, null, t.message)
            }
        })
    }

    fun getRestaurantData(restaurantId: String){
        val client = ApiConfig.getApiService().getRestaurantById(restaurantId)
        client.enqueue(object: Callback<RestaurantItem> {
            override fun onResponse(call: Call<RestaurantItem>, response: Response<RestaurantItem>) {
                _restoData.value = response.body()
            }

            override fun onFailure(call: Call<RestaurantItem>, t: Throwable) {
            }
        })
    }
}