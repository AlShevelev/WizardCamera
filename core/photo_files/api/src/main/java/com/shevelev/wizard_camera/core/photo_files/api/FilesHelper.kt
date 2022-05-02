package com.shevelev.wizard_camera.core.photo_files.api

import java.io.File

interface FilesHelper {
    fun getShotFileByName(fileName: String): File

    fun removeShotFileByName(fileName: String): File

    fun removeFile(fileToRemove: File)
}