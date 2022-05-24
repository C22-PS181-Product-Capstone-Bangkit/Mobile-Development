package com.bangkit.cemil.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bangkit.cemil.R
import com.bangkit.cemil.SettingPreferences
import com.bangkit.cemil.dataStore
import com.bangkit.cemil.databinding.FragmentAppearanceBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AppearanceFragment : Fragment() {

    private lateinit var binding : FragmentAppearanceBinding
    private var themeMode: Boolean? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAppearanceBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).apply{
            setSupportActionBar(binding.materialToolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            onBackPressedDispatcher.addCallback(object: OnBackPressedCallback(true){
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.profileFragment)
                }
            })
            binding.materialToolbar.setNavigationOnClickListener {
                onBackPressed()
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pref = SettingPreferences.getInstance(requireContext().dataStore)
        setRadioChecked()
        binding.themeRadioGroup.setOnCheckedChangeListener { _, id ->
            when(id){
                R.id.btnLight -> {
                    lifecycleScope.launch{
                        pref.saveThemePreference(false)
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    }
                }
                R.id.btnDark -> {
                    lifecycleScope.launch{
                        pref.saveThemePreference(true)
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    }
                }
                R.id.btnSystemDefault -> {
                    Toast.makeText(requireContext(), "TestingSystem", Toast.LENGTH_SHORT).show()
                    lifecycleScope.launch {
                        pref.deleteThemePreference()
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    }
                }
            }
        }
    }

    private fun setRadioChecked(){
        lifecycleScope.launch{
            themeMode = requireContext().dataStore.data.first()[SettingPreferences.THEME_KEY]
            when(themeMode){
                true -> binding.themeRadioGroup.check(R.id.btnDark)
                false -> binding.themeRadioGroup.check(R.id.btnLight)
                else -> binding.themeRadioGroup.check(R.id.btnSystemDefault)
            }
        }
    }
}

