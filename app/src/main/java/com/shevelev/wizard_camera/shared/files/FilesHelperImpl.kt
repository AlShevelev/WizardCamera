package com.shevelev.wizard_camera.shared.files

import android.content.Context
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.common_entities.enums.FilterCode
import com.shevelev.wizard_camera.utils.id.IdUtil
import java.io.File
import java.util.*
import javax.inject.Inject
import kotlin.math.absoluteValue

class FilesHelperImpl
@Inject
constructor(
    private val appContext: Context
) : FilesHelper {
    override fun createFileForShot(activeFilter: FilterCode): File {
        val prefix = activeFilter.toString().toLowerCase(Locale.getDefault())
        val suffix = IdUtil.generateLongId().absoluteValue
        val dir = getShotsDirectory()

        return File(dir, "$prefix$suffix.jpg")
    }

    override fun getShotFileByName(fileName: String): File = File(getShotsDirectory(), fileName)

    private fun getShotsDirectory(): File {
        val dir = File(appContext.externalMediaDirs[0], appContext.getString(R.string.appName))
        if(!dir.exists()) {
            dir.mkdir()
        }

        return dir
    }
}