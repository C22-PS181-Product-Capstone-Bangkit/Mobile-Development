package com.bangkit.cemil.home

import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.cemil.SettingPreferences
import com.bangkit.cemil.dataStore
import com.bangkit.cemil.databinding.FragmentRecommendRestaurantBinding
import com.bangkit.cemil.tools.RecommendAdapter
import com.bangkit.cemil.tools.model.ProfileResponse
import com.bangkit.cemil.tools.model.RestaurantItem
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch

class RecommendRestaurantFragment : Fragment() {

    private lateinit var binding: FragmentRecommendRestaurantBinding
    private val viewModel by viewModels<RecommendRestaurantViewModel>()
    private var accessToken: String? = ""
    private val list = ArrayList<RestaurantItem>()
    private lateinit var latLng: LatLng
    private lateinit var userData: ProfileResponse

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecommendRestaurantBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pref = SettingPreferences.getInstance(requireContext().dataStore)
        lifecycleScope.launch {
            accessToken =
                pref.getPreferences()[SettingPreferences.AUTHORIZATION_TOKEN_KEY].toString()
        }

        val categories = RecommendRestaurantFragmentArgs.fromBundle(arguments as Bundle).categories
        val prices = RecommendRestaurantFragmentArgs.fromBundle(arguments as Bundle).prices
        val distance = RecommendRestaurantFragmentArgs.fromBundle(arguments as Bundle).distance
        val ratings = RecommendRestaurantFragmentArgs.fromBundle(arguments as Bundle).ratings

        viewModel.fetchProfile(accessToken.toString())
        binding.rvRecommendations.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        viewModel.profileData.observe(viewLifecycleOwner) {
            if (it != null) {
                userData = it
                viewModel.fetchRecommendations(accessToken.toString())
            } else {
                Toast.makeText(context, "Fetching user profile failed!", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.recommendationList.observe(viewLifecycleOwner) {
            list.clear()
            val filteredList = filterList(it, pref, categories, prices, distance, ratings)
            list.addAll(filteredList)
            showRecyclerList()
        }
        viewModel.historyResponse.observe(viewLifecycleOwner) {
            if (it != null) {
                val toRestaurantFragment =
                    RecommendRestaurantFragmentDirections.actionRecommendRestaurantFragmentToRestaurantFragment(
                        it.idRestaurant.toString()
                    )
                requireView().findNavController().navigate(toRestaurantFragment)
            }
        }
        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
    }

    private fun filterList(
        restaurantList: List<RestaurantItem>,
        preferences: SettingPreferences,
        categories: String,
        prices: String,
        distance: String,
        ratings: String
    ) : List<RestaurantItem> {
        val categoryFilteredList = filterCategory(restaurantList, categories)
        val priceFilteredList = filterPrice(categoryFilteredList, prices)
        val distanceFilteredList = filterDistance(priceFilteredList, preferences, distance)
        val ratingFilteredList = filterRating(distanceFilteredList, ratings)
        return ratingFilteredList
    }

    private fun filterRating(distanceFilteredList: List<RestaurantItem>, ratings: String): List<RestaurantItem> {
        val tempList = mutableListOf<RestaurantItem>()
        if (ratings == "4.0+") {
            for (restaurant in distanceFilteredList) {
                if (restaurant.rating.toDouble() >= 4.0) {
                    tempList.add(restaurant)
                }
            }
        } else {
            return distanceFilteredList
        }
        return tempList
    }

    private fun filterDistance(priceFilteredList: List<RestaurantItem>, preferences: SettingPreferences, distance: String): List<RestaurantItem> {
        val tempList = mutableListOf<RestaurantItem>()
        if (distance.isNotEmpty()) {
            lifecycleScope.launch {
                latLng = LatLng(
                    preferences.getPreferences()[SettingPreferences.LATITUDE_KEY]?.toDouble() ?: 0.0,
                    preferences.getPreferences()[SettingPreferences.LONGITUDE_KEY]?.toDouble() ?: 0.0
                )
            }
            calculateRestaurantDistances(priceFilteredList.toMutableList().listIterator(), FloatArray(1))
            for (restaurant in priceFilteredList) {
                if (distance[0] == '0') {
                    if (restaurant.distance.toDouble() <= 5.0) {
                        tempList.add(restaurant)
                    }
                    continue
                }
                if (distance[0] == '5') {
                    if (restaurant.distance.toDouble() > 5.0 && restaurant.distance.toDouble() <= 10.0) {
                        tempList.add(restaurant)
                    }
                    continue
                }
                if (distance[0] == '1') {
                    if (restaurant.distance.toDouble() > 10.0) {
                        tempList.add(restaurant)
                    }
                    continue
                }
            }
        } else {
            return priceFilteredList
        }
        return tempList
    }

    private fun filterPrice(categoryFilteredList: List<RestaurantItem>, prices: String): List<RestaurantItem> {
        val tempList = mutableListOf<RestaurantItem>()
        if (prices.isNotEmpty()) {
            for (restaurant in categoryFilteredList) {
                val averagePrice = restaurant.price.toDouble() / restaurant.person.toDouble()
                if (prices.contains("10.000")) {
                    if (averagePrice <= 10000) {
                        tempList.add(restaurant)
                    }
                } else if (prices.contains("25.000")) {
                    if (averagePrice <= 25000) {
                        tempList.add(restaurant)
                    }
                } else if (prices.contains("50.000")) {
                    if (averagePrice <= 50000) {
                        tempList.add(restaurant)
                    }
                } else if (prices.contains("100.000")) {
                    if (averagePrice <= 100000) {
                        tempList.add(restaurant)
                    }
                }
            }
        } else {
            return categoryFilteredList
        }
        return tempList
    }

    private fun filterCategory(restaurantList: List<RestaurantItem>, categories: String) : List<RestaurantItem>{
        val tempList = mutableListOf<RestaurantItem>()
        if (categories.isNotEmpty()) {
            if (categories.contains(", ")) {
                val categoryList = categories.split(", ").toTypedArray()
                for (restaurant in restaurantList) {
                    for (category in categoryList) {
                        if (restaurant.category?.contains(category) == true) {
                            tempList.add(restaurant)
                        }
                    }
                }
            } else {
                for (restaurant in restaurantList) {
                    if (restaurant.category?.contains(categories) == true) {
                        tempList.add(restaurant)
                    }
                }
            }
        } else {
            return restaurantList
        }
        return tempList
    }

    private fun showRecyclerList() {
        val adapter = RecommendAdapter(list, userData)
        binding.rvRecommendations.adapter = adapter
        binding.rvRecommendations.setOnTouchListener { _, _ -> true }
        adapter.setOnItemClickCallback(object : RecommendAdapter.OnItemClickCallback {
            override fun onDeclineClicked(data: RestaurantItem, position: Int) {
                binding.rvRecommendations.smoothScrollToPosition(position + 1)
            }

            override fun onRecommendClicked(data: RestaurantItem, position: Int) {
                viewModel.postRestaurantLike(accessToken.toString(), data.id.toString())
                Toast.makeText(context, "Restaurant saved to likes!", Toast.LENGTH_SHORT).show()
            }

            override fun onRecommendDeleteClicked(data: RestaurantItem, position: Int) {
                viewModel.deleteRestaurantLike(accessToken.toString(), data.id.toString())
                Toast.makeText(context, "Restaurant deleted from likes!", Toast.LENGTH_SHORT).show()
            }

            override fun onAcceptClicked(data: RestaurantItem, position: Int) {
                viewModel.postHistory(accessToken.toString(), data.id.toString())
                Toast.makeText(context, "${data.name} accepted!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.recommendProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun calculateRestaurantDistances(
        iterator: MutableListIterator<RestaurantItem>,
        results: FloatArray
    ) {
        while (iterator.hasNext()) {
            val oldValue = iterator.next()
            try {
                val addresses = Geocoder(requireActivity().applicationContext).getFromLocationName(
                    oldValue.location.toString(),
                    1
                )
                if (addresses.size > 0) {
                    val latitude = addresses[0].latitude
                    val longitude = addresses[0].longitude
                    Location.distanceBetween(
                        latLng.latitude,
                        latLng.longitude,
                        latitude,
                        longitude,
                        results
                    )
                }
            } catch (e: Exception) {
                oldValue.distance = 0.0.toString()
            }
            oldValue.distance = (Math.round((results[0] / 1000) * 10.0) / 10.0).toString()
            iterator.set(oldValue)
        }
    }
}