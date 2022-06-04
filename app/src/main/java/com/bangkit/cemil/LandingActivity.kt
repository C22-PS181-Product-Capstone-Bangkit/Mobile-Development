package com.bangkit.cemil

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bangkit.cemil.databinding.ActivityLandingBinding
import com.bangkit.cemil.home.MainActivity
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = "settings")
class LandingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLandingBinding
    private var themeMode: Boolean? = null
    private var firstTimeLanding: Boolean? = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pref = SettingPreferences.getInstance(dataStore)
        setThemeMode(pref)
        installSplashScreen()
        binding = ActivityLandingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        lifecycleScope.launch{
            firstTimeLanding = pref.getPreferences()[SettingPreferences.LANDING_KEY]
            if(firstTimeLanding == false){
                startMainActivity()
            }
        }

        val viewPagerAdapter = LandingScreenAdapter(this)
        binding.landingViewPager.adapter = viewPagerAdapter

        TabLayoutMediator(binding.landingTabLayout, binding.landingViewPager) { _, _ ->

        }.attach()

        binding.landingNextButton.setOnClickListener {
            binding.landingViewPager.currentItem++
        }

        binding.landingGetStartedButton.setOnClickListener {
            lifecycleScope.launch{
                pref.saveFirstTimeLanding(false)
            }
            startMainActivity()
        }

        binding.landingViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                isTheEndOfLandingScreen(position)
            }
        })
    }

    private fun isTheEndOfLandingScreen(currentItem: Int) {
        if (currentItem == 2) {
            binding.landingNextButton.visibility = View.INVISIBLE
            binding.landingGetStartedButton.visibility = View.VISIBLE
        } else {
            binding.landingGetStartedButton.visibility = View.INVISIBLE
            binding.landingNextButton.visibility = View.VISIBLE
        }
    }

    private inner class LandingScreenAdapter(activity: AppCompatActivity) :
        FragmentStateAdapter(activity) {
        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment {
            return LandingFragment.getInstance(position)
        }
    }

    private fun setThemeMode(pref: SettingPreferences){
        lifecycleScope.launch{
            themeMode = pref.getPreferences()[SettingPreferences.THEME_KEY]
            when(themeMode){
                true -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                false -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
        }
    }

    private fun startMainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}