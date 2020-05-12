package com.shevelev.wizard_camera.camera

import android.opengl.GLES31
import com.shevelev.wizard_camera.camera.utils.TextureUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.microedition.khronos.opengles.GL10

class RenderBuffer(val width: Int, val height: Int, activeTexUnit: Int) {
    val texId: Int

    private val frameBufferId: Int

    init {
        // Generate and bind 2d texture
        GLES31.glActiveTexture(activeTexUnit)
        texId = TextureUtils.createTexture(GLES31.GL_TEXTURE_2D)
         val texBuffer = ByteBuffer.allocateDirect(width * height * 4).order(ByteOrder.nativeOrder()).asIntBuffer()
        GLES31.glTexImage2D(GLES31.GL_TEXTURE_2D, 0, GLES31.GL_RGBA, width, height, 0, GLES31.GL_RGBA, GLES31.GL_UNSIGNED_BYTE, texBuffer)

        GLES31.glTexParameterf(GLES31.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR.toFloat())
        GLES31.glTexParameterf(GLES31.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR.toFloat())
        GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE)
        GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE)

        // Generate frame buffer
        val generatedFrameBufId = IntArray(1)
        GLES31.glGenFramebuffers(1, generatedFrameBufId, 0)
        frameBufferId = generatedFrameBufId[0]
        // Bind frame buffer
        GLES31.glBindFramebuffer(GLES31.GL_FRAMEBUFFER, frameBufferId)

        unbind()
    }

    fun bind() {
        // Set a texture to full screen
        GLES31.glViewport(0, 0, width, height)

        // Add framebuffer to a current texture slot
        GLES31.glBindFramebuffer(GLES31.GL_FRAMEBUFFER, frameBufferId)

        // Link a texture and the framebuffer
        GLES31.glFramebufferTexture2D(GLES31.GL_FRAMEBUFFER, GLES31.GL_COLOR_ATTACHMENT0, GLES31.GL_TEXTURE_2D, texId, 0)
    }

    fun unbind() = GLES31.glBindFramebuffer(GLES31.GL_FRAMEBUFFER, 0)
}