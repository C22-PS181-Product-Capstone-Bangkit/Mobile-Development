package com.bangkit.cemil.authentication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.bangkit.cemil.R
import com.google.android.material.bottomnavigation.BottomNavigationView


class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNav = (activity as AppCompatActivity).findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.visibility = View.GONE
    }

}