package com.bangkit.cemil.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.cemil.SettingPreferences
import com.bangkit.cemil.tools.ApiConfig
import com.bangkit.cemil.tools.model.RegisterResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel(){

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val _preferenceReady = MutableLiveData<Boolean>()
    val preferenceReady : LiveData<Boolean> = _preferenceReady

    private val _registerResponse = MutableLiveData<RegisterResponse>()
    val registerResponse : LiveData<RegisterResponse> = _registerResponse

    fun requestRegister(name: String, email: String, password: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().registerRequest(name, email, password)
        client.enqueue(object: Callback<RegisterResponse>{
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if(response.isSuccessful ){
                    _registerResponse.value = response.body()
                }else if(response.code() == 400){
                    _registerResponse.value = RegisterResponse(null, null, null, null, null, null, "An account with this email already exists. Please login.")
                }
                _isLoading.value = false
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _registerResponse.value = RegisterResponse(null, null, null, null, null, null, "Registration Failed. Try Again")
                _isLoading.value = false
            }
        })
    }

    fun saveSessionInfo(pref: SettingPreferences, token: String){
        _preferenceReady.value = false
        viewModelScope.launch {
            pref.saveAuthorizationToken(token)
            pref.saveAuthorization(true)
            _preferenceReady.value = true
        }
    }
}