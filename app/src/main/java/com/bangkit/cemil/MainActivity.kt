package com.bangkit.cemil

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bangkit.cemil.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBinding : ActivityMainBinding
    private lateinit var viewPager: ViewPager2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
        supportActionBar?.hide()

        viewPager = activityMainBinding.landingViewPager
        val viewPagerAdapter = LandingScreenAdapter(this)
        viewPager.adapter = viewPagerAdapter
    }

    override fun onBackPressed() {
        if(viewPager.currentItem == 0){
            super.onBackPressed()
        }else{
            viewPager.currentItem -= 1
        }
    }

    private inner class LandingScreenAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity){
        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment {
            return LandingFragment.getInstance(position)
        }
    }
}