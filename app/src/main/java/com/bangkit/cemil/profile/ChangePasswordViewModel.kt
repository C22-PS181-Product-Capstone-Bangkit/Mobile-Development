package com.bangkit.cemil.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.cemil.tools.ApiConfig
import com.bangkit.cemil.tools.model.ChangePasswordResponse
import com.bangkit.cemil.tools.model.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordViewModel : ViewModel() {

    private val _changePassResponse = MutableLiveData<ChangePasswordResponse>()
    val changePassResponse : LiveData<ChangePasswordResponse> = _changePassResponse

    fun requestChangePassword(accessToken: String, oldPassword: String, password: String){
        val token = "Bearer $accessToken"
        val client = ApiConfig.getApiService().putChangePassword(token, oldPassword, password)
        client.enqueue(object: Callback<ChangePasswordResponse>{
            override fun onResponse(call: Call<ChangePasswordResponse>, response: Response<ChangePasswordResponse>) {
                if(response.code() == 401){
                    _changePassResponse.value = ChangePasswordResponse("Change password failed. Try checking your current password.")
                }else _changePassResponse.value = response.body()
            }

            override fun onFailure(call: Call<ChangePasswordResponse>, t: Throwable) {
                _changePassResponse.value = ChangePasswordResponse("Server failed.")
            }
        })
    }
}