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
        placesClient.findAutocompletePredictions(request).addOnSuccessListener { response: FindAutocompletePredictionsResponse ->
           val responseList = response.autocompletePredictions
           for(index in responseList.indices){
               list.add(LocationSearchItem(
                   responseList[index].getPrimaryText(null).toString(),
                   responseList[index].getSecondaryText(null).toString()
               ))
           }
            _listLocations.value = list
       }.addOnFailureListener {
            Log.e("LocationViewModel", "onFailure: ${it.message}")
       }
    }
}