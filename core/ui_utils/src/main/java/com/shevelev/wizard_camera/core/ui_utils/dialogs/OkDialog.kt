package com.shevelev.wizard_camera.core.ui_utils.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.fragment.app.DialogFragment
import com.shevelev.wizard_camera.core.ui_utils.R
import com.shevelev.wizard_camera.core.ui_utils.mvvm.view.FragmentBase

class OkDialog : DialogFragment() {
    companion object {
        private const val ARG_REQUEST_CODE = "ARG_REQUEST_CODE"
        private const val ARG_TEXT = "ARG_TEXT"

        fun show(
            requestCode: Int,
            fragment: FragmentBase<*>,
            @StringRes text: Int) = show(requestCode, fragment, fragment.requireContext().getString(text))

        fun show(
            requestCode: Int,
            fragment: FragmentBase<*>,
            text: String) {
            OkDialog().apply {
                arguments = Bundle().apply {
                    putInt(ARG_REQUEST_CODE, requestCode)
                    putString(ARG_TEXT, text)
                }
                show(fragment.childFragmentManager, null)
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            AlertDialog.Builder(it)
                .setMessage(requireArguments().getString(ARG_TEXT))
                .setCancelable(true)
                .setPositiveButton(R.string.ok) { _, _ ->
                    (parentFragment as FragmentBase<*>).onDialogResult(false, requireArguments().getInt(ARG_REQUEST_CODE), null)
                }
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onCancel(dialog: DialogInterface) {
        (parentFragment as FragmentBase<*>).onDialogResult(true, requireArguments().getInt(ARG_REQUEST_CODE), null)
    }
}