package com.bangkit.cemil.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.cemil.tools.ApiConfig
import com.bangkit.cemil.tools.model.HistoryItem
import com.bangkit.cemil.tools.model.ProfileResponse
import com.bangkit.cemil.tools.model.RestaurantItem
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryViewModel : ViewModel() {

    private val _historyData = MutableLiveData<List<HistoryItem>?>()
    val historyData : LiveData<List<HistoryItem>?> = _historyData

    fun fetchProfileHistory(accessToken: String){
        val client = ApiConfig.getApiService().getProfile("Bearer $accessToken")
        client.enqueue(object: Callback<ProfileResponse> {
            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                _historyData.value = response.body()?.history
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                Log.e("History", "Fetching Profile failed")
            }
        })
    }

}