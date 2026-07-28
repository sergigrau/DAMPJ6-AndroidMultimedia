package edu.fje.dam2

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

/**
 * Activity que permet l'accés a l'aplicació estàndard de la càmera de fotos.
 *
 * @author sergi.grau@fje.edu
 * @version 6.0 (API 33)
 */
class M12_CameraIntentActivity : AppCompatActivity() {

    private lateinit var imatge: ImageView

    private val capturaImatgeLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val extras = result.data!!.extras
                val imageBitmap = extras?.getParcelable("data", Bitmap::class.java)
                imatge.setImageBitmap(imageBitmap)
            }
        }

    private val requestCameraPermissionLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) fesFoto()
            else Toast.makeText(this, "Permís de càmera denegat", Toast.LENGTH_SHORT).show()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.m13_camera_intent)

        val boto: Button = findViewById(R.id.botoFoto)
        imatge = findViewById(R.id.imageView1)

        boto.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
            ) {
                requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                return@setOnClickListener
            }
            fesFoto()
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun fesFoto() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            capturaImatgeLauncher.launch(takePictureIntent)
        }
    }
}

