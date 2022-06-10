package com.bangkit.cemil.home

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
import kotlinx.coroutines.launch

class RecommendRestaurantFragment : Fragment() {

    private lateinit var binding : FragmentRecommendRestaurantBinding
    private val viewModel by viewModels<RecommendRestaurantViewModel>()
    private var accessToken : String? = ""
    private val list = ArrayList<RestaurantItem>()
    private lateinit var userData : ProfileResponse

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
            accessToken = pref.getPreferences()[SettingPreferences.AUTHORIZATION_TOKEN_KEY].toString()
        }
        viewModel.fetchProfile(accessToken.toString())
        binding.rvRecommendations.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        viewModel.profileData.observe(viewLifecycleOwner){
            if(it != null){
                userData = it
                viewModel.fetchRecommendations(accessToken.toString())
            }else{
                Toast.makeText(context, "Fetching user profile failed!", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.recommendationList.observe(viewLifecycleOwner){
            list.clear()
            list.addAll(it)
            showRecyclerList()
        }
        viewModel.historyResponse.observe(viewLifecycleOwner){
            if(it != null){
                val toRestaurantFragment = RecommendRestaurantFragmentDirections.actionRecommendRestaurantFragmentToRestaurantFragment(it.idRestaurant.toString())
                requireView().findNavController().navigate(toRestaurantFragment)
            }
        }
        viewModel.isLoading.observe(viewLifecycleOwner){
            showLoading(it)
        }
    }

    private fun showRecyclerList(){
        val adapter = RecommendAdapter(list, userData)
        binding.rvRecommendations.adapter = adapter
        binding.rvRecommendations.setOnTouchListener{ _, _ -> true}
        adapter.setOnItemClickCallback(object: RecommendAdapter.OnItemClickCallback{
            override fun onDeclineClicked(data: RestaurantItem, position: Int) {
                binding.rvRecommendations.smoothScrollToPosition(position+1)
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

    private fun showLoading(isLoading: Boolean){
        binding.recommendProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}