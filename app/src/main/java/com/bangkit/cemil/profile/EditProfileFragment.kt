package com.bangkit.cemil.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.bangkit.cemil.R
import com.bangkit.cemil.databinding.FragmentEditProfileBinding

class EditProfileFragment : Fragment() {

    private lateinit var binding : FragmentEditProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).apply {
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
            title = "Edit Profile"
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}