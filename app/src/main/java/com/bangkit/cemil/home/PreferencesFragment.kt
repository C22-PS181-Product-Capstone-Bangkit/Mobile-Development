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

    private lateinit var binding: FragmentPreferencesBinding

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

        var categories = ""
        var prices = ""
        var distance = ""
        var ratings = ""

        binding.tvNextPreferences.setOnClickListener {
            for (categoryChip in binding.chipGroupCategories.checkedChipIds) {
                val chip = binding.chipGroupCategories.findViewById<Chip>(categoryChip)
                categories += if (categories.isEmpty()) chip.text.toString() else ", ${chip.text}"
            }

            for (priceChip in binding.chipGroupAveragePrices.checkedChipIds) {
                val chip = binding.chipGroupAveragePrices.findViewById<Chip>(priceChip)
                prices += if (prices.isEmpty()) chip.text.toString() else ", ${chip.text}"
            }

            for (distanceChip in binding.chipGroupDistance.checkedChipIds) {
                val chip = binding.chipGroupDistance.findViewById<Chip>(distanceChip)
                distance += if (distance.isEmpty()) chip.text.toString() else " ${chip.text}"
            }

            if (binding.chipGroupRatng.checkedChipIds.isNotEmpty()) {
                for (ratingChip in binding.chipGroupRatng.checkedChipIds) {
                    val chip =
                        binding.chipGroupRatng.findViewById<Chip>(ratingChip)
                    ratings += chip.text.toString()
                }
            }

            val toRecommendRestaurantFragment =
                PreferencesFragmentDirections.actionPreferencesFragmentToRecommendRestaurantFragment(
                    categories,
                    prices,
                    distance,
                    ratings
                )
            requireView().findNavController().navigate(toRecommendRestaurantFragment)
        }
    }

    private fun setFilterChips() {
        val chipCategoriesTitles = resources.getStringArray(R.array.category_titles)
        val chipAveragePriceTitles = resources.getStringArray(R.array.average_price)
        val chipDistanceTitles = resources.getStringArray(R.array.distance)
        val chipRatingTitles = "4.0+"
        for (title in chipCategoriesTitles) {
            val chip = createChip(title)
            binding.chipGroupCategories.addView(chip)
        }
        for (title in chipAveragePriceTitles) {
            val chip = createChip(title)
            binding.chipGroupAveragePrices.addView(chip)
        }
        for (title in chipDistanceTitles) {
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