package edu.fje.dam2

import android.animation.ObjectAnimator
import android.graphics.Rect
import android.os.Bundle
import android.view.WindowMetrics
import androidx.appcompat.app.AppCompatActivity
import edu.fje.dam2.databinding.M10AnimacioBasicaBinding

/**
 * Classe que implementa una animació bàsica utilitzant ViewBinding de Jetpack
 * i ObjectAnimator d'AndroidX.
 *
 * @author sergi.grau@fje.edu
 * @version 7.0 (API 36) 2026
 */
class M10_AnimacioBasica : AppCompatActivity() {

    private lateinit var binding: M10AnimacioBasicaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = M10AnimacioBasicaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        binding.fab.setOnClickListener {
            val metrics: WindowMetrics = windowManager.currentWindowMetrics
            val bounds: Rect = metrics.bounds
            val alcada = bounds.height()

            val animY = ObjectAnimator.ofFloat(binding.fab, "y", 0f, alcada / 2f).apply { duration = 1000 }
            val animAlpha = ObjectAnimator.ofFloat(binding.fab, "alpha", 1f, 0f).apply { duration = 1000 }
            animY.start()
            animAlpha.start()
        }
    }
}
