package com.bangkit.cemil.profile

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.bangkit.cemil.databinding.FragmentLogoutDialogBinding

class LogoutDialogFragment : DialogFragment() {

    private lateinit var binding : FragmentLogoutDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentLogoutDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvPositive.setOnClickListener {
            //Proceed to logout
            dismiss()
        }
        binding.tvNegative.setOnClickListener {
            dismiss()
        }
    }

    companion object{
        const val TAG = "LogoutDialog"
    }

}