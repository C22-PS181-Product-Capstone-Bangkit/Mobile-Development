package com.bangkit.cemil.home

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.cemil.R
import com.bangkit.cemil.SettingPreferences
import com.bangkit.cemil.dataStore
import com.bangkit.cemil.databinding.FragmentLocationBinding
import com.bangkit.cemil.tools.LocationSearchAdapter
import com.bangkit.cemil.tools.LocationSearchItem
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.libraries.places.api.Places
import kotlinx.coroutines.launch

class LocationFragment : Fragment() {

    private lateinit var binding : FragmentLocationBinding
    private lateinit var viewModel: LocationViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var pref : SettingPreferences
    private lateinit var appContext : Context
    private val list = ArrayList<LocationSearchItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLocationBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).apply{
            setSupportActionBar(binding.materialToolbarLocation)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            binding.toolbarNavUp.setOnClickListener {
                onBackPressed()
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appContext = view.context
        pref = SettingPreferences.getInstance(requireContext().dataStore)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        viewModel = ViewModelProvider(this)[LocationViewModel::class.java]
        binding.rvSearchLocation.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
        }

        viewModel.listLocations.observe(requireActivity()){
            list.clear()
            list.addAll(it)
            showRecyclerList()
        }

        binding.btnCurrentLocation.setOnClickListener {
            getMyCurrentLocation()
        }

        binding.etSearchLocation.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                Places.initialize(requireContext(), getString(R.string.google_maps_api_key))
                val placesClient = Places.createClient(requireContext())
                viewModel.searchQueryLocation(query, placesClient)
                binding.etSearchLocation.clearFocus()
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

        binding.btnChooseMaps.setOnClickListener {
            Toast.makeText(context, "Testing choose maps", Toast.LENGTH_SHORT).show()
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){ permissions ->
        when{
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> getMyCurrentLocation()
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> getMyCurrentLocation()
            else -> Toast.makeText(appContext, "Location permisson needed. Try again.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getMyCurrentLocation(){
        if(checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION) && checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            val tokenSrc = CancellationTokenSource()
            fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY,
                tokenSrc.token).addOnSuccessListener { location : Location? ->
                if(location != null){
                    val addresses = Geocoder(appContext).getFromLocation(location.latitude, location.longitude, 1)
                    val currentLocation = if(addresses[0].featureName.contains("+")){
                        addresses[0].getAddressLine(0).replace("${addresses[0].featureName},", "").trim()
                    }else {
                        addresses[0].featureName
                    }
                    lifecycleScope.launch {
                        pref.deleteLocation()
                        activity?.onBackPressed()
                    }
                    Toast.makeText(appContext, currentLocation, Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(appContext, "Cannot retrieve your current location.", Toast.LENGTH_SHORT).show()
                }
            }
        } else{
            requestPermissionLauncher.launch(arrayOf( Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION))
        }
    }

    private fun checkPermission(permission : String) :Boolean{
        return ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED
    }

    private fun showRecyclerList(){

        val locationAdapter = LocationSearchAdapter(list)
        binding.rvSearchLocation.adapter = locationAdapter
        locationAdapter.setOnItemClickCallback(object : LocationSearchAdapter.OnItemClickCallback{
            override fun onItemClicked(data: LocationSearchItem) {
                Toast.makeText(appContext, data.locationDesc.toString(), Toast.LENGTH_SHORT).show()
                val addresses = Geocoder(requireContext()).getFromLocationName(data.locationDesc, 1)
                lifecycleScope.launch {
                    pref.saveLocation("${data.locationName}, ${data.locationDesc}")
                    pref.saveLatitudeLongitude(addresses[0].latitude.toString(), addresses[0].longitude.toString())
                    requireActivity().onBackPressed()
                }
            }
        })
    }
}