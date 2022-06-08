package com.bangkit.cemil.tools

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.cemil.R
import com.bangkit.cemil.databinding.ItemRowReviewBinding
import com.bangkit.cemil.tools.model.RestaurantReviewItem
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
import kotlin.math.abs

class ReviewAdapter(private val reviewList : List<RestaurantReviewItem>) : RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    class ViewHolder(var binding : ItemRowReviewBinding) : RecyclerView.ViewHolder(binding.root)

    private lateinit var onItemClickCallback: OnItemClickCallback
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val reviewItem = reviewList[position]
        holder.binding.apply {
            val daysAgo = "${getHowManyDaysAgo(reviewItem.updatedAt?.take(10).toString())} days ago"
            tvReviewerName.text = reviewItem.name
            tvReviewText.text = reviewItem.description
            tvReviewRating.text = reviewItem.rating?.toDouble().toString()
            tvReviewDate.text = daysAgo
            if (reviewItem.profilePic != null) {
                Glide.with(holder.itemView.context).load(reviewItem.profilePic).into(imgProfile)
            } else {
                Glide.with(holder.itemView.context).load(R.drawable.img_profile_placeholder).into(imgProfile)
            }

        }
        holder.binding.tvReviewText.setOnClickListener { onItemClickCallback.onItemClicked(reviewList[holder.adapterPosition]) }
    }

    override fun getItemCount() = reviewList.size

    private fun getHowManyDaysAgo(date: String): String{
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = dateFormatter.format(Calendar.getInstance().time)
        val differenceDays = (abs(dateFormatter.parse(currentDate)!!.time - dateFormatter.parse(date)!!.time)) / (24 * 3600000)
        return differenceDays.toString()
    }

    interface OnItemClickCallback{
        fun onItemClicked(data: RestaurantReviewItem)
    }
}