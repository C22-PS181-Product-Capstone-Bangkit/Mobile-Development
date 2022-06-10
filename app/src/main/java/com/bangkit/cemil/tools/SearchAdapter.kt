package com.bangkit.cemil.tools

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.cemil.databinding.ItemRowSearchBinding
import com.bangkit.cemil.tools.model.RestaurantItem
import com.bumptech.glide.Glide

class SearchAdapter(private val listSearch : List<RestaurantItem>): RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    class ViewHolder(var binding : ItemRowSearchBinding) : RecyclerView.ViewHolder(binding.root)

    private lateinit var onItemClickCallback: OnItemClickCallback
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowSearchBinding.inflate(LayoutInflater.from(parent.context), parent ,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val listItem = listSearch[position]
        holder.binding.tvSearchResto.text = listItem.name
        holder.binding.tvSearchRestoAddress.text = listItem.location
        holder.binding.tvHistoryRestoRating.text = listItem.rating
        Glide.with(holder.itemView).load(listItem.profilePic).into(holder.binding.imgSearchResto)
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listSearch[holder.adapterPosition]) }
    }

    override fun getItemCount(): Int = listSearch.size

    interface OnItemClickCallback{
        fun onItemClicked(data: RestaurantItem)
    }
}