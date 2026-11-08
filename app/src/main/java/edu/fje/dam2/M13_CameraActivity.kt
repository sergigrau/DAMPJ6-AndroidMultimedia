package edu.fje.dam2

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import edu.fje.dam2.databinding.M12CameraBinding
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Activity que utilitza la llibreria Jetpack CameraX per accedir a la càmera,
 * mostrar la previsualització i capturar fotografies de forma lifecycle-aware.
 *
 * @author sergi.grau@fje.edu
 * @version 7.0 (API 36) 2026
 */
class M13_CameraActivity : AppCompatActivity() {

    private lateinit var binding: M12CameraBinding
    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                iniciarCameraX()
            } else {
                Toast.makeText(this, "Permisos de càmera denegats", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = M12CameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cameraExecutor = Executors.newSingleThreadExecutor()

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED
        ) {
            iniciarCameraX()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }

        binding.btnTakepicture.setOnClickListener { prendreFoto() }
    }

    private fun iniciarCameraX() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.surfaceProvider = binding.previewView.surfaceProvider
            }

            imageCapture = ImageCapture.Builder().build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )
            } catch (e: Exception) {
                Log.e("M13_CameraX", "Error vinculant CameraX: ${e.message}", e)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun prendreFoto() {
        val imageCapture = imageCapture ?: return
        val photoFile = File(getExternalFilesDir(null), "camerax_pic.jpg")
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Toast.makeText(this@M13_CameraActivity, "Error capturant: ${exc.message}", Toast.LENGTH_SHORT).show()
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    Toast.makeText(this@M13_CameraActivity, "Foto desada amb CameraX: ${photoFile.absolutePath}", Toast.LENGTH_LONG).show()
                }
            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}
