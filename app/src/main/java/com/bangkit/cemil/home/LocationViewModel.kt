package com.bangkit.cemil.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.cemil.tools.LocationSearchItem
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient

class LocationViewModel: ViewModel() {

    private val _listLocations = MutableLiveData<List<LocationSearchItem>>()
    val listLocations : LiveData<List<LocationSearchItem>> = _listLocations

    fun searchQueryLocation(query : String?, placesClient: PlacesClient){
        val request = FindAutocompletePredictionsRequest.builder().setQuery(query).build()
        val list = ArrayList<LocationSearchItem>()
        Log.e("LocationViewModel", "Querying Location...")
        placesClient.findAutocompletePredictions(request).addOnSuccessListener { response: FindAutocompletePredictionsResponse ->
           val responseList = response.autocompletePredictions
           for(index in responseList.indices){
               list.add(LocationSearchItem(
                   responseList[index].getPrimaryText(null).toString(),
                   responseList[index].getSecondaryText(null).toString()))
           }
            _listLocations.value = list
           Log.e("LocationViewModel", "onSuccess: $list")
       }.addOnFailureListener {
            Log.e("LocationViewModel", "onFailure: ${it.message}")
        }
        //Ini yg pake geocoder, hasil query result bakal 1 spesifik, walaupun maxResultnya dikasih 5
//        val locationAddresses = geocoder.getFromLocationName(query, 5)
//        val list = ArrayList<LocationSearchItem>()
//        if(locationAddresses != null){
//            for(index in locationAddresses.indices){
//                val locationAddress = locationAddresses[index]
//                list.add(LocationSearchItem(
//                    locationAddress.featureName,
//                    locationAddress.getAddressLine(0).replace("${locationAddress.featureName},", "").trim()
//                ))
//            }
//            Log.e("LocationViewModel", "onFailure: $list")
//        }else{
//            Log.e("LocationViewModel", "onFailure: location null")
//        }
//        _listLocations.value = list
    }
}