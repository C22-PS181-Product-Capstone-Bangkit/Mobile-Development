package com.bangkit.cemil.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.cemil.databinding.FragmentRestaurantBinding
import com.bangkit.cemil.tools.ReviewAdapter
import com.bangkit.cemil.tools.model.RestaurantReviewItem
import com.bumptech.glide.Glide
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class RestaurantFragment : Fragment() {

    private lateinit var binding : FragmentRestaurantBinding
    private val viewModel by viewModels<RestaurantViewModel>()
    private val list = ArrayList<RestaurantReviewItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentRestaurantBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dataRestaurantId = RestaurantFragmentArgs.fromBundle(arguments as Bundle).restaurantId
        binding.rvReviews.layoutManager = LinearLayoutManager(context)
        viewModel.getRestaurantById(dataRestaurantId)
        viewModel.restoData.observe(viewLifecycleOwner){
            binding.nestedScrollView.visibility = View.VISIBLE
            val restoReviewAmount = "(${it.countReview})"
            val restoAverageCost = "${currencyFormat((it.price!!.toInt() / 2).toString())} / person"
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
            binding.tvRestaurantCost.text = restoAverageCost
            binding.tvRestaurantReviewsAmount.text = restoReviewAmount
            Glide.with(requireContext()).load(it.photoPlaces).into(binding.imgRestaurantBanner)
            list.clear()
            list.addAll(it.review!!)
            showRecyclerList()
        }
        binding.tvAddReview.setOnClickListener {
            Toast.makeText(context, "To Add Review Page", Toast.LENGTH_SHORT).show()
        }
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

    private fun currencyFormat(amount: String) : String{
        val rupiahFormatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
        return rupiahFormatter.format(amount.toDouble())
    }
}