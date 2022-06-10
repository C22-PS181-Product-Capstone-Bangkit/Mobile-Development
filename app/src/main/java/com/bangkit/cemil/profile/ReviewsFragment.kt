package com.bangkit.cemil.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.cemil.SettingPreferences
import com.bangkit.cemil.dataStore
import com.bangkit.cemil.databinding.FragmentReviewsBinding
import com.bangkit.cemil.tools.UserReviewAdapter
import com.bangkit.cemil.tools.model.ReviewItem
import kotlinx.coroutines.launch

class ReviewsFragment : Fragment() {

    private lateinit var binding : FragmentReviewsBinding
    private val viewModel by viewModels<ReviewsViewModel>()
    private var accessToken : String? = null
    private var list = ArrayList<ReviewItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReviewsBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).apply{
            setSupportActionBar(binding.materialToolbarReviews)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            binding.toolbarNavUp.setOnClickListener {
                onBackPressed()
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pref = SettingPreferences.getInstance(requireContext().dataStore)
        lifecycleScope.launch{
            accessToken = pref.getPreferences()[SettingPreferences.AUTHORIZATION_TOKEN_KEY]
        }
        binding.rvUserReviews.layoutManager = LinearLayoutManager(requireContext())
        viewModel.fetchUserReviews(accessToken.toString())
        viewModel.userReviews.observe(viewLifecycleOwner){
            if(it != null){
                list.clear()
                list.addAll(it)
                list.reverse()
                showRecyclerList()
            }
        }
        binding.refreshReviewsRecyclerView.setOnRefreshListener {
            viewModel.fetchUserReviews(accessToken!!)
            binding.refreshReviewsRecyclerView.isRefreshing = false
        }
    }

    private fun showRecyclerList() {
        val adapter = UserReviewAdapter(list)
        binding.rvUserReviews.adapter = adapter
        adapter.setOnItemClickCallback(object: UserReviewAdapter.OnItemClickCallback{
            override fun onItemClicked(data: ReviewItem) {
                val toRestaurantFragment = ReviewsFragmentDirections.actionReviewsFragmentToRestaurantFragment(data.restaurant?.id!!)
                requireView().findNavController().navigate(toRestaurantFragment)
            }
        })
    }

}