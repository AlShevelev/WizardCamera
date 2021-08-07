package com.shevelev.wizard_camera.activity_gallery.fragment_gallery.view.external_actions

import android.net.Uri
import androidx.fragment.app.Fragment

interface SharingHelper {
    fun startSharing(contentUri: Uri, fragment: Fragment): Boolean
}