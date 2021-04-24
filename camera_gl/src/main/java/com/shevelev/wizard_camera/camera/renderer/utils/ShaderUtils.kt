package com.shevelev.wizard_camera.camera.renderer.utils

import android.content.Context
import android.opengl.GLES31
import android.opengl.GLException
import androidx.annotation.RawRes
import com.shevelev.wizard_camera.utils.resources.getRawString
import timber.log.Timber

object ShaderUtils {
    fun buildProgram(context: Context, @RawRes vertexSourceRawId: Int, @RawRes fragmentSourceRawId: Int): Int =
        buildProgram(context.getRawString(vertexSourceRawId), context.getRawString(fragmentSourceRawId))

    private fun buildProgram(vertexSource: String, fragmentSource: String): Int {
        val vertexShader = buildShader(GLES31.GL_VERTEX_SHADER, vertexSource)
        val fragmentShader = buildShader(GLES31.GL_FRAGMENT_SHADER, fragmentSource)

        val program = GLES31.glCreateProgram()
        if (program == 0) {
            throw GLException(0, "Can't create a program!")
        }

        GLES31.glAttachShader(program, vertexShader)
        GLES31.glAttachShader(program, fragmentShader)
        GLES31.glLinkProgram(program)

        return program
    }

    private fun buildShader(type: Int, shaderSource: String): Int {
        val shader = GLES31.glCreateShader(type)

        if(shader == 0) {
            throw GLException(0, "Can't create shader (type is: $type)!")
        }

        GLES31.glShaderSource(shader, shaderSource)
        GLES31.glCompileShader(shader)

        val status = IntArray(1)
        GLES31.glGetShaderiv(shader, GLES31.GL_COMPILE_STATUS, status, 0)
        if (status[0] == 0) {
            Timber.tag("SHADER_ERROR").e(GLES31.glGetShaderInfoLog(shader))
            GLES31.glDeleteShader(shader)
            
            throw GLException(0, "Can't create shader (type is: $type)!")
        }

        return shader
    }
}