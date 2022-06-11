package com.bangkit.cemil.home

import android.app.SearchManager
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.cemil.R
import com.bangkit.cemil.SettingPreferences
import com.bangkit.cemil.dataStore
import com.bangkit.cemil.databinding.FragmentSearchBinding
import com.bangkit.cemil.tools.CategoryAdapter
import com.bangkit.cemil.tools.CategoryItem
import com.bangkit.cemil.tools.JsonUtils.fromJson
import com.bangkit.cemil.tools.RecentSearchAdapter
import com.bangkit.cemil.tools.SearchAdapter
import com.bangkit.cemil.tools.model.RestaurantItem
import com.google.android.gms.maps.model.LatLng
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchFragment : Fragment() {

    private lateinit var binding : FragmentSearchBinding
    private val viewModel by viewModels<SearchViewModel>()
    private lateinit var mutableList : MutableList<RestaurantItem>
    private var latLng : LatLng? = null
    private var recentSearchList = ArrayList<String>()
    private val gson = GsonBuilder().create()
    private lateinit var pref : SettingPreferences
    private var isReversed = false

    private val listCategories: ArrayList<CategoryItem>
        get() {
            val categoryList = ArrayList<CategoryItem>()
            lifecycleScope.launch(Dispatchers.IO){
                val settingTitles = resources.getStringArray(R.array.category_titles)
                val settingPhotoIds = resources.obtainTypedArray(R.array.category_photos)
                for (index in settingTitles.indices) {
                    val categoryItem = CategoryItem(settingTitles[index], settingPhotoIds.getResourceId(index, -1))
                    categoryList.add(categoryItem)
                }
                settingPhotoIds.recycle()
            }
            return categoryList
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).apply{
            setSupportActionBar(binding.materialToolbar)
            supportActionBar?.setDisplayShowTitleEnabled(false)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pref = SettingPreferences.getInstance(requireContext().dataStore)
        binding.rvRecentSearches.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSearch.layoutManager = LinearLayoutManager(requireContext())
        lifecycleScope.launch {
            latLng = LatLng(pref.getPreferences()[SettingPreferences.LATITUDE_KEY]?.toDouble() ?: 0.0,
                pref.getPreferences()[SettingPreferences.LONGITUDE_KEY]?.toDouble() ?: 0.0)
            val recentSearchesJson = pref.getPreferences()[SettingPreferences.RECENT_SEARCH_KEY] ?: recentSearchList.toString()
            recentSearchList = recentSearchesJson.fromJson(gson)!!
        }
        binding.tvClearAll.setOnClickListener {
            Toast.makeText(context, "Clear all Recent Searches", Toast.LENGTH_SHORT).show()
            recentSearchList.clear()
            lifecycleScope.launch{
                pref.saveRecentSearch(gson.toJson(recentSearchList))
            }
            showRecentSearchRecyclerList()
        }
        viewModel.isLoading.observe(viewLifecycleOwner){
            showLoading(it)
        }
        setUpCategoriesList()
        showRecentSearchRecyclerList()
        val searchManager = requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        binding.etSearch.apply {
            setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
            queryHint = getString(R.string.foods_restaurants_users_reviews_etc)
            setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    viewModel.searchRestos(query.toString())
                    reverseBackRecentList()
                    recentSearchList.add(query.toString())
                    val searchJson = gson.toJson(recentSearchList)
                    lifecycleScope.launch{
                        pref.saveRecentSearch(searchJson)
                    }
                    binding.etSearch.clearFocus()
                    binding.searchDefaultLayout.visibility = View.GONE
                    binding.searchResultLayout.visibility = View.VISIBLE
                    return true
                }
                override fun onQueryTextChange(query: String?): Boolean {
                    if(query.equals("")){
                        binding.searchResultLayout.visibility = View.GONE
                        binding.searchDefaultLayout.visibility = View.VISIBLE
                        binding.btnSearchSortRating.visibility = View.INVISIBLE
                        binding.btnSearchSortLocation.visibility = View.INVISIBLE
                        binding.rvSearch.visibility = View.INVISIBLE
                        showRecentSearchRecyclerList()
                    }
                    return true
                }
            })
        }
        viewModel.listRestos.observe(viewLifecycleOwner){
            if(it != null){
                mutableList = it.toMutableList()
                val results = FloatArray(1)
                lifecycleScope.launch(Dispatchers.IO){
                    calculateRestaurantDistances(mutableList.listIterator(), results)
                    withContext(Dispatchers.Main){
                        binding.btnSearchSortRating.visibility = View.VISIBLE
                        binding.btnSearchSortLocation.visibility = View.VISIBLE
                        binding.rvSearch.visibility = View.VISIBLE
                        binding.btnSearchSortRating.isClickable = true
                        binding.btnSearchSortLocation.isClickable = true
                        showRecyclerList()
                    }
                }
                if(it.isEmpty()) Toast.makeText(context, "Not found.", Toast.LENGTH_SHORT).show()
            }
        }
        setButtonListeners()
    }

    private fun calculateRestaurantDistances(iterator: MutableListIterator<RestaurantItem>, results: FloatArray) {
        while (iterator.hasNext()) {
            try{
                val oldValue = iterator.next()
                val addresses = Geocoder(requireActivity().applicationContext).getFromLocationName(oldValue.location.toString(), 1)
                if (addresses.size > 0) {
                    val latitude = addresses[0].latitude
                    val longitude = addresses[0].longitude
                    if(latLng != null){
                        Location.distanceBetween(latLng!!.latitude, latLng!!.longitude, latitude, longitude, results)
                        oldValue.distance = (Math.round((results[0] / 1000) * 10.0) / 10.0).toString()
                    }else{
                        Toast.makeText(context, "Can't calculate your distance.", Toast.LENGTH_SHORT).show()
                    }
                }
                iterator.set(oldValue)
            }catch(e: Exception){
                binding.searchDefaultLayout.visibility = View.VISIBLE
                binding.searchResultLayout.visibility = View.GONE
            }
        }
    }

    private fun setUpCategoriesList(){
        val categoriesAdapter = CategoryAdapter(listCategories)
        binding.rvCategories.apply{
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, 3)
            adapter = categoriesAdapter
        }
        categoriesAdapter.setOnItemClickCallback(object: CategoryAdapter.OnItemClickCallback{
            override fun onItemClicked(data: CategoryItem) {
                viewModel.fetchRestosByCategory(data.title)
                binding.searchDefaultLayout.visibility = View.GONE
                binding.searchResultLayout.visibility = View.VISIBLE
            }
        })
    }

    private fun showRecyclerList() {
        val searchAdapter = SearchAdapter(mutableList)
        binding.rvSearch.adapter = searchAdapter
        searchAdapter.setOnItemClickCallback(object: SearchAdapter.OnItemClickCallback{
            override fun onItemClicked(data: RestaurantItem) {
                val toRestaurantFragment = SearchFragmentDirections.actionSearchFragmentToRestaurantFragment(data.id.toString())
                requireView().findNavController().navigate(toRestaurantFragment)
            }
        })
    }

    private fun showRecentSearchRecyclerList(){
        if(!isReversed){
            recentSearchList.reverse()
            isReversed = true
        }
        val recentSearchAdapter = RecentSearchAdapter(recentSearchList)
        binding.rvRecentSearches.adapter = recentSearchAdapter
        recentSearchAdapter.setOnItemClickCallback(object: RecentSearchAdapter.OnItemClickCallback{
            override fun onItemClicked(data: String) {
                binding.etSearch.setQuery(data, true)
            }
            override fun onEraseClicked(data: String, position: Int) {
                recentSearchList.removeAt(position)
                reverseBackRecentList()
                lifecycleScope.launch{
                        pref.saveRecentSearch(gson.toJson(recentSearchList))
                }
                showRecentSearchRecyclerList()
            }
        })
    }

    private fun reverseBackRecentList(){
        if(isReversed){
            recentSearchList.reverse()
            isReversed = false
        }
    }

    private fun setButtonListeners(){
        binding.btnSearchSortRating.setOnClickListener {
            binding.btnSearchSortRating.isClickable = false
            binding.btnSearchSortLocation.isClickable = true
            lifecycleScope.launch(Dispatchers.IO){
                mutableList.sortBy { it.rating }
                mutableList.reverse()
                withContext(Dispatchers.Main){
                    showRecyclerList()
                }
            }
        }
        binding.btnSearchSortLocation.setOnClickListener {
            binding.btnSearchSortLocation.isClickable = false
            binding.btnSearchSortRating.isClickable = true
            lifecycleScope.launch(Dispatchers.IO){
                mutableList.sortBy { it.distance?.toDouble() }
                withContext(Dispatchers.Main){
                    showRecyclerList()
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean){
        binding.searchProgressBar.visibility = if(isLoading) View.VISIBLE else View.GONE
    }
}