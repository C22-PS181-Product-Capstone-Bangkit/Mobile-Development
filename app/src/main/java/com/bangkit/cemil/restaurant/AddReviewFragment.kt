package com.bangkit.cemil.restaurant

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.bangkit.cemil.SettingPreferences
import com.bangkit.cemil.dataStore
import com.bangkit.cemil.databinding.FragmentAddReviewBinding
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch

class AddReviewFragment : Fragment() {

    private lateinit var binding : FragmentAddReviewBinding
    private val viewModel by viewModels<AddReviewViewModel>()
    private var accessToken : String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddReviewBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).apply{
            setSupportActionBar(binding.materialToolbarAddReview)
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
        val dataRestaurantId = AddReviewFragmentArgs.fromBundle(arguments as Bundle).restaurantId
        lifecycleScope.launch {
            accessToken = pref.getPreferences()[SettingPreferences.AUTHORIZATION_TOKEN_KEY]
        }
        viewModel.getRestaurantData(dataRestaurantId)
        viewModel.restoData.observe(viewLifecycleOwner){
            binding.tvAddReviewRestoName.text = it.name
            Glide.with(requireContext()).load(it.profilePic).into(binding.imgAddReviewResto)
            binding.ratingBar.rating = 5F
        }
        viewModel.addReviewResponse.observe(viewLifecycleOwner){
            if(it.message != null && it.message == "You already reviewed this restaurant."){
                Toast.makeText(context, "You already reviewed this restaurant.", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context, "Review added!", Toast.LENGTH_SHORT).show()
                val toRestaurantFragment = AddReviewFragmentDirections.actionAddReviewFragmentToRestaurantFragment(dataRestaurantId)
                requireView().findNavController().navigate(toRestaurantFragment)
            }
        }

        binding.btnSendReview.setOnClickListener {
            val reviewDescription = binding.etAddReview.text.toString().trim()
            val userRating = binding.ratingBar.rating
            if(reviewDescription.isBlank()){
                binding.etAddReview.error = "Review should not be empty!"
            }else{
                viewModel.postReview(accessToken.toString(), dataRestaurantId, userRating.toDouble(), reviewDescription)
            }
        }

        binding.ratingBar.setOnRatingBarChangeListener { ratingBar, fl, _ ->
            if(fl < 1.0f){
                ratingBar.rating = 1.0f
            }else if(ratingBar.rating == 1.0f){
                ratingBar.rating = 1.0f
            }
        }
    }
}