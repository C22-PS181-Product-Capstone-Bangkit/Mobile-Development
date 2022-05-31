package com.bangkit.cemil.tools

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.cemil.databinding.ItemCategoryBinding
import com.bumptech.glide.Glide

class CategoryAdapter(private val listCategories: List<CategoryItem>) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    class ViewHolder(var binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root)

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val categoryItem = listCategories[position]
        holder.binding.apply {
            tvCategoryTitle.text = categoryItem.title
            Glide.with(holder.itemView.context).load(categoryItem.photoId).into(imgCategory)
        }
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listCategories[holder.adapterPosition])}
    }

    override fun getItemCount() = listCategories.size

    interface OnItemClickCallback {
        fun onItemClicked(data: CategoryItem)
    }
}