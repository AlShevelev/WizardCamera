package com.shevelev.wizard_camera.shared.files

import java.io.File

interface FilesHelper {
    fun createFileForShot(): File

    fun getShotFileByName(fileName: String): File

    fun removeShotFileByName(fileName: String): File

    fun copyToTempFile(source: File): File

    fun removeFIle(fileToRemove: File)
}