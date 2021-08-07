package com.shevelev.wizard_camera.activity_gallery.fragment_gallery.view.external_actions

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import javax.inject.Inject

class GalleryHelperImpl
@Inject
constructor() : ExternalActionHelperBase(), GalleryHelper {

    override fun startTakingPhoto(fragment: Fragment): Boolean {
        val takePictureIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        if(!checkIntent(takePictureIntent, fragment.requireContext())) {
            return false
        }

        fragment.startActivityForResult(takePictureIntent, REQUEST)
        return true
    }

    override fun processTakingPhotoResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        successAction: (Uri) -> Unit): Boolean {

        if(resultCode != Activity.RESULT_OK || requestCode != REQUEST) {
            return false
        }

        if(data == null) {
            return false
        }

        successAction(data.data as Uri)
        return true
    }

    companion object {
        private const val REQUEST = 24894
    }
}