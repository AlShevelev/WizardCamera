package com.shevelev.wizard_camera.shared.files

import android.content.Context
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.utils.id.IdUtil
import java.io.File
import javax.inject.Inject
import kotlin.math.absoluteValue

class FilesHelperImpl
@Inject
constructor(
    private val appContext: Context
) : FilesHelper {
    override fun createFileForShot(): File = createFileForShot(getShotsDirectory())

    override fun getShotFileByName(fileName: String): File = File(getShotsDirectory(), fileName)

    override fun removeShotFileByName(fileName: String) = getShotFileByName(fileName).apply { delete() }

    override fun copyToTempFile(source: File): File {
        val destinationFile = createFileForShot(appContext.cacheDir)
        source.copyTo(destinationFile, overwrite = true)
        return destinationFile
    }

    override fun removeFIle(fileToRemove: File) {
        fileToRemove.delete()
    }

    private fun getShotsDirectory(): File {
        val dir = File(appContext.externalMediaDirs[0], appContext.getString(R.string.appName))
        if(!dir.exists()) {
            dir.mkdir()
        }

        return dir
    }

    private fun createFileForShot(dir: File): File = File(dir, "${IdUtil.generateLongId().absoluteValue}.jpg")
}