package com.bangkit.cemil

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bangkit.cemil.databinding.ActivityLandingBinding
import com.bangkit.cemil.home.MainActivity
import com.google.android.material.tabs.TabLayoutMediator

class LandingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLandingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityLandingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val viewPagerAdapter = LandingScreenAdapter(this)
        binding.landingViewPager.adapter = viewPagerAdapter

        TabLayoutMediator(binding.landingTabLayout, binding.landingViewPager) { _, _ ->

        }.attach()

        binding.landingNextButton.setOnClickListener {
            binding.landingViewPager.currentItem++
        }

        binding.landingGetStartedButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
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
}