package com.bangkit.cemil.tools

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.cemil.databinding.ItemRowHomeRestoBinding
import com.bangkit.cemil.tools.model.RestaurantItem
import com.bumptech.glide.Glide

class RestaurantAdapter(private val restaurantList: List<RestaurantItem>) : RecyclerView.Adapter<RestaurantAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    class ViewHolder(var binding: ItemRowHomeRestoBinding): RecyclerView.ViewHolder(binding.root)

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowHomeRestoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val restaurantItem = restaurantList[position]
        val restaurantReviews = if(restaurantItem.countReview > 999) "999+" else restaurantItem.countReview
        val restaurantRating = "${(restaurantItem.rating ?: 0.0).toString()} (${restaurantReviews})"
        val restaurantDistance = "${restaurantItem.distance} km"
        holder.binding.apply {
            tvItemRestoName.text = restaurantItem.name
            tvItemRestoRating.text = restaurantRating
            tvItemRestoDistance.text = restaurantDistance
            if(restaurantItem.profilePic != null){
                Glide.with(holder.itemView.context).load(restaurantItem.profilePic).into(imgItemResto)
            }
        }
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(restaurantList[holder.adapterPosition]) }
    }

    override fun getItemCount(): Int = restaurantList.size

    interface OnItemClickCallback{
        fun onItemClicked(data: RestaurantItem)
    }
}