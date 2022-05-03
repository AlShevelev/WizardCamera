package com.shevelev.wizard_camera.core.photo_files.impl

import com.shevelev.wizard_camera.core.photo_files.api.FilesHelper
import java.io.File

internal class FilesHelperImpl : FilesHelper {
    override fun removeFile(fileToRemove: File) {
        fileToRemove.delete()
    }
}