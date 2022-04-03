package com.example.covid_tracker.utils

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.example.covid_tracker.R
import com.example.covid_tracker.databinding.AlertDialogBinding

class DialogCreator (
    private var dialogTitle: Int,
    private var dialogMessage: Int,
    private var dialogImgId: Int? = null,
    private var dialogPositiveBtnCallback: (() -> Unit)? = null
): DialogFragment(), DialogInterface {

    private val TAG = "DialogCreator"
    private var _binding: AlertDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = AlertDialogBinding.inflate(LayoutInflater.from(context))

        binding.tvDialogTitle.text = requireActivity().getString(dialogTitle)
        binding.tvDialogMessage.text = requireActivity().getString(dialogMessage)
        binding.ivDialogImage.setImageResource(dialogImgId ?: R.drawable.dialog_error_appeared)
        binding.btnDialogPositive.setOnClickListener {
            dialogPositiveBtnCallback?.let { callback -> callback() } ?: dismiss()
        }

        val alertDialogBuilder = AlertDialog.Builder(requireActivity())
        alertDialogBuilder
            .setCancelable(false)
            .setView(binding.root)

        return alertDialogBuilder.create()
    }

    override fun cancel() {
        dismiss()
    }

    fun showDialog(fragmentActivity: FragmentActivity) {
        val fragmentManager = fragmentActivity.supportFragmentManager
        show(fragmentManager, null)
    }

}