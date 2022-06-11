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
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.cemil.SettingPreferences
import com.bangkit.cemil.dataStore
import com.bangkit.cemil.databinding.FragmentHomeBinding
import com.bangkit.cemil.tools.JsonUtils.fromJson
import com.bangkit.cemil.tools.RestaurantAdapter
import com.bangkit.cemil.tools.model.RestaurantItem
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var locationAddress: String = ""
    private val viewModel by viewModels<HomeViewModel>()
    private var latLng : LatLng? = null
    private val list = ArrayList<RestaurantItem>()
    private val recentlyVisitedList = ArrayList<RestaurantItem>()
    private var recentlyVisitedIds = ArrayList<String>()
    private val gson = GsonBuilder().create()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pref = SettingPreferences.getInstance(requireContext().dataStore)
        lifecycleScope.launch {
            locationAddress = pref.getPreferences()[SettingPreferences.LOCATION_KEY].toString()
            latLng = LatLng(pref.getPreferences()[SettingPreferences.LATITUDE_KEY]?.toDouble() ?: 0.0,
                pref.getPreferences()[SettingPreferences.LONGITUDE_KEY]?.toDouble() ?: 0.0)
            val recentlyVisitedJson = pref.getPreferences()[SettingPreferences.RECENTLY_VISITED_KEY] ?: recentlyVisitedIds.toString()
            recentlyVisitedIds = recentlyVisitedJson.fromJson(gson)!!
        }

        binding.rvNearbyRestos.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
        binding.rvRecentlyVisited.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(view.context)
        if (locationAddress != "null") {
            binding.tvCurrentLocation.text = locationAddress
        } else {
            getMyLocation()
        }

        setLocationTextClickListener()
        if(viewModel.restoData.value.isNullOrEmpty()){
            viewModel.requestRestoData()
        }
        viewModel.restoData.observe(viewLifecycleOwner){ it ->
            val results = FloatArray(1)
            val restaurants = it.toMutableList()
            lifecycleScope.launch(Dispatchers.IO){
                calculateRestaurantDistances(restaurants.listIterator(), results, pref)
                restaurants.sortBy { it.distance?.toDouble() }
                list.clear()
                list.addAll(restaurants)
                withContext(Dispatchers.Main){
                    showRecyclerList()
                }
            }
        }
        if(recentlyVisitedIds.isNotEmpty()){
            viewModel.requestRestoFromIds(recentlyVisitedIds)
        }
        viewModel.restoRecentlyVisitedData.observe(viewLifecycleOwner){
            val results = FloatArray(1)
            val restaurants = it.toMutableList()
            lifecycleScope.launch(Dispatchers.IO){
                calculateRestaurantDistances(restaurants.listIterator(), results, pref)
                recentlyVisitedList.clear()
                recentlyVisitedList.addAll(restaurants)
                recentlyVisitedList.reverse()
                withContext(Dispatchers.Main){
                    showRecentlyVisitedRecyclerList()
                }
            }
        }

        binding.tvCurrentLocationLabel.setOnClickListener {
            navigateToLocationFragment()
        }

        binding.btnFindMeFood.setOnClickListener {
            val toPreferencesFragment = HomeFragmentDirections.actionHomeFragmentToPreferencesFragment()
            requireView().findNavController().navigate(toPreferencesFragment)
        }
    }

    private fun calculateRestaurantDistances(iterator: MutableListIterator<RestaurantItem>, results: FloatArray, pref: SettingPreferences) {
        while (iterator.hasNext()) {
            val oldValue = iterator.next()
            try{
                val addresses = Geocoder(requireActivity().applicationContext).getFromLocationName(oldValue.location.toString(), 1)
                if (addresses.size > 0) {
                    val latitude = addresses[0].latitude
                    val longitude = addresses[0].longitude
                    if(latLng == null){
                        val userAddress = Geocoder(requireContext()).getFromLocationName(locationAddress, 1)
                        Location.distanceBetween(userAddress[0].latitude, userAddress[0].longitude, latitude, longitude, results)
                        lifecycleScope.launch{
                            pref.saveLatitudeLongitude(userAddress[0].latitude.toString(), userAddress[0].longitude.toString())
                        }
                    }else{
                        Location.distanceBetween(latLng!!.latitude, latLng!!.longitude, latitude, longitude, results)
                    }
                }
            }catch(e: Exception){
                val toHomeFragment = HomeFragmentDirections.actionHomeFragmentToHomeFragment()
                view?.findNavController()?.navigate(toHomeFragment)
            }
            oldValue.distance = (Math.round((results[0] / 1000) * 10.0) / 10.0).toString()
            iterator.set(oldValue)
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> getMyLocation()
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> getMyLocation()
                else -> Toast.makeText(
                    requireContext(),
                    "Location permission needed. Try again.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    private fun getMyLocation() {
        if (checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION) && checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val addresses = Geocoder(requireContext()).getFromLocation(
                        location.latitude,
                        location.longitude,
                        1
                    )
                    val myLocation = addresses[0].getAddressLine(0)
                    binding.tvCurrentLocation.text = myLocation

                } else {
                    //If no last location found in cache, attempt to retrieve the Current Location
                    val tokenSrc = CancellationTokenSource()
                    fusedLocationClient.getCurrentLocation(
                        LocationRequest.PRIORITY_HIGH_ACCURACY,
                        tokenSrc.token
                    ).addOnSuccessListener { currLocation: Location? ->
                        if (currLocation != null) {
                            val addresses = Geocoder(requireContext()).getFromLocation(
                                currLocation.latitude,
                                currLocation.longitude,
                                1
                            )
                            val myLocation = addresses[0].getAddressLine(0)
                            binding.tvCurrentLocation.text = myLocation
                        } else {
                            Toast.makeText(
                                context,
                                "Cannot retrieve your current location.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
        }
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun navigateToLocationFragment() {
        val toLocationFragment = HomeFragmentDirections.actionHomeFragmentToLocationFragment()
        requireView().findNavController().navigate(toLocationFragment)
    }

    private fun setLocationTextClickListener(){
        binding.tvCurrentLocation.setOnClickListener {
            val toLocationFragment = HomeFragmentDirections.actionHomeFragmentToLocationFragment()
            requireView().findNavController().navigate(toLocationFragment)
        }
        binding.tvCurrentLocationLabel.setOnClickListener {
            val toLocationFragment = HomeFragmentDirections.actionHomeFragmentToLocationFragment()
            requireView().findNavController().navigate(toLocationFragment)
        }
    }

    private fun showRecyclerList(){
        val adapter = RestaurantAdapter(list)
        binding.rvNearbyRestos.adapter = adapter
        adapter.setOnItemClickCallback(object : RestaurantAdapter.OnItemClickCallback{
            override fun onItemClicked(data: RestaurantItem) {
                val toRestaurantFragment = HomeFragmentDirections.actionHomeFragmentToRestaurantFragment(data.id.toString())
                requireView().findNavController().navigate(toRestaurantFragment)
            }
        })
    }

    private fun showRecentlyVisitedRecyclerList(){
        val adapter = RestaurantAdapter(recentlyVisitedList)
        binding.rvRecentlyVisited.adapter = adapter
        adapter.setOnItemClickCallback(object : RestaurantAdapter.OnItemClickCallback{
            override fun onItemClicked(data: RestaurantItem) {
                val toRestaurantFragment = HomeFragmentDirections.actionHomeFragmentToRestaurantFragment(data.id.toString())
                requireView().findNavController().navigate(toRestaurantFragment)
            }
        })
    }
}