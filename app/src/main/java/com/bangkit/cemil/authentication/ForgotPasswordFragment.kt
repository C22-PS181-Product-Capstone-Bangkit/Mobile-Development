package com.bangkit.cemil.authentication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.cemil.R
import com.bangkit.cemil.databinding.FragmentForgotPasswordBinding
import androidx.appcompat.widget.Toolbar

class ForgotPasswordFragment : Fragment() {

    private lateinit var binding : FragmentForgotPasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).setSupportActionBar(binding.materialToolbar)
        return binding.root
    }

}