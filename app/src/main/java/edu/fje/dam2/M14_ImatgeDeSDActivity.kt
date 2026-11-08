package edu.fje.dam2

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import edu.fje.dam2.databinding.M14ImatgesSdBinding

/**
 * Activity que utilitza el Photo Picker de Jetpack (PickVisualMedia)
 * per a seleccionar imatges de la galeria de manera segura.
 *
 * @author sergi.grau@fje.edu
 * @version 7.0 (API 36) 2026
 */
class M14_ImatgeDeSDActivity : AppCompatActivity() {

    private lateinit var binding: M14ImatgesSdBinding

    private val pickVisualMediaLauncher: ActivityResultLauncher<PickVisualMediaRequest> =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            if (uri != null) {
                try {
                    contentResolver.openInputStream(uri)?.use { inputStream ->
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        binding.imageView1.setImageBitmap(bitmap)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = M14ImatgesSdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pickVisualMediaLauncher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }
}
