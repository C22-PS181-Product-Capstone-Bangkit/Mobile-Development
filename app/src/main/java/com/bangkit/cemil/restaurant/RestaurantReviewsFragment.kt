package com.bangkit.cemil.restaurant

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.cemil.R
import com.bangkit.cemil.databinding.FragmentRestaurantReviewsBinding
import com.bangkit.cemil.tools.ReviewAdapter
import com.bangkit.cemil.tools.model.RestaurantReviewItem
import com.bangkit.cemil.tools.model.ReviewItem


class RestaurantReviewsFragment : Fragment() {

    private lateinit var binding : FragmentRestaurantReviewsBinding
    private val viewModel by viewModels<RestaurantReviewsViewModel>()
    private val list = ArrayList<RestaurantReviewItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRestaurantReviewsBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).apply{
            setSupportActionBar(binding.materialToolbarRestoReviews)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            binding.toolbarNavUp.setOnClickListener {
                onBackPressed()
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dataRestaurantId = RestaurantReviewsFragmentArgs.fromBundle(arguments as Bundle).restaurantId
        binding.rvRestoReviews.layoutManager = LinearLayoutManager(context)
        viewModel.getRestaurantReviews(dataRestaurantId)
        viewModel.restoReviewsData.observe(viewLifecycleOwner){
            list.clear()
            list.addAll(it!!)
            list.reverse()
            showRecyclerList()
        }
    }

    private fun showRecyclerList(){
        val adapter = ReviewAdapter(list)
        binding.rvRestoReviews.adapter = adapter
        adapter.setOnItemClickCallback(object : ReviewAdapter.OnItemClickCallback{
            override fun onItemClicked(data: RestaurantReviewItem) {
                Toast.makeText(context, data.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

}