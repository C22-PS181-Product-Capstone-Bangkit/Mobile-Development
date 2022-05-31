package com.bangkit.cemil.home

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.bangkit.cemil.SettingPreferences
import com.bangkit.cemil.dataStore
import com.bangkit.cemil.databinding.FragmentHomeBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.launch
import java.util.*

class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var locationAddress : String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pref = SettingPreferences.getInstance(requireContext().dataStore)
        lifecycleScope.launch {
            locationAddress = pref.getPreferences()[SettingPreferences.LOCATION_KEY].toString()
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        if(locationAddress != "null"){
            binding.tvCurrentLocation.text = locationAddress
        }else{
            getMyLocation()
        }

        binding.tvCurrentLocation.setOnClickListener {
            navigateToLocationFragment()
        }
        binding.tvCurrentLocationLabel.setOnClickListener {
            navigateToLocationFragment()
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){ permissions ->
        when{
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> getMyLocation()
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> getMyLocation()
            else -> Toast.makeText(requireContext(), "Location permisson needed. Try again.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getMyLocation(){
        if(checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION) && checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location : Location? ->
                if(location != null){
                    val addresses = Geocoder(requireContext()).getFromLocation(location.latitude, location.longitude, 1)
                    val myLocation = addresses[0].featureName
                    //Replace Plus Code POI names with street addresses
                    binding.tvCurrentLocation.text = if(myLocation.contains("+")){
                        addresses[0].getAddressLine(0).replace("${myLocation},", "").trim()
                    }else {
                        myLocation
                    }
                }else{
                    //If no last location found in cache, attempt to retrieve the Current Location
                    val tokenSrc = CancellationTokenSource()
                    fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY,
                        tokenSrc.token).addOnSuccessListener { currLocation : Location? ->
                        if(currLocation != null){
                            val addresses = Geocoder(requireContext()).getFromLocation(currLocation.latitude, currLocation.longitude, 1)
                            val myLocation = addresses[0].featureName
                            //Replace Plus Code POI names with street addresses
                            binding.tvCurrentLocation.text = if(myLocation.contains("+")){
                                addresses[0].getAddressLine(0).replace("${myLocation},", "").trim()
                            }else {
                                myLocation
                            }
                        }else{
                            Toast.makeText(context, "Cannot retrieve your current location.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        } else{
            requestPermissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION))
        }
    }

    private fun checkPermission(permission : String) :Boolean{
        return ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED
    }

    private fun navigateToLocationFragment(){
        val toLocationFragment = HomeFragmentDirections.actionHomeFragmentToLocationFragment()
        requireView().findNavController().navigate(toLocationFragment)
    }
}