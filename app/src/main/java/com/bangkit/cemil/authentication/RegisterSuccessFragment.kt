package com.bangkit.cemil.authentication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.bangkit.cemil.databinding.FragmentRegisterSuccessBinding

class RegisterSuccessFragment : Fragment() {

    private lateinit var binding : FragmentRegisterSuccessBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterSuccessBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnRegisterSuccess.setOnClickListener {
            val toProfileFragment = RegisterSuccessFragmentDirections.actionRegisterSuccessFragmentToProfileFragment()
            requireView().findNavController().navigate(toProfileFragment)
        }
    }

}