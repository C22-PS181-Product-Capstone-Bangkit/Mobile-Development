package com.bangkit.cemil.tools

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.cemil.databinding.ItemRowHistoryBinding
import com.bangkit.cemil.tools.model.HistoryItem
import com.bumptech.glide.Glide

class HistoryAdapter(private val historyList: List<HistoryItem>): RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    class ViewHolder(var binding : ItemRowHistoryBinding) : RecyclerView.ViewHolder(binding.root)

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val historyRestoItem = historyList[position]
        holder.binding.tvHistoryRestoName.text = historyRestoItem.restaurant!!.name
        holder.binding.tvHistoryRestoRating.text = historyRestoItem.restaurant.rating.toString()
        holder.binding.tvHistoryDate.text = historyRestoItem.createdAt?.take(10).toString()
        Glide.with(holder.itemView).load(historyRestoItem.restaurant.profilePic).into(holder.binding.imgHistoryResto)
        holder.binding.btnHistoryReviews.setOnClickListener { onItemClickCallback.onItemClicked(historyList[holder.adapterPosition])}
    }

    override fun getItemCount(): Int = historyList.size

    interface OnItemClickCallback {
        fun onItemClicked(data: HistoryItem)
    }
}

