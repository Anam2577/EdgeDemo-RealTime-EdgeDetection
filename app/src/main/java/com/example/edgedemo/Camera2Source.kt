package com.example.edgedemo

import android.app.Activity
import android.content.Context
import android.graphics.ImageFormat
import android.media.Image
import android.media.ImageReader
import android.hardware.camera2.*

class Camera2Source(private val activity: Activity) {
    private var cameraDevice: CameraDevice? = null
    private var captureSession: CameraCaptureSession? = null
    private lateinit var reader: ImageReader

    fun start(targetW: Int, targetH: Int, onFrame: (ByteArray, Int, Int) -> Unit) {
        val manager = activity.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val cameraId = manager.cameraIdList[0]
        reader = ImageReader.newInstance(targetW, targetH, ImageFormat.YUV_420_888, 2)
        reader.setOnImageAvailableListener({ r ->
            val img = r.acquireLatestImage() ?: return@setOnImageAvailableListener
            val nv21 = yuv420ToNV21(img)
            onFrame(nv21, img.width, img.height)
            img.close()
        }, null)

        manager.openCamera(cameraId, object : CameraDevice.StateCallback() {
            override fun onOpened(camera: CameraDevice) {
                cameraDevice = camera
                val surface = reader.surface
                val req = camera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                req.addTarget(surface)
                camera.createCaptureSession(listOf(surface), object : CameraCaptureSession.StateCallback() {
                    override fun onConfigured(session: CameraCaptureSession) {
                        captureSession = session
                        session.setRepeatingRequest(req.build(), null, null)
                    }
                    override fun onConfigureFailed(session: CameraCaptureSession) {}
                }, null)
            }
            override fun onDisconnected(camera: CameraDevice) {}
            override fun onError(camera: CameraDevice, error: Int) {}
        }, null)
    }

    private fun yuv420ToNV21(image: Image): ByteArray {
        val width = image.width
        val height = image.height
        val ySize = width * height
        val uvSize = width * height / 4
        val nv21 = ByteArray(ySize + uvSize * 2)

        val yBuffer = image.planes[0].buffer
        val uBuffer = image.planes[1].buffer
        val vBuffer = image.planes[2].buffer

        yBuffer.get(nv21, 0, ySize)

        val rowStride = image.planes[2].rowStride
        val pixelStride = image.planes[2].pixelStride

        var pos = ySize
        val v = ByteArray(vBuffer.remaining())
        val u = ByteArray(uBuffer.remaining())
        vBuffer.get(v)
        uBuffer.get(u)

        for (i in 0 until v.size step pixelStride) {
            nv21[pos++] = v[i]
            nv21[pos++] = u[i]
        }
        return nv21
    }
}
