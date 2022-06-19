package com.shevelev.wizard_camera.activity_gallery.fragment_gallery.view_model

import android.net.Uri
import androidx.annotation.StringRes
import com.shevelev.wizard_camera.core.common_entities.entities.PhotoShot

internal sealed class GalleryFragmentCommand {
    data class ShareShot(
        val contentUri: Uri
    ) : GalleryFragmentCommand()

    data class EditShot(
        val shot: PhotoShot
    ) : GalleryFragmentCommand()

    data class ShowMessageRes(
        @StringRes val textResId: Int,
        val isError: Boolean = true
    ) : GalleryFragmentCommand()

    data class ScrollGalleryToPosition(
        val position: Int
    ) : GalleryFragmentCommand()
}