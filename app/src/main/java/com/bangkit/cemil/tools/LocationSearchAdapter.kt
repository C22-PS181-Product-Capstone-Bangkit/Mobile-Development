package com.bangkit.cemil.tools

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.cemil.databinding.ItemLocationSearchBinding

class LocationSearchAdapter(private val listLocations: List<LocationSearchItem>) : RecyclerView.Adapter<LocationSearchAdapter.ViewHolder>() {

    class ViewHolder(var binding: ItemLocationSearchBinding ) : RecyclerView.ViewHolder(binding.root)

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLocationSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val locationItem = listLocations[position]
        holder.binding.apply {
            tvLocationName.text = locationItem.locationName
            tvLocationDesc.text = locationItem.locationDesc
        }
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listLocations[holder.adapterPosition]) }
    }

    override fun getItemCount(): Int = listLocations.size

    interface OnItemClickCallback {
        fun onItemClicked(data: LocationSearchItem)
    }
}