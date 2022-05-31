package com.bangkit.cemil.tools

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.cemil.databinding.ItemCategoryBinding
import com.bangkit.cemil.databinding.ItemRecentSearchesBinding
import com.bangkit.cemil.databinding.ItemSettingBinding

class RecentSearchAdapter(private val listRecentSearches: List<RecentSearchItem>): RecyclerView.Adapter<RecentSearchAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: SettingAdapter.OnItemClickCallback

    class ViewHolder(var binding: ItemRecentSearchesBinding) : RecyclerView.ViewHolder(binding.root)

    fun setOnItemClickCallback(onItemClickCallback: SettingAdapter.OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecentSearchesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecentSearchAdapter.ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recentSearchItem = listRecentSearches[position]
        holder.binding.tvProfileSetting.text = recentSearchItem.restaurantName
    }

    override fun getItemCount() = listRecentSearches.size
}