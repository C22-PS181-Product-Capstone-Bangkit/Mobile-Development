package com.bangkit.cemil.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.bangkit.cemil.R
import com.bangkit.cemil.databinding.FragmentSearchBinding
import com.bangkit.cemil.tools.CategoryAdapter
import com.bangkit.cemil.tools.CategoryItem

class SearchFragment : Fragment() {

    private lateinit var binding : FragmentSearchBinding

    private val listCategories: ArrayList<CategoryItem>
        get() {
            val categoryList = ArrayList<CategoryItem>()
            val settingTitles = resources.getStringArray(R.array.category_titles)
            val settingPhotoIds = resources.obtainTypedArray(R.array.category_photos)
            for (index in settingTitles.indices) {
                val categoryItem = CategoryItem(settingTitles[index], settingPhotoIds.getResourceId(index, -1))
                categoryList.add(categoryItem)
            }
            return categoryList
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).apply{
            setSupportActionBar(binding.materialToolbar)
            supportActionBar?.setDisplayShowTitleEnabled(false)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvClearAll.setOnClickListener {
            Toast.makeText(context, "Clear all Recent Searches", Toast.LENGTH_SHORT).show()
        }

        val categoriesAdapter = CategoryAdapter(listCategories)
        binding.rvCategories.apply{
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, 3)
            adapter = categoriesAdapter
        }

        categoriesAdapter.setOnItemClickCallback(object: CategoryAdapter.OnItemClickCallback{
            override fun onItemClicked(data: CategoryItem) {
                Toast.makeText(context, data.title, Toast.LENGTH_SHORT).show()
            }
        })
    }

}