package com.bangkit.cemil.tools

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.cemil.databinding.ItemRowReviewBinding
import com.bangkit.cemil.tools.model.RestaurantReviewItem

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
            tvReviewerName.text = reviewItem.name
            tvReviewText.text = reviewItem.description
        }

    }

    override fun getItemCount() = reviewList.size

    interface OnItemClickCallback{
        fun onItemClicked(data: RestaurantReviewItem)
    }
}