package com.bangkit.cemil.tools

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.cemil.databinding.ItemSettingBinding

class SettingAdapter(private val listSetting: List<SettingItem>) :
    RecyclerView.Adapter<SettingAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    class ViewHolder(var binding: ItemSettingBinding) : RecyclerView.ViewHolder(binding.root)

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSettingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val settingItem = listSetting[position]
        holder.binding.tvProfileSetting.apply {
            setCompoundDrawablesWithIntrinsicBounds(settingItem.drawable, null, null, null)
            text = settingItem.text
        }
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listSetting[holder.adapterPosition]) }
    }

    override fun getItemCount() = listSetting.size

    interface OnItemClickCallback {
        fun onItemClicked(data: SettingItem)
    }
}