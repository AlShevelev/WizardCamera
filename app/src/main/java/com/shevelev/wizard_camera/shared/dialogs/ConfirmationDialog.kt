package com.shevelev.wizard_camera.shared.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

class ConfirmationDialog(
    @StringRes private val text: Int,
    @StringRes private val positiveButton: Int,
    @StringRes private val negativeButton: Int,
    private val resultAction: (Boolean) -> Unit
) : DialogFragment() {

    companion object {
        fun show(
            fragmentManager: FragmentManager,
            @StringRes text: Int,
            @StringRes  positiveButton: Int,
            @StringRes negativeButton: Int,
            resultAction: (Boolean) -> Unit) {
            ConfirmationDialog(text, positiveButton, negativeButton, resultAction).show(fragmentManager, null)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            AlertDialog.Builder(it)
                .setMessage(text)
                .setPositiveButton(positiveButton) { _, _ -> resultAction(true) }
                .setNegativeButton(negativeButton) { _, _ -> resultAction(false) }
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}