package com.bangkit.cemil.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.cemil.SettingPreferences
import com.bangkit.cemil.dataStore
import com.bangkit.cemil.databinding.FragmentHistoryBinding
import com.bangkit.cemil.tools.HistoryAdapter
import com.bangkit.cemil.tools.model.HistoryItem
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList

class HistoryFragment : Fragment() {

    private lateinit var binding : FragmentHistoryBinding
    private val viewModel by viewModels<HistoryViewModel>()
    private var isAuthorized: Boolean = false
    private var accessToken: String? = null
    private val list = ArrayList<HistoryItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).apply{
            setSupportActionBar(binding.materialToolbar)
            supportActionBar?.setDisplayShowTitleEnabled(false)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pref = SettingPreferences.getInstance(requireContext().dataStore)
        lifecycleScope.launch {
            isAuthorized = pref.getPreferences()[SettingPreferences.AUTHORIZED_KEY] == true
            accessToken = pref.getPreferences()[SettingPreferences.AUTHORIZATION_TOKEN_KEY]
        }
        //Check isNullOrEmpty to only initialize API call after app is first launched
        if(isAuthorized && viewModel.historyData.value.isNullOrEmpty()){
            Log.e("HistoryFragment", " API called")
            viewModel.fetchProfileHistory(accessToken!!)
        }
        viewModel.historyData.observe(viewLifecycleOwner){
            list.clear()
            list.addAll(it)
            list.reverse()
            showRecyclerList()
        }

        binding.rvHistory.layoutManager = LinearLayoutManager(requireContext())
        binding.refreshRecyclerView.setOnRefreshListener {
            viewModel.fetchProfileHistory(accessToken!!)
            binding.refreshRecyclerView.isRefreshing = false
        }
    }

    private fun showRecyclerList(){
        val adapter = HistoryAdapter(list)
        binding.rvHistory.adapter = adapter
        adapter.setOnItemClickCallback(object : HistoryAdapter.OnItemClickCallback{
            override fun onItemClicked(data: HistoryItem) {
                Toast.makeText(context, data.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }
}