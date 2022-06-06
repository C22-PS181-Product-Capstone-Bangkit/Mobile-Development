package com.bangkit.cemil.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.cemil.tools.ApiConfig
import com.bangkit.cemil.tools.model.LikesItem
import com.bangkit.cemil.tools.model.ProfileResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LikesViewModel: ViewModel() {

    private val _likesData = MutableLiveData<List<LikesItem>?>()
    val likesData : LiveData<List<LikesItem>?> = _likesData

    fun fetchLikesData(accessToken: String){
        val client = ApiConfig.getApiService().getProfile("Bearer $accessToken")
        client.enqueue(object: Callback<ProfileResponse> {
            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                _likesData.value = response.body()?.likes
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                Log.e("Likes", "Fetching Profile failed")
            }
        })
    }
}