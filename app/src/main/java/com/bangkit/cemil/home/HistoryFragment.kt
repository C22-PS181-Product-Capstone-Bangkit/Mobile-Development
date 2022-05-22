package com.bangkit.cemil.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.cemil.R
import com.bangkit.cemil.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {

    private lateinit var binding : FragmentHistoryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).apply{
            setSupportActionBar(binding.materialToolbar)
            setTitle("Recommendation History")
        }
        return binding.root
    }



}