package com.shevelev.wizard_camera.shared.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.shevelev.wizard_camera.R

class OkDialog(
    @StringRes private val text: Int,
    private val resultAction: () -> Unit
) : DialogFragment() {

    companion object {
        fun show(
            fragmentManager: FragmentManager,
            @StringRes text: Int,
            resultAction: () -> Unit) {
            OkDialog(text, resultAction).show(fragmentManager, null)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            AlertDialog.Builder(it)
                .setMessage(text)
                .setPositiveButton(R.string.ok) { _, _ -> resultAction() }
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}