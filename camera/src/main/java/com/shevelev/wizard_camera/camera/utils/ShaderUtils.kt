package com.shevelev.wizard_camera.camera.utils

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLException
import androidx.annotation.RawRes
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.IOException

object ShaderUtils {
    fun buildProgram(context: Context, @RawRes vertexSourceRawId: Int, @RawRes fragmentSourceRawId: Int): Int =
        buildProgram(getStringFromRaw(context, vertexSourceRawId), getStringFromRaw(context, fragmentSourceRawId))

    private fun buildProgram(vertexSource: String, fragmentSource: String): Int {
        val vertexShader = buildShader(GLES20.GL_VERTEX_SHADER, vertexSource);
        val fragmentShader = buildShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource);

        val program = GLES20.glCreateProgram();
        if (program == 0) {
            throw GLException(0, "Can't create a program!")
        }

        GLES20.glAttachShader(program, vertexShader);
        GLES20.glAttachShader(program, fragmentShader);
        GLES20.glLinkProgram(program);

        return program;
    }

    private fun buildShader(type: Int, shaderSource: String): Int {
        val shader = GLES20.glCreateShader(type)

        if(shader == 0) {
            throw GLException(0, "Can't create shader (type is: $type)!")
        }

        GLES20.glShaderSource(shader, shaderSource)
        GLES20.glCompileShader(shader)

        val status = IntArray(1)
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, status, 0)
        if (status[0] == 0) {
            Timber.tag("SHADER_ERROR").e(GLES20.glGetShaderInfoLog(shader))
            GLES20.glDeleteShader(shader)
            
            throw GLException(0, "Can't create shader (type is: $type)!")
        }

        return shader
    }

    private fun getStringFromRaw(context: Context, @RawRes resId: Int): String =
        try {
            context.resources.openRawResource(resId).use { inputStream ->
                ByteArrayOutputStream().use { outputStream ->
                    var i = inputStream.read()
                    while (i != -1) {
                        outputStream.write(i)
                        i = inputStream.read()
                    }

                    outputStream.toString()
                }
            }
        } catch (ex: IOException) {
            Timber.e(ex)
            throw ex
        }
}