package com.example.edgedemo

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.SurfaceTexture
import android.os.Bundle
import android.view.TextureView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity(), TextureView.SurfaceTextureListener {
    companion object {
        init { System.loadLibrary("edge_native") }
    }

    external fun nativeProcessFrame(nv21: ByteArray, width: Int, height: Int, low: Int, high: Int): ByteArray

    private lateinit var textureView: TextureView
    private lateinit var renderer: GLRenderer
    private val REQUEST_CAMERA = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textureView = findViewById(R.id.textureView)
        textureView.surfaceTextureListener = this

        val btnToggle: Button = findViewById(R.id.btnToggle)
        btnToggle.setOnClickListener {
            // placeholder for toggle
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA)
        }
    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
        renderer = GLRenderer(surface)
        renderer.start()

        val cameraSource = Camera2Source(this)
        cameraSource.start(width, height) { nv21Bytes, w, h ->
            val processed = nativeProcessFrame(nv21Bytes, w, h, 50, 150)
            renderer.updateFrame(processed, w, h)
        }
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {}
    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean { renderer.stopRenderer(); return true }
    override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {}
}
