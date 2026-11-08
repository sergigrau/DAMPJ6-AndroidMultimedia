package edu.fje.dam2

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import edu.fje.dam2.databinding.M13CameraIntentBinding

/**
 * Activity que permet l'accés a la càmera de fotos utilitzant
 * Jetpack Activity Result API (TakePicturePreview).
 *
 * @author sergi.grau@fje.edu
 * @version 7.0 (API 36) 2026
 */
class M12_CameraIntentActivity : AppCompatActivity() {

    private lateinit var binding: M13CameraIntentBinding

    private val takePictureLauncher: ActivityResultLauncher<Void?> =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap: Bitmap? ->
            if (bitmap != null) {
                binding.imageView1.setImageBitmap(bitmap)
            }
        }

    private val requestCameraPermissionLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) takePictureLauncher.launch(null)
            else Toast.makeText(this, "Permís de càmera denegat", Toast.LENGTH_SHORT).show()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = M13CameraIntentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.botoFoto.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
            ) {
                requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            } else {
                takePictureLauncher.launch(null)
            }
        }
    }
}
