package com.bangkit.cemil.authentication

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.bangkit.cemil.SettingPreferences
import com.bangkit.cemil.dataStore
import com.bangkit.cemil.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private lateinit var binding : FragmentRegisterBinding
    private val viewModel by viewModels<RegisterViewModel>()
    private var validityEmail = false
    private var validityPass = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pref = SettingPreferences.getInstance(requireContext().dataStore)
        setInputListener()
        setButtonListener()
        viewModel.registerResponse.observe(viewLifecycleOwner){ registerResponse ->
            if(registerResponse.message != null){
                Toast.makeText(context, registerResponse.message, Toast.LENGTH_SHORT).show()
            }else if(registerResponse.accessToken != null){
                viewModel.saveSessionInfo(pref, registerResponse.accessToken)
                Toast.makeText(context, "Registration Succesful!", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.preferenceReady.observe(viewLifecycleOwner){
            if(it){
                val toRegisterSuccessFragment = RegisterFragmentDirections.actionRegisterFragmentToRegisterSuccessFragment()
                requireView().findNavController().navigate(toRegisterSuccessFragment)
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner){
            showLoading(it)
        }
    }

    private fun setInputListener(){
        binding.etRegisterEmail.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                validityEmail = if(!Patterns.EMAIL_ADDRESS.matcher(p0.toString()).matches()){
                    binding.etRegisterEmail.error = "Invalid email format!"
                    false
                }else true
            }
            override fun afterTextChanged(p0: Editable?) {
            }
        })

        binding.etRegisterPassword.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                validityPass = if(p0.toString().trim().length <= 5){
                    binding.etRegisterPassword.error = "Password needs to be at least 6 characters."
                    false
                }else true
            }
            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    private fun setButtonListener(){
        binding.buttonAccountRegister.setOnClickListener {
            val name = binding.etRegisterName.text.toString().trim()
            val email = binding.etRegisterEmail.text.toString().trim()
            val password = binding.etRegisterPassword.text.toString().trim()
            val confirmPass = binding.etRegisterConfirmPassword.text.toString().trim()
            if(name.isBlank()){
                binding.etRegisterName.error = "Name can't be empty!"
            }else if(email.isBlank()){
                binding.etRegisterEmail.error = "Email can't be empty!"
            }else if(password.isBlank()) {
                binding.etRegisterPassword.error = "Password can't be empty!"
            }else if(confirmPass != password){
                binding.etRegisterConfirmPassword.error = "Confirmation and entered password isn't equal!"
            }else if(!binding.materialCheckboxRegister.isChecked){
                Toast.makeText(context, "Please read and accept the terms and conditions!", Toast.LENGTH_SHORT).show()
            }else if(validityEmail && validityPass){
                viewModel.requestRegister(name, email, password)
            }
        }
    }

    private fun showLoading(isLoading: Boolean){
        binding.loadingRegister.visibility = if(isLoading) View.VISIBLE else View.GONE
    }
}