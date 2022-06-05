package com.bangkit.cemil.tools

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.cemil.databinding.ItemRowHomeRestoBinding
import com.bangkit.cemil.databinding.ItemRowReviewBinding
import com.bangkit.cemil.tools.model.ReviewItem

class ReviewAdapter(private val reviewList : List<ReviewItem>) : RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    class ViewHolder(var binding : ItemRowReviewBinding) : RecyclerView.ViewHolder(binding.root)

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
}