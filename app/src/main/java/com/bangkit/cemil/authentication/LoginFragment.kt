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
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.bangkit.cemil.SettingPreferences
import com.bangkit.cemil.dataStore
import com.bangkit.cemil.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModels<LoginViewModel>()
    private var validityEmail = false
    private var validityPass = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container ,false)
        (activity as AppCompatActivity).apply{
            setSupportActionBar(binding.materialToolbarLogin)
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
        setInputListener()
        setButtonListener()
        viewModel.loginResponse.observe(viewLifecycleOwner){ loginResponse ->
            if(loginResponse?.message != null){
                Toast.makeText(context, loginResponse.message, Toast.LENGTH_SHORT).show()
            }else if(loginResponse?.accessToken != null){
                viewModel.saveSessionInfo(pref, loginResponse.accessToken)
                Toast.makeText(context, "Login Succesful", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context, loginResponse?.message + "asd", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.preferenceReady.observe(viewLifecycleOwner){
            if(it){
                val toProfileFragment = LoginFragmentDirections.actionLoginFragmentToProfileFragment()
                requireView().findNavController().navigate(toProfileFragment)
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner){
            showLoading(it)
        }
    }

    private fun setInputListener(){
        binding.etLoginEmail.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                validityEmail = if(!Patterns.EMAIL_ADDRESS.matcher(p0.toString()).matches()){
                    binding.etLoginEmail.error = "Invalid email format!"
                    false
                }else true
            }
            override fun afterTextChanged(p0: Editable?) {
            }
        })

        binding.etLoginPassword.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                validityPass = if(p0.toString().trim().length <= 5){
                    binding.etLoginPassword.error = "Password needs to be at least 6 characters."
                    false
                }else true
            }
            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    private fun setButtonListener(){
        binding.buttonAccountLogin.setOnClickListener {
            val email = binding.etLoginEmail.text.toString().trim()
            val password = binding.etLoginPassword.text.toString().trim()
            if(email.isBlank()){
                binding.etLoginEmail.error = "Email can't be empty!"
            }else if(password.isBlank()){
                binding.etLoginPassword.error = "Password can't be empty!"
            }else if(validityEmail && validityPass){
                viewModel.requestLogin(email, password)
            }
        }
    }

    private fun showLoading(isLoading: Boolean){
        binding.loadingLogin.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}