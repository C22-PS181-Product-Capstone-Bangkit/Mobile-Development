package com.bangkit.cemil.tools

import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.cemil.R
import com.bangkit.cemil.databinding.ItemRecommendedRestaurantBinding
import com.bangkit.cemil.tools.model.LikesItem
import com.bangkit.cemil.tools.model.ProfileResponse
import com.bangkit.cemil.tools.model.RestaurantItem
import com.bumptech.glide.Glide
import java.text.NumberFormat
import java.util.*

class RecommendAdapter(private val listRestaurant : List<RestaurantItem>, val profileData : ProfileResponse): RecyclerView.Adapter<RecommendAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    class ViewHolder(val binding: ItemRecommendedRestaurantBinding): RecyclerView.ViewHolder(binding.root)

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecommendedRestaurantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val restaurantItem = listRestaurant[position % listRestaurant.size]
        val likeItem = profileData.likes?.any{it.restaurant?.id == restaurantItem.id}
        val restoAverageCost = "${currencyFormat((restaurantItem.price!!.toInt() / 2).toString())} / person"
        val restoRatings = "${restaurantItem.rating} (${restaurantItem.countReview})"
        val categoryAmount = restaurantItem.category?.split(", ")
        if(categoryAmount?.size!! >= 1){
            holder.binding.tvRecommendRestaurantCategory.text = categoryAmount[0]
        }
        if(categoryAmount.size >= 2){
            holder.binding.tvRecommendRestaurantCategory2.apply {
                visibility = View.VISIBLE
                text = categoryAmount[1]
            }
        }
        if(categoryAmount.size >= 3){
            holder.binding.tvRecommendRestaurantCategory3.apply {
                visibility = View.VISIBLE
                text = categoryAmount[2]
            }
        }
        holder.binding.apply {
            tvRecommendRestoName.text = restaurantItem.name
            tvRecommendRestoCost.text = restoAverageCost
            tvRecommendRestoLocation.text = restaurantItem.location
            tvRecommendRestoTime.text = restaurantItem.openHour
            tvRecommendRestoRatings.text = restoRatings
            tvRecommendRestoDistance.text = restaurantItem.distance
            Glide.with(holder.itemView).load(restaurantItem.profilePic).into(imgBannerRecommend)
            imgBannerRecommend.minimumHeight = imgBannerRecommend.width
            fabDecline.setOnClickListener { onItemClickCallback.onDeclineClicked(listRestaurant[holder.adapterPosition % listRestaurant.size], holder.adapterPosition)}
            fabAccept.setOnClickListener { onItemClickCallback.onAcceptClicked(listRestaurant[holder.adapterPosition % listRestaurant.size], holder.adapterPosition % listRestaurant.size)}
        }
        if(likeItem == true) toggleLikeButtonOn(holder) else toggleLikeButtonOff(holder)
    }

    override fun getItemCount() = Int.MAX_VALUE

    private fun currencyFormat(amount: String) : String{
        val rupiahFormatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
        return rupiahFormatter.format(amount.toDouble())
    }
    private fun toggleLikeButtonOff(holder: ViewHolder){
        holder.binding.fabRecommend.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(holder.itemView.context, R.color.secondary))
        holder.binding.fabRecommend.setOnClickListener {
            onItemClickCallback.onRecommendClicked(listRestaurant[holder.adapterPosition % listRestaurant.size], holder.adapterPosition % listRestaurant.size)
            toggleLikeButtonOn(holder)
        }
    }

    private fun toggleLikeButtonOn(holder: ViewHolder){
        holder.binding.fabRecommend.imageTintList = ColorStateList.valueOf(com.google.android.material.R.attr.strokeColor)
        holder.binding.fabRecommend.setOnClickListener {
            onItemClickCallback.onRecommendDeleteClicked(listRestaurant[holder.adapterPosition % listRestaurant.size], holder.adapterPosition % listRestaurant.size)
            toggleLikeButtonOff(holder)
        }
    }

    interface OnItemClickCallback{
        fun onDeclineClicked(data: RestaurantItem, position: Int)
        fun onRecommendClicked(data: RestaurantItem, position: Int)
        fun onRecommendDeleteClicked(data: RestaurantItem, position: Int)
        fun onAcceptClicked(data: RestaurantItem, position: Int)
    }
}