package com.shevelev.wizard_camera.filter

import android.content.Context
import android.opengl.GLES31
import com.shevelev.wizard_camera.camera.R
import com.shevelev.wizard_camera.common_entities.filter_settings.CrackedFilterSettings
import com.shevelev.wizard_camera.common_entities.filter_settings.FilterSettings

class CrackedCameraFilter(context: Context) : CameraFilter(context, R.raw.cracked) {
    /**
     * Pass filter-specific arguments
     */
    override fun passSettingsParams(program: Int, settings: FilterSettings) {
        settings as CrackedFilterSettings

        val shardsCount = GLES31.glGetUniformLocation(program, "shardsCount")
        GLES31.glUniform1i(shardsCount, settings.shards)

        val randomA = GLES31.glGetUniformLocation(program, "randomA")
        GLES31.glUniform1f(randomA, settings.randomA)

        val randomB = GLES31.glGetUniformLocation(program, "randomB")
        GLES31.glUniform1f(randomB, settings.randomB)

        val randomC = GLES31.glGetUniformLocation(program, "randomC")
        GLES31.glUniform1f(randomC, settings.randomC)
    }
}