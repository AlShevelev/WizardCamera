package com.shevelev.wizard_camera.core.camera_gl.impl.camera.renderer.utils

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

internal object BufferUtils {
    /**
     * Create a buffer for and put the coordinates to it
     */
    fun createBuffer(vararg coordinates: Float): FloatBuffer =
        ByteBuffer.allocateDirect(coordinates.size * Float.SIZE_BYTES)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .apply {
                put(coordinates)
                position(0)
            }
}