package edu.fje.dam2

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import edu.fje.dam2.databinding.M06CanvasPersonalitzatPrincipalBinding

/**
 * Classe que demostra el funcionament de Canvas amb Vistes personalitzades
 * i ViewBinding de Jetpack.
 *
 * @author sergi.grau@fje.edu
 * @version 7.0 (API 36) 2026
 */
class M06_CanvasPersonalitzat : AppCompatActivity() {

    private lateinit var binding: M06CanvasPersonalitzatPrincipalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = M06CanvasPersonalitzatPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        binding.contingut.vista.setColor(Color.RED)
    }
}
