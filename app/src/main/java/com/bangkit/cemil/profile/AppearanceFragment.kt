package com.bangkit.cemil.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.bangkit.cemil.R
import com.bangkit.cemil.databinding.FragmentAppearanceBinding

class AppearanceFragment : Fragment() {

    private lateinit var binding : FragmentAppearanceBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAppearanceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.themeRadioGroup.setOnCheckedChangeListener { _, id ->
            when(id){
                R.id.btnLight -> {
                    // Temporary, soon will change to save/fetch settings into/from datastore
                    Toast.makeText(requireContext(), "Testing", Toast.LENGTH_SHORT).show()
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
                R.id.btnDark -> {
                    Toast.makeText(requireContext(), "TestingDark", Toast.LENGTH_SHORT).show()
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
                R.id.btnSystemDefault -> {
                    Toast.makeText(requireContext(), "TestingSystem", Toast.LENGTH_SHORT).show()
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }
            }
        }
    }

}