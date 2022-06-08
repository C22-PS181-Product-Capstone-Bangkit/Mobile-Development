package com.bangkit.cemil.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.cemil.tools.ApiConfig
import com.bangkit.cemil.tools.model.ProfileResponse
import com.bangkit.cemil.tools.model.ReviewItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReviewsViewModel: ViewModel() {

    private val _userReviews = MutableLiveData<List<ReviewItem>?>()
    val userReviews : LiveData<List<ReviewItem>?> = _userReviews

    fun fetchUserReviews(accessToken: String){
        val client = ApiConfig.getApiService().getProfile("Bearer $accessToken")
        client.enqueue(object: Callback<ProfileResponse>{
            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                if(response.isSuccessful){
                    _userReviews.value = response.body()?.review
                }
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
            }
        })
    }


}