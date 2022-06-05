package com.bangkit.cemil.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.cemil.tools.ApiConfig
import com.bangkit.cemil.tools.model.EditResponse
import com.bangkit.cemil.tools.model.ProfileResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfileViewModel : ViewModel() {

    private val _profileData = MutableLiveData<ProfileResponse>()
    val profileData : LiveData<ProfileResponse> = _profileData

    private val _editResponse = MutableLiveData<EditResponse>()
    val editResponse : LiveData<EditResponse> = _editResponse

    fun fetchProfile(accessToken  : String){
        val client = ApiConfig.getApiService().getProfile("Bearer $accessToken")
        client.enqueue(object: Callback<ProfileResponse>{
            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                _profileData.value = response.body()
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                _profileData.value = ProfileResponse(null, null, null, null, t.message, null)
            }
        })
    }

    fun postEditProfile(accessToken: String, name: String, email: String, phone: String){
        val client = ApiConfig.getApiService().postEditProfile("Bearer $accessToken", name, email, phone)
        client.enqueue(object: Callback<EditResponse>{
            override fun onResponse(call: Call<EditResponse>, response: Response<EditResponse>) {
                _editResponse.value = response.body()
            }

            override fun onFailure(call: Call<EditResponse>, t: Throwable) {
                _editResponse.value = EditResponse(t.message)
            }
        })
    }
}