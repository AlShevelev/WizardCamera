package com.shevelev.wizard_camera.shared.files

import com.shevelev.wizard_camera.common_entities.enums.FilterCode
import java.io.File

interface FilesHelper {
    fun createFileForShot(activeFilter: FilterCode): File

    fun getShotFileByName(fileName: String): File

    fun removeShotFileByName(fileName: String): File
}