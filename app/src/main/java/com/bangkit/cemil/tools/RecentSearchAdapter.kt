package com.bangkit.cemil.tools

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.cemil.databinding.ItemRecentSearchesBinding

class RecentSearchAdapter(private val listRecentSearches: List<String>): RecyclerView.Adapter<RecentSearchAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    class ViewHolder(var binding: ItemRecentSearchesBinding) : RecyclerView.ViewHolder(binding.root)

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecentSearchesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recentSearchItem = listRecentSearches[position]
        holder.binding.tvProfileSetting.text = recentSearchItem
        holder.binding.tvProfileSetting.setOnClickListener { onItemClickCallback.onItemClicked(listRecentSearches[holder.adapterPosition]) }
        holder.binding.imgRecentSymbol.setOnClickListener { onItemClickCallback.onEraseClicked(listRecentSearches[holder.adapterPosition], position) }
    }

    override fun getItemCount() : Int{
        if(listRecentSearches.size > 5){
            return 5
        }
        return listRecentSearches.size
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: String)
        fun onEraseClicked(data: String, position: Int)
    }
}