package com.bangkit.cemil.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.cemil.SettingPreferences
import com.bangkit.cemil.tools.ApiConfig
import com.bangkit.cemil.tools.model.LoginResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {

    private val _preferenceReady = MutableLiveData<Boolean>()
    val preferenceReady : LiveData<Boolean> = _preferenceReady

    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse : LiveData<LoginResponse> = _loginResponse

    fun requestLogin(email: String, password: String){
        val client = ApiConfig.getApiService().loginRequest(email, password)
        client.enqueue(object: Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
               if(response.isSuccessful ){
                   _loginResponse.value = response.body()
               }else if(response.code() == 400){
                   _loginResponse.value = LoginResponse(null, "Wrong email or password")
               }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _loginResponse.value = LoginResponse(null, "Server Error")
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