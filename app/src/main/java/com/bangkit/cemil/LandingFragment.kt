package com.bangkit.cemil

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.viewpager2.widget.ViewPager2


class LandingFragment : Fragment() {
    private val ARG_POSITION = "position"
    private var fragmentPosition : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentPosition = arguments?.getInt(ARG_POSITION) ?: 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view : View
        when(fragmentPosition){
            0 -> {
                view = inflater.inflate(R.layout.fragment_landing1, container, false)
            }
            1 -> {
                view = inflater.inflate(R.layout.fragment_landing2, container, false)
            }
            else ->{
                view = inflater.inflate(R.layout.fragment_landing3, container, false)
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewPager : ViewPager2 = activity?.findViewById(R.id.landingViewPager) as ViewPager2
        val btnLandingScreen : Button = view.findViewById(R.id.btnLandingScreen)
        btnLandingScreen.setOnClickListener{
            if(fragmentPosition < 2){
                viewPager.currentItem++
            }else{
                //TODO direct to login page fragment
            }
        }
    }

    companion object {
        @JvmStatic
        fun getInstance(position : Int) =
            LandingFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_POSITION, position)
                }
            }
    }
}