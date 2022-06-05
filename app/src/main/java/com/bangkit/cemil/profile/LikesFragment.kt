package com.bangkit.cemil.profile

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.cemil.SettingPreferences
import com.bangkit.cemil.dataStore
import com.bangkit.cemil.databinding.FragmentLikesBinding
import com.bangkit.cemil.home.HomeFragmentDirections
import com.bangkit.cemil.tools.HistoryAdapter
import com.bangkit.cemil.tools.LikesAdapter
import com.bangkit.cemil.tools.model.HistoryItem
import com.bangkit.cemil.tools.model.LikesItem
import kotlinx.coroutines.launch


class LikesFragment : Fragment() {

    private lateinit var binding : FragmentLikesBinding
    private val viewModel by viewModels<LikesViewModel>()
    private var accessToken: String? = null
    private val list = ArrayList<LikesItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLikesBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).apply{
            setSupportActionBar(binding.materialToolbarLikes)
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
        lifecycleScope.launch {
            accessToken = pref.getPreferences()[SettingPreferences.AUTHORIZATION_TOKEN_KEY]
        }
        if(accessToken != null){
            viewModel.fetchLikesData(accessToken!!)
        }
        viewModel.likesData.observe(viewLifecycleOwner){
            if(it != null){
                list.clear()
                list.addAll(it)
                list.reverse()
                showRecyclerList()
            }
        }
        binding.rvLikes.layoutManager = LinearLayoutManager(requireContext())
        binding.refreshLikesRecyclerView.setOnRefreshListener {
            viewModel.fetchLikesData(accessToken!!)
            binding.refreshLikesRecyclerView.isRefreshing = false
        }
    }
    private fun showRecyclerList(){
        val adapter = LikesAdapter(list)
        binding.rvLikes.adapter = adapter
        adapter.setOnItemClickCallback(object : LikesAdapter.OnItemClickCallback{
            override fun onItemClicked(data: LikesItem) {
                val toRestaurantFragment = LikesFragmentDirections.actionLikesFragmentToRestaurantFragment(data.id.toString())
                requireView().findNavController().navigate(toRestaurantFragment)
            }
        })
    }

}