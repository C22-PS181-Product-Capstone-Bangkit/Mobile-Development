package com.bangkit.cemil.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.bangkit.cemil.R
import com.bangkit.cemil.databinding.FragmentProfileBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfileFragment : Fragment() {

    private lateinit var binding : FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container ,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomNavigation : BottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView)
        binding.menuHistory.setOnClickListener {
            NavigationUI.onNavDestinationSelected(bottomNavigation.menu.findItem(R.id.historyFragment),it.findNavController())
        }
        binding.menuReviews.setOnClickListener {

        }
        binding.menuRecommendations.setOnClickListener {

        }
        binding.menuAppearances.setOnClickListener {

        }
        binding.menuLanguage.setOnClickListener {

        }
        binding.menuAbout.setOnClickListener {

        }
        binding.menuChangePass.setOnClickListener {

        }
        binding.menuLogout.setOnClickListener {

        }
    }
}