package com.bangkit.cemil.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.cemil.R
import com.bangkit.cemil.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private lateinit var binding : FragmentSearchBinding

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

}