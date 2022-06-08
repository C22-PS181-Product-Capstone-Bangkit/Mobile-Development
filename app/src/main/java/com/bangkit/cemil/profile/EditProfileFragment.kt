package com.bangkit.cemil.profile

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bangkit.cemil.SettingPreferences
import com.bangkit.cemil.dataStore
import com.bangkit.cemil.databinding.FragmentEditProfileBinding
import com.bangkit.cemil.tools.ImageUtils.reduceFileImage
import com.bangkit.cemil.tools.ImageUtils.uriToFile
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

class EditProfileFragment : Fragment() {

    private lateinit var binding: FragmentEditProfileBinding
    private val viewModel by viewModels<EditProfileViewModel>()
    private var getFile: File? = null
    private var validityEmail = false
    private var validityPass = false
    private var accessToken: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.materialToolbarEdit)
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
            accessToken = pref.getPreferences()[SettingPreferences.AUTHORIZATION_TOKEN_KEY]
        }
        if (accessToken != null) {
            viewModel.fetchProfile(accessToken!!)
        }
        viewModel.profileData.observe(viewLifecycleOwner) {
            binding.etEditName.setText(it.user?.name)
            binding.etEditEmail.setText(it.user?.email)
            binding.etEditPhone.setText(it.user?.phone)
        }
        viewModel.editResponse.observe(viewLifecycleOwner) {
            if (it?.message != null) {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                if (it.message == "Data User berhasil diperbarui") {
                    requireActivity().onBackPressed()
                }
            }
        }
        setInputListener()
        setButtonListener()
    }

    private fun setInputListener() {
        binding.etEditEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                validityEmail = if (!Patterns.EMAIL_ADDRESS.matcher(p0).matches()) {
                    binding.etEditEmail.error = "Invalid email format!"
                    false
                } else true
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        binding.etEditPhone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                validityPass = if (p0.toString().trim() == "") {
                    binding.etEditPhone.error = null
                    true
                } else if (!Patterns.PHONE.matcher(p0).matches()) {
                    binding.etEditPhone.error = "Invalid phone format!"
                    false
                } else true
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    private fun setButtonListener() {
        binding.tvSaveEdit.setOnClickListener {
            val name = binding.etEditName.text.toString().trim()
            val email = binding.etEditEmail.text.toString().trim()
            val phone = binding.etEditPhone.text.toString().trim()
            if (name.isBlank()) {
                binding.etEditName.error = "Name can't be empty!"
            } else if (email.isBlank()) {
                binding.etEditEmail.error = "Email can't be empty!"
            } else if (validityEmail && validityPass) {
                viewModel.postEditProfile(accessToken.toString(), name, email, phone)
            }
        }
        binding.imgProfileEdit.setOnClickListener {
            openGallery()
        }
    }

    private fun openGallery() {
        val intentGallery = Intent()
        intentGallery.action = Intent.ACTION_GET_CONTENT
        intentGallery.type = "image/*"
        val chooser = Intent.createChooser(intentGallery, "Choose Picture")
        launcherIntentGallery.launch(chooser)

    }

    private val launcherIntentGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, requireContext())
            getFile = myFile
            if (getFile != null) {
                val file = getFile
                Glide.with(requireContext())
                    .load(file)
                    .into(binding.imgProfileEdit)
                //val file = reduceFileImage(getFile as File)
//                val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val requestImageFile = file!!.asRequestBody("image/*".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part =
                    MultipartBody.Part.createFormData("file", file.name, requestImageFile)
                viewModel.uploadProfilePicture(accessToken.toString(), imageMultipart)
            } else {
                Toast.makeText(context, "Upload image failed.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}