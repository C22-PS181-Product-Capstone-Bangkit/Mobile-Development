package com.bangkit.cemil.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.bangkit.cemil.LandingFragment
import com.bangkit.cemil.R
import com.bangkit.cemil.databinding.FragmentRestaurantBinding
import com.bumptech.glide.Glide

class RestaurantFragment : Fragment() {

    private lateinit var binding : FragmentRestaurantBinding
    private val viewModel by viewModels<RestaurantViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        binding = FragmentRestaurantBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dataRestaurantId = RestaurantFragmentArgs.fromBundle(arguments as Bundle).restaurantId
        viewModel.getRestaurantById(dataRestaurantId)
        viewModel.restoData.observe(viewLifecycleOwner){
            binding.nestedScrollView.visibility = View.VISIBLE
            binding.tvRestaurantName.text = it.name
            val categoryAmount = it.category?.split(", ")
            val restoReviewAmount = "(${it.countReview})"
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

            binding.tvRestaurantTime.text = it.openHour
            binding.tvRestaurantLocation.text = it.location
            binding.tvRestaurantCost.text = it.price
            Glide.with(requireContext()).load(it.photoPlaces).into(binding.imgRestaurantBanner)
            binding.tvRestaurantReviewsAmount.text = restoReviewAmount
        }
    }

}