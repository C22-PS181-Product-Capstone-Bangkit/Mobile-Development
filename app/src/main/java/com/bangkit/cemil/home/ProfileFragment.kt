package com.bangkit.cemil.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.cemil.R
import com.bangkit.cemil.databinding.FragmentProfileBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfileFragment : Fragment() {

    private lateinit var binding : FragmentProfileBinding

    private val listSetting: ArrayList<SettingItem>
        get() {
            val tempList = ArrayList<SettingItem>()
            val settingNames = resources.getStringArray(R.array.setting_names)
            val settingDrawableIds = resources.obtainTypedArray(R.array.setting_icons)

            for (index in settingNames.indices) {
                val settingItem = SettingItem(settingNames[index], ResourcesCompat.getDrawable(resources, settingDrawableIds.getResourceId(index, -1), null))
                tempList.add(settingItem)
            }
            settingDrawableIds.recycle()
            return tempList
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container ,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(binding.materialToolbar)
        (activity as AppCompatActivity).supportActionBar?.title = null
        (activity as AppCompatActivity).findViewById<BottomNavigationView>(R.id.bottomNavigationView).visibility = View.VISIBLE

        checkAuthorization()

        binding.layoutUnauthorized.btnCreateAccount.setOnClickListener {
            val toRegisterFragment = ProfileFragmentDirections.actionProfileFragmentToRegisterFragment()
            view.findNavController().navigate(toRegisterFragment)
        }

        binding.layoutUnauthorized.btnLogin.setOnClickListener {
            val toLoginFragment = ProfileFragmentDirections.actionProfileFragmentToLoginFragment()
            view.findNavController().navigate(toLoginFragment)
        }

    }

    private fun checkAuthorization() {
        if (isAuthorized) {
            showProfile()
        } else {
            hideProfile()
        }
    }

    private fun hideProfile() {
        binding.apply {
            layoutUnauthorized.root.visibility = View.VISIBLE
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
            imgEditProfile.visibility = View.VISIBLE
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

        settingAdapter.setOnItemClickCallback(object : SettingAdapter.OnItemClickCallback{
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
                        val toChangePasswordFragment = ProfileFragmentDirections.actionProfileFragmentToChangePasswordFragment()
                        requireView().findNavController().navigate(toChangePasswordFragment)
                    }
                    resources.getString(R.string.appearance) -> {
                        // Navigate to Change Appearance Fragment
                    }
                    resources.getString(R.string.language) -> {
                        // Navigate to Change Language Fragment
                    }
                    resources.getString(R.string.about_cemil) -> {
                        // Navigate to About Cemil Fragment
                    }
                    resources.getString(R.string.logout) -> {
                        // Show Alert Dialog
                    }
                }
            }
        })
    }

    companion object {
        private const val isAuthorized: Boolean = true
    }
}