package com.shevelev.wizard_camera.filter

import android.content.Context
import android.opengl.GLES31
import com.shevelev.wizard_camera.camera.R
import com.shevelev.wizard_camera.common_entities.filter_settings.FilterSettings
import com.shevelev.wizard_camera.common_entities.filter_settings.SwirlFilterSettings

class SwirlCameraFilter(context: Context) : CameraFilter(context, R.raw.swirl) {
    /**
     * Pass filter-specific arguments
     */
    override fun passSettingsParams(program: Int, settings: FilterSettings) {
        settings as SwirlFilterSettings

        val rotation = GLES31.glGetUniformLocation(program, "rotation")
        var rotationValue = 11f - settings.rotation.toFloat()
        if(settings.invertRotation) {
            rotationValue = 1f / rotationValue
        }
        GLES31.glUniform1f(rotation, rotationValue)

        val radius = GLES31.glGetUniformLocation(program, "radiusRatio")
        GLES31.glUniform1f(radius, settings.radius.toFloat() / 10f)
    }
}