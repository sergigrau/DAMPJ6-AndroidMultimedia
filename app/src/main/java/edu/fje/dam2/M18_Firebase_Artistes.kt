package edu.fje.dam2

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import edu.fje.dam2.databinding.M18FirebaseBinding
import kotlinx.coroutines.launch

/**
 * Activity que utilitza l'arquitectura MVVM amb Jetpack ViewModel i StateFlow
 * per a la gestió d'artistes a Firebase Realtime Database.
 *
 * @author sergi.grau@fje.edu
 * @version 7.0 (API 36) 2026
 */
class M18_Firebase_Artistes : AppCompatActivity() {

    companion object {
        const val NOM = "dam2.fje.edu.nom"
        const val ID = "dam2.fje.edu.id"
    }

    private lateinit var binding: M18FirebaseBinding
    private val viewModel: M18_ArtistesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = M18FirebaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.afegirArtistes.setOnClickListener { afegirArtista() }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.artistes.collect { llista ->
                    binding.llistaArtistes.adapter = M18_Llista(this@M18_Firebase_Artistes, llista.toMutableList())

                    binding.llistaArtistes.setOnItemClickListener { _, _, i, _ ->
                        if (i in llista.indices) {
                            val artist = llista[i]
                            val intent = Intent(applicationContext, M18_Firebase_Artistes::class.java).apply {
                                putExtra(ID, artist.id)
                                putExtra(NOM, artist.nom)
                            }
                            startActivity(intent)
                        }
                    }
                }
            }
        }
    }

    private fun afegirArtista() {
        val nomText = binding.editNom.text.toString().trim()
        val genereText = binding.generes.selectedItem.toString()
        if (!TextUtils.isEmpty(nomText)) {
            viewModel.afegirArtista(nomText, genereText)
            binding.editNom.setText("")
            Toast.makeText(this, "Artista afegit amb MVVM i StateFlow", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Cal entrar un nom", Toast.LENGTH_LONG).show()
        }
    }
}
