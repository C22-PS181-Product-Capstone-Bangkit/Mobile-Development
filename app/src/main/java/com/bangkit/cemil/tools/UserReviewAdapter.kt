package com.bangkit.cemil.tools

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.cemil.databinding.ItemRowReviewBinding
import com.bangkit.cemil.tools.model.ReviewItem
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

class UserReviewAdapter(private val reviewList : List<ReviewItem>) : RecyclerView.Adapter<UserReviewAdapter.ViewHolder>() {

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
            var restoName = reviewItem.restaurant!!.name
            if(restoName!!.length >= 27){
                restoName = "${restoName.take(24)}..."
            }
            tvReviewerName.textSize = 16F
            tvReviewerName.text = restoName
            tvReviewText.text = reviewItem.description
            tvReviewRating.text = reviewItem.rating?.toDouble().toString()
            tvReviewDate.text = daysAgo
            Glide.with(holder.itemView.context).load(reviewItem.restaurant.profilePic).into(imgProfile)
        }
        holder.binding.tvReviewerName.setOnClickListener { onItemClickCallback.onItemClicked(reviewList[holder.adapterPosition]) }
        holder.binding.imgProfile.setOnClickListener { onItemClickCallback.onItemClicked(reviewList[holder.adapterPosition]) }
    }

    override fun getItemCount() = reviewList.size

    private fun getHowManyDaysAgo(date: String): String{
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = dateFormatter.format(Calendar.getInstance().time)
        val differenceDays = (abs(dateFormatter.parse(currentDate)!!.time - dateFormatter.parse(date)!!.time)) / (24 * 3600000)
        return differenceDays.toString()
    }

    interface OnItemClickCallback{
        fun onItemClicked(data: ReviewItem)
    }
}