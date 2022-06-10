package com.bangkit.cemil.restaurant

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.cemil.R
import com.bangkit.cemil.SettingPreferences
import com.bangkit.cemil.dataStore
import com.bangkit.cemil.databinding.FragmentRestaurantBinding
import com.bangkit.cemil.tools.JsonUtils.fromJson
import com.bangkit.cemil.tools.ReviewAdapter
import com.bangkit.cemil.tools.model.LikesItem
import com.bangkit.cemil.tools.model.RestaurantItem
import com.bangkit.cemil.tools.model.RestaurantReviewItem
import com.bumptech.glide.Glide
import com.google.gson.GsonBuilder
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class RestaurantFragment : Fragment() {

    private lateinit var binding : FragmentRestaurantBinding
    private lateinit var dataRestaurantId : String
    private val viewModel by viewModels<RestaurantViewModel>()
    private var accessToken : String? = null
    private var likeItem: LikesItem? = null
    private val list = ArrayList<RestaurantReviewItem>()
    private var recentlyVisitedIds = ArrayList<String>()
    private val gson = GsonBuilder().create()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentRestaurantBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataRestaurantId = RestaurantFragmentArgs.fromBundle(arguments as Bundle).restaurantId
        val pref = SettingPreferences.getInstance(requireContext().dataStore)
        lifecycleScope.launch {
            accessToken = pref.getPreferences()[SettingPreferences.AUTHORIZATION_TOKEN_KEY]
            val recentlyVisitedJson = pref.getPreferences()[SettingPreferences.RECENTLY_VISITED_KEY] ?: recentlyVisitedIds.toString()
            recentlyVisitedIds = recentlyVisitedJson.fromJson(gson)!!
            if(recentlyVisitedIds.any{it == dataRestaurantId}){
                recentlyVisitedIds.remove(dataRestaurantId)
            }
            if(recentlyVisitedIds.size >= 4){
                recentlyVisitedIds.removeAt(0)
            }
            recentlyVisitedIds.add(dataRestaurantId)
            pref.saveRecentlyVisited(gson.toJson(recentlyVisitedIds))
        }
        if(accessToken == null) binding.fabLike.visibility = View.INVISIBLE else viewModel.fetchProfile(accessToken!!)

        viewModel.profileData.observe(viewLifecycleOwner){ profileData ->
            likeItem = profileData.likes?.find{it.restaurant?.id == dataRestaurantId}
            if(likeItem != null) toggleLikeButtonOn() else toggleLikeButtonOff()
            val alreadyReviewed = profileData.review?.any { it.restaurant?.id == dataRestaurantId }
            if(alreadyReviewed == true) binding.tvAddReview.visibility = View.GONE
        }
        viewModel.restoLike.observe(viewLifecycleOwner){
            Toast.makeText(context, "Restaurant liked!", Toast.LENGTH_SHORT).show()
            toggleLikeButtonOn()
        }
        viewModel.restoDeleteLike.observe(viewLifecycleOwner){
            Toast.makeText(context, "Restaurant like removed!", Toast.LENGTH_SHORT).show()
            toggleLikeButtonOff()
        }

        binding.rvReviews.layoutManager = LinearLayoutManager(context)
        viewModel.getRestaurantById(dataRestaurantId)
        viewModel.restoData.observe(viewLifecycleOwner){
            binding.nestedScrollView.visibility = View.VISIBLE
            setupRestaurantPageContent(it)
            list.clear()
            list.addAll(it.review!!)
            list.reverse()
            showRecyclerList()
        }

        setButtonListener()
    }

    private fun showRecyclerList(){
        val adapter = ReviewAdapter(list.take(2))
        binding.rvReviews.adapter = adapter
        adapter.setOnItemClickCallback(object : ReviewAdapter.OnItemClickCallback{
            override fun onItemClicked(data: RestaurantReviewItem) {
                Toast.makeText(context, data.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupRestaurantPageContent(restoDetail: RestaurantItem){
        val restoReviewAmount = "(${restoDetail.countReview})"
        val restoAverageCost = "${currencyFormat((restoDetail.price!!.toInt() / 2).toString())} / person"
        val categoryAmount = restoDetail.category?.split(", ")
        if(categoryAmount?.size!! >= 1){
            binding.tvRestaurantCategory.text = categoryAmount[0]
        }
        if(categoryAmount.size >= 2){
            binding.tvRestaurantCategory2.apply {
                visibility = View.VISIBLE
                text = categoryAmount[1]
            }
        }
        if(categoryAmount.size >= 3){
            binding.tvRestaurantCategory3.apply {
                visibility = View.VISIBLE
                text = categoryAmount[2]
            }
        }
        binding.tvRestaurantName.text = restoDetail.name
        binding.tvRestaurantTime.text = restoDetail.openHour
        binding.tvRestaurantLocation.text = restoDetail.location
        binding.tvRestaurantCost.text = restoAverageCost
        binding.tvRestaurantReviewsAmount.text = restoReviewAmount
        Glide.with(requireContext()).load(restoDetail.photoPlaces).into(binding.imgRestaurantBanner)
    }

    private fun currencyFormat(amount: String) : String{
        val rupiahFormatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
        return rupiahFormatter.format(amount.toDouble())
    }

    private fun setButtonListener(){
        binding.tvAddReview.setOnClickListener {
            val toAddReviewFragment = RestaurantFragmentDirections.actionRestaurantFragmentToAddReviewFragment(dataRestaurantId)
            requireView().findNavController().navigate(toAddReviewFragment)
        }
        binding.btnSeeAllReviews.setOnClickListener {
            val toRestaurantReviewsFragment = RestaurantFragmentDirections.actionRestaurantFragmentToRestaurantReviewsFragment(dataRestaurantId)
            requireView().findNavController().navigate(toRestaurantReviewsFragment)
        }
        binding.fabBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun toggleLikeButtonOff(){
        binding.fabLike.imageTintList = ColorStateList.valueOf(com.google.android.material.R.attr.strokeColor)
        binding.fabLike.setOnClickListener {
            viewModel.postRestaurantLike(accessToken.toString(), dataRestaurantId)
        }
    }

    private fun toggleLikeButtonOn(){
        binding.fabLike.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.secondary))
        binding.fabLike.setOnClickListener {
            viewModel.deleteRestaurantLike(accessToken.toString(), likeItem?.id.toString())
        }
    }
}