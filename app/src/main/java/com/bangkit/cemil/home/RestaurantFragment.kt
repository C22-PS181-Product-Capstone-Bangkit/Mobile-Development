package com.bangkit.cemil.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.cemil.SettingPreferences
import com.bangkit.cemil.dataStore
import com.bangkit.cemil.databinding.FragmentRestaurantBinding
import com.bangkit.cemil.tools.ReviewAdapter
import com.bangkit.cemil.tools.model.RestaurantItem
import com.bangkit.cemil.tools.model.RestaurantReviewItem
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class RestaurantFragment : Fragment() {

    private lateinit var binding : FragmentRestaurantBinding
    private val viewModel by viewModels<RestaurantViewModel>()
    private var accessToken : String? = null
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
        val pref = SettingPreferences.getInstance(requireContext().dataStore)
        lifecycleScope.launch {
            accessToken = pref.getPreferences()[SettingPreferences.AUTHORIZATION_TOKEN_KEY]
        }
        binding.rvReviews.layoutManager = LinearLayoutManager(context)
        viewModel.getRestaurantById(dataRestaurantId)
        viewModel.restoData.observe(viewLifecycleOwner){
            binding.nestedScrollView.visibility = View.VISIBLE
            setupRestaurantPageContent(it)
            list.clear()
            list.addAll(it.review!!)
            showRecyclerList()
        }
        viewModel.restoLike.observe(viewLifecycleOwner){
            Toast.makeText(context, "Restaurant liked!", Toast.LENGTH_SHORT).show()
        }
        binding.tvAddReview.setOnClickListener {
            Toast.makeText(context, "To Add Review Page", Toast.LENGTH_SHORT).show()
        }
        binding.fabBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.fabLike.setOnClickListener {
            viewModel.postRestaurantLike(accessToken.toString(), dataRestaurantId)
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
}