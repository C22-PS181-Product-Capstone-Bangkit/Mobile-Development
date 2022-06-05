package com.bangkit.cemil.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.bangkit.cemil.databinding.FragmentRestaurantBinding
import com.bangkit.cemil.tools.HistoryAdapter
import com.bangkit.cemil.tools.model.HistoryItem
import com.bangkit.cemil.tools.model.ReviewItem
import com.bumptech.glide.Glide

class RestaurantFragment : Fragment() {

    private lateinit var binding : FragmentRestaurantBinding
    private val viewModel by viewModels<RestaurantViewModel>()
    private val list = ArrayList<ReviewItem>()

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
            val restoReviewAmount = "(${it.countReview})"
            val categoryAmount = it.category?.split(", ")
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
            binding.tvRestaurantName.text = it.name
            binding.tvRestaurantTime.text = it.openHour
            binding.tvRestaurantLocation.text = it.location
            binding.tvRestaurantCost.text = it.price
            binding.tvRestaurantReviewsAmount.text = restoReviewAmount
            Glide.with(requireContext()).load(it.photoPlaces).into(binding.imgRestaurantBanner)
        }
    }

    private fun showRecyclerList(){
//        val adapter = HistoryAdapter(list)
//        binding.rvReviews.adapter = adapter
//        adapter.setOnItemClickCallback(object : HistoryAdapter.OnItemClickCallback{
//            override fun onItemClicked(data: HistoryItem) {
//                Toast.makeText(context, data.toString(), Toast.LENGTH_SHORT).show()
//            }
//        })
    }
}