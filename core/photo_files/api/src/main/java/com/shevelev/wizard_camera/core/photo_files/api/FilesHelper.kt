package com.shevelev.wizard_camera.core.photo_files.api

import java.io.File

interface FilesHelper {
    fun createFileForShot(): File

    fun createTempFileForShot(): File

    fun getShotFileByName(fileName: String): File

    fun removeShotFileByName(fileName: String): File

    fun copyToTempFile(source: File): File

    fun removeFile(fileToRemove: File)
}