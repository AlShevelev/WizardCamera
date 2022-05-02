package com.shevelev.wizard_camera.core.photo_files.impl

import android.content.Context
import com.shevelev.wizard_camera.core.photo_files.api.FilesHelper
import java.io.File

internal class FilesHelperImpl
constructor(
    private val appContext: Context,
    private val appName: String
) : FilesHelper {
    override fun getShotFileByName(fileName: String): File = File(getShotsDirectory(), fileName)

    override fun removeShotFileByName(fileName: String) = getShotFileByName(fileName).apply { delete() }

    override fun removeFile(fileToRemove: File) {
        fileToRemove.delete()
    }

    private fun getShotsDirectory(): File {
        val dir = File(appContext.externalMediaDirs[0], appName)
        if(!dir.exists()) {
            dir.mkdir()
        }

        return dir
    }
}