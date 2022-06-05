package com.bangkit.cemil.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.cemil.R
import com.bangkit.cemil.SettingPreferences
import com.bangkit.cemil.dataStore
import com.bangkit.cemil.databinding.FragmentProfileBinding
import com.bangkit.cemil.tools.SettingAdapter
import com.bangkit.cemil.tools.SettingItem
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class ProfileFragment : Fragment(), LogoutDialogFragment.DialogCallback {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: ProfileViewModel
    private var isAuthorized: Boolean = false
    private var accessToken: String? = null

    private val listSetting: ArrayList<SettingItem>
        get() {
            val tempList = ArrayList<SettingItem>()
            val settingNames = resources.getStringArray(R.array.setting_names)
            val settingDrawableIds = resources.obtainTypedArray(R.array.setting_icons)

            for (index in settingNames.indices) {
                val settingItem = SettingItem(
                    settingNames[index],
                    ResourcesCompat.getDrawable(
                        resources,
                        settingDrawableIds.getResourceId(index, -1),
                        null
                    )
                )
                tempList.add(settingItem)
            }
            settingDrawableIds.recycle()
            return tempList
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]

        (activity as AppCompatActivity).setSupportActionBar(binding.materialToolbar)
        (activity as AppCompatActivity).supportActionBar?.title = null
        (activity as AppCompatActivity).findViewById<BottomNavigationView>(R.id.bottomNavigationView).visibility =
            View.VISIBLE

        val pref = SettingPreferences.getInstance(requireContext().dataStore)
        lifecycleScope.launch {
            isAuthorized = pref.getPreferences()[SettingPreferences.AUTHORIZED_KEY] == true
            accessToken = pref.getPreferences()[SettingPreferences.AUTHORIZATION_TOKEN_KEY]
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        viewModel.profileData.observe(viewLifecycleOwner) { profileData ->
            if (profileData != null) {
                if (profileData.message == null && profileData.data == null) {
                    binding.tvProfileName.text = profileData.user?.name
                    binding.tvProfileEmail.text = profileData.user?.email
                    if (profileData.user?.profilePic != null) {
                        Glide.with(requireContext())
                            .load(profileData.user.profilePic)
                            .placeholder(R.drawable.bg_shimmer)
                            .into(binding.imgProfile)
                    } else {
                        Glide.with(requireContext())
                            .load(R.drawable.img_profile_placeholder)
                            .into(binding.imgProfile)
                    }
                }
            }
        }

        checkAuthorization()
        binding.layoutUnauthorized.btnCreateAccount.setOnClickListener {
            val toRegisterFragment =
                ProfileFragmentDirections.actionProfileFragmentToRegisterFragment()
            view.findNavController().navigate(toRegisterFragment)
        }

        binding.layoutUnauthorized.btnLogin.setOnClickListener {
            val toLoginFragment =
                ProfileFragmentDirections.actionProfileFragmentToLoginFragment()
            view.findNavController().navigate(toLoginFragment)
        }

        binding.imgEditProfile.setOnClickListener {
            val toEditProfileFragment =
                ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment()
            view.findNavController().navigate(toEditProfileFragment)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.apply {
                shimmerLayout.startShimmer()
                shimmerLayout.visibility = View.VISIBLE
                tvProfileName.visibility = View.INVISIBLE
                tvProfileEmail.visibility = View.INVISIBLE
                imgProfile.visibility = View.INVISIBLE
                imgEditProfile.visibility = View.INVISIBLE
            }
        } else {
            binding.apply {
                shimmerLayout.stopShimmer()
                shimmerLayout.visibility = View.INVISIBLE
                tvProfileName.visibility = View.VISIBLE
                tvProfileEmail.visibility = View.VISIBLE
                imgProfile.visibility = View.VISIBLE
                imgEditProfile.visibility = View.VISIBLE
            }
        }
    }

    private fun checkAuthorization() {
        if (isAuthorized) {
            viewModel.fetchProfile(accessToken!!)
            showProfile()
        } else {
            hideProfile()
        }
    }

    private fun hideProfile() {
        binding.apply {
            layoutUnauthorized.root.visibility = View.VISIBLE
            shimmerLayout.visibility = View.INVISIBLE
            imgProfile.visibility = View.INVISIBLE
            imgEditProfile.visibility = View.INVISIBLE
            tvProfileName.visibility = View.INVISIBLE
            tvProfileEmail.visibility = View.INVISIBLE
            tvProfileAccount.visibility = View.INVISIBLE
            rvSettingList.visibility = View.INVISIBLE
        }
    }

    private fun showProfile() {
        binding.apply {
            layoutUnauthorized.root.visibility = View.INVISIBLE
            imgProfile.visibility = View.VISIBLE
            tvProfileName.visibility = View.VISIBLE
            tvProfileEmail.visibility = View.VISIBLE
            tvProfileAccount.visibility = View.VISIBLE
            rvSettingList.visibility = View.VISIBLE
        }

        val settingAdapter = SettingAdapter(listSetting)
        binding.rvSettingList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = settingAdapter
        }

        settingAdapter.setOnItemClickCallback(object : SettingAdapter.OnItemClickCallback {
            override fun onItemClicked(data: SettingItem) {
                when (data.text) {
                    resources.getString(R.string.history) -> {
                        // Navigate to History Tab of Bottom Navigation View
                    }
                    resources.getString(R.string.my_reviews) -> {
                        // Navigate to List of Reviews Fragment
                    }
                    resources.getString(R.string.recommendations) -> {
                        // Navigate to List of Recommendations Fragment
                    }
                    resources.getString(R.string.change_password) -> {
                        val toChangePasswordFragment =
                            ProfileFragmentDirections.actionProfileFragmentToChangePasswordFragment()
                        requireView().findNavController().navigate(toChangePasswordFragment)
                    }
                    resources.getString(R.string.appearance) -> {
                        val toAppearanceFragment =
                            ProfileFragmentDirections.actionProfileFragmentToAppearanceFragment()
                        requireView().findNavController().navigate(toAppearanceFragment)
                    }
                    resources.getString(R.string.language) -> {
                        // Navigate to Change Language Fragment
                    }
                    resources.getString(R.string.about_cemil) -> {
                        // Navigate to About Cemil Fragment
                    }
                    resources.getString(R.string.logout) -> {
                        LogoutDialogFragment().let {
                            it.setDialogCallback(this@ProfileFragment)
                            it.show(parentFragmentManager, LogoutDialogFragment.TAG)
                        }
                    }
                }
            }
        })
    }

    override fun onDialogLogout() {
        hideProfile()
    }
}


