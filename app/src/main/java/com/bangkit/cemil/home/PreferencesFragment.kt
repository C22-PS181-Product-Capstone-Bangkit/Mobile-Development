package com.bangkit.cemil.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.bangkit.cemil.R
import com.bangkit.cemil.databinding.CategoryChipsBinding
import com.bangkit.cemil.databinding.FragmentPreferencesBinding
import com.google.android.material.chip.Chip

class PreferencesFragment : Fragment() {

    private lateinit var binding : FragmentPreferencesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPreferencesBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.materialToolbarPreferences)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            binding.toolbarNavUp.setOnClickListener {
                onBackPressed()
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFilterChips()
        binding.tvNextPreferences.setOnClickListener {
            val toRecommendRestaurantFragment = PreferencesFragmentDirections.actionPreferencesFragmentToRecommendRestaurantFragment()
            requireView().findNavController().navigate(toRecommendRestaurantFragment)
        }
    }

    private fun setFilterChips(){
        val chipCategoriesTitles = resources.getStringArray(R.array.category_titles)
        val chipAveragePriceTitles = resources.getStringArray(R.array.average_price)
        val chipOpenDaysTitles = resources.getStringArray(R.array.open_days)
        val chipDistanceTitles = resources.getStringArray(R.array.distance)
        val chipRatingTitles = "4.0+"
        for(title in chipCategoriesTitles){
            val chip = createChip(title)
            binding.chipGroupCategories.addView(chip)
        }
        for(title in chipAveragePriceTitles){
            val chip = createChip(title)
            binding.chipGroupAveragePrices.addView(chip)
        }
        for(title in chipOpenDaysTitles){
            val chip = createChip(title)
            binding.chipGroupOpenDays.addView(chip)
        }
        for(title in chipDistanceTitles){
            val chip = createChip(title)
            binding.chipGroupDistance.addView(chip)
        }
        binding.chipGroupRatng.addView(createChip(chipRatingTitles))
    }

    private fun createChip(title: String): Chip {
        val chip = CategoryChipsBinding.inflate(layoutInflater).root
        chip.text = title
        return chip
    }

}