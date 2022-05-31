package com.bangkit.cemil.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bangkit.cemil.LandingFragment
import com.bangkit.cemil.R
import com.bangkit.cemil.databinding.FragmentRestaurantBinding

class RestaurantFragment : Fragment() {

    private val argument = "position"
    private lateinit var binding : FragmentRestaurantBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        arguments?.getInt()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRestaurantBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun getInstance(position : Int) =
            RestaurantFragment().apply {
                arguments = Bundle().apply {
                    putInt(argument, position)
                }
            }
    }

}