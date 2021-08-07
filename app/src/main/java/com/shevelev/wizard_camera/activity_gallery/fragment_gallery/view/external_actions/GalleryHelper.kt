package com.shevelev.wizard_camera.activity_gallery.fragment_gallery.view.external_actions

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment

interface GalleryHelper {
    fun startTakingPhoto(fragment: Fragment): Boolean

    fun processTakingPhotoResult(requestCode: Int, resultCode: Int, data: Intent?, successAction: (Uri) -> Unit): Boolean
}