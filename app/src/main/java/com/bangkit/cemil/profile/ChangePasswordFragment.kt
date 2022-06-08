package com.bangkit.cemil.profile

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.bangkit.cemil.SettingPreferences
import com.bangkit.cemil.dataStore
import com.bangkit.cemil.databinding.FragmentChangePasswordBinding
import kotlinx.coroutines.launch

class ChangePasswordFragment : Fragment() {

    private lateinit var binding: FragmentChangePasswordBinding
    private val viewModel by viewModels<ChangePasswordViewModel>()
    private lateinit var accessToken: String
    private var validityPass = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.materialToolbarChangePassword)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            binding.toolbarNavUp.setOnClickListener {
                onBackPressed()
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pref = SettingPreferences.getInstance(requireContext().dataStore)
        lifecycleScope.launch {
            accessToken =
                pref.getPreferences()[SettingPreferences.AUTHORIZATION_TOKEN_KEY].toString()
        }
        setInputListener()
        setButtonListener()

        viewModel.changePassResponse.observe(viewLifecycleOwner) {
            if (it != null) {
                if (it.message == "Password Berhasil Diubah") {
                    Toast.makeText(context, "Password successfully changed!", Toast.LENGTH_SHORT)
                        .show()
                    val toProfileFragment =
                        ChangePasswordFragmentDirections.actionChangePasswordFragmentToProfileFragment()
                    requireView().findNavController().navigate(toProfileFragment)
                } else {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setInputListener() {
        binding.etNewPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                validityPass = if (p0.toString().trim().length <= 5) {
                    binding.etNewPassword.error = "Password needs to be at least 6 characters."
                    false
                } else true
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    private fun setButtonListener() {
        binding.btnChangePassword.setOnClickListener {
            val currentPass = binding.etCurrentPassword.text.toString().trim()
            val password = binding.etNewPassword.text.toString().trim()
            val confirmPass = binding.etConfirmPassword.text.toString().trim()
            if (currentPass.isBlank()) {
                binding.etCurrentPassword.error = "Fill your current password."
            } else if (password.isBlank()) {
                binding.etNewPassword.error = "Fill your new password."
            } else if (confirmPass != password) {
                binding.etConfirmPassword.error = "Confirmation of new password isn't equal!"
            } else if (validityPass) {
                viewModel.requestChangePassword(accessToken, currentPass, password)
            }
        }

        binding.btnClearFields.setOnClickListener {
            binding.etCurrentPassword.text.clear()
            binding.etNewPassword.text.clear()
            binding.etConfirmPassword.text.clear()
        }
    }
}