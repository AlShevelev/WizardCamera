package com.shevelev.wizard_camera.shared.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.shevelev.wizard_camera.shared.mvvm.view.FragmentBase

class ConfirmationDialog : DialogFragment() {
    companion object {
        private const val ARG_REQUEST_CODE = "ARG_REQUEST_CODE"
        private const val ARG_TEXT = "ARG_TEXT"
        private const val ARG_POSITIVE_BUTTON = "ARG_POSITIVE_BUTTON"
        private const val ARG_NEGATIVE_BUTTON = "ARG_NEGATIVE_BUTTON"

        fun show(
            requestCode: Int,
            fragment: FragmentBase<*>,
            @StringRes text: Int,
            @StringRes  positiveButton: Int,
            @StringRes negativeButton: Int) {

            ConfirmationDialog().apply {
                arguments = Bundle().apply {
                    putInt(ARG_REQUEST_CODE, requestCode)
                    putInt(ARG_TEXT, text)
                    putInt(ARG_POSITIVE_BUTTON, positiveButton)
                    putInt(ARG_NEGATIVE_BUTTON, negativeButton)
                }
                show(fragment.childFragmentManager, null)
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val requestCode = requireArguments().getInt(ARG_REQUEST_CODE)

        return activity?.let {
            AlertDialog.Builder(it)
                .setMessage(requireArguments().getInt(ARG_TEXT))
                .setPositiveButton(requireArguments().getInt(ARG_POSITIVE_BUTTON)) { _, _ ->
                    (parentFragment as FragmentBase<*>).onDialogResult(false, requestCode, null)
                }
                .setNegativeButton(requireArguments().getInt(ARG_NEGATIVE_BUTTON)) { _, _ ->
                    (parentFragment as FragmentBase<*>).onDialogResult(true, requestCode, null)
                }
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onCancel(dialog: DialogInterface) {
        (parentFragment as FragmentBase<*>).onDialogResult(true, requireArguments().getInt(ARG_REQUEST_CODE), null)
    }
}