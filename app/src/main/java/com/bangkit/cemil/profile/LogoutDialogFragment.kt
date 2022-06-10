package com.bangkit.cemil.profile

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.bangkit.cemil.SettingPreferences
import com.bangkit.cemil.dataStore
import com.bangkit.cemil.databinding.FragmentLogoutDialogBinding
import kotlinx.coroutines.launch

class LogoutDialogFragment : DialogFragment() {

    private lateinit var binding : FragmentLogoutDialogBinding
    private lateinit var dialogCallback : DialogCallback

    fun setDialogCallback(dialogCallback: DialogCallback){
        this.dialogCallback = dialogCallback
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
        val pref = SettingPreferences.getInstance(requireContext().dataStore)

        binding.tvPositive.setOnClickListener {
            Toast.makeText(context, "Logout successful", Toast.LENGTH_SHORT).show()
            lifecycleScope.launch{
                pref.saveAuthorization(false)
                pref.deleteAuthorizationToken()
                dialogCallback.onDialogLogout()
                dismiss()
            }
        }

        binding.tvNegative.setOnClickListener {
            dismiss()
        }
    }

    companion object{
        const val TAG = "LogoutDialog"
    }
    interface DialogCallback{
        fun onDialogLogout()
    }
}
