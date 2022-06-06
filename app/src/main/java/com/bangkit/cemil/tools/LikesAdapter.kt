package com.bangkit.cemil.tools

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.cemil.databinding.ItemRowLikesBinding
import com.bangkit.cemil.tools.model.LikesItem
import com.bumptech.glide.Glide

class LikesAdapter(private val likedList: List<LikesItem>) : RecyclerView.Adapter<LikesAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    class ViewHolder(val binding: ItemRowLikesBinding): RecyclerView.ViewHolder(binding.root)

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowLikesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val likedItem = likedList[position]
        holder.binding.apply {
            val likedRestoRating = "${likedItem.restaurant!!.rating} (${likedItem.restaurant!!.countReview})"
            tvLikedResto.text = likedItem.restaurant!!.name
            tvLikedRestoRating.text = likedRestoRating
            Glide.with(holder.itemView.context).load(likedItem.restaurant!!.profilePic).into(imgLikedResto)
        }
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(likedList[holder.adapterPosition]) }
    }

    override fun getItemCount() = likedList.size

    interface OnItemClickCallback{
        fun onItemClicked(data: LikesItem)
    }
}