package com.shevelev.wizard_camera.core.photo_files.impl

import android.content.Context
import com.shevelev.wizard_camera.core.photo_files.api.FilesHelper
import com.shevelev.wizard_camera.core.utils.id.IdUtil
import java.io.File
import kotlin.math.absoluteValue

internal class FilesHelperImpl
constructor(
    private val appContext: Context,
    private val appName: String
) : FilesHelper {
    // return Uri via File - see GalleryFragmentInteractorImpl line 114
    // remove this call from GalleryFragmentInteractorImpl
    override fun createTempFileForShot(): File = createFileForShot(appContext.cacheDir)

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

    private fun createFileForShot(dir: File): File = File(dir, "${IdUtil.generateLongId().absoluteValue}.jpg")
}