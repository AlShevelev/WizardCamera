package com.shevelev.wizard_camera.activity_gallery.fragment_gallery.view.external_actions

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment
import com.shevelev.wizard_camera.R
import javax.inject.Inject

class SharingHelperImpl
@Inject
constructor() : ExternalActionHelperBase(), SharingHelper {

    override fun startSharing(contentUri: Uri, fragment: Fragment): Boolean {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, contentUri)
            type = "image/jpeg"
        }

        if(!checkIntent(shareIntent, fragment.requireContext())) {
            return false
        }

        fragment.startActivity(Intent.createChooser(
            shareIntent,
            fragment.requireContext().resources.getText(R.string.sendTo))
        )

        return true
    }
}