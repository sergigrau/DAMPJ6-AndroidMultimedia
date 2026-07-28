package edu.fje.dam2

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

/**
 * Activity que permet seleccionar una imatge de la galeria d'imatges del dispositiu.
 *
 * @author sergi.grau@fje.edu
 * @version 6.0 (API 33) 19.04.2026
 */
class M14_ImatgeDeSDActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView

    private val seleccioImatgeLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val seleccio: Uri? = result.data!!.data
                try {
                    val inputStream = seleccio?.let { contentResolver.openInputStream(it) }
                    val imatge = BitmapFactory.decodeStream(inputStream)
                    imageView.setImageBitmap(imatge)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.m14_imatges_sd)

        imageView = findViewById(R.id.imageView1)
        val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        seleccioImatgeLauncher.launch(i)
    }
}

