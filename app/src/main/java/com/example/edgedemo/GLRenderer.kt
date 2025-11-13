package com.example.edgedemo

import android.graphics.SurfaceTexture
import android.opengl.GLES20
import java.nio.ByteBuffer
import java.nio.ByteOrder

class GLRenderer(surfaceTexture: SurfaceTexture) : Thread() {
    private var running = true
    private var textureId = -1
    private var width = 0
    private var height = 0
    private var frameBuffer: ByteBuffer? = null

    init {
        // Note: Full EGL context creation and shader compilation are left as an exercise.
        // This file provides the texture upload flow and scaffold.
    }

    fun updateFrame(rgbaBytes: ByteArray, w: Int, h: Int) {
        width = w; height = h
        frameBuffer = ByteBuffer.allocateDirect(rgbaBytes.size)
        frameBuffer!!.order(ByteOrder.nativeOrder())
        frameBuffer!!.put(rgbaBytes)
        frameBuffer!!.position(0)
    }

    override fun run() {
        while (running) {
            if (frameBuffer != null) {
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId)
                GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, frameBuffer)
                // draw full-screen quad (shaders omitted)
            }
            try { sleep(16) } catch (e: InterruptedException) {}
        }
    }

    fun stopRenderer() { running = false }
}
