package edu.fje.dam2

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.WindowMetrics
import androidx.appcompat.app.AppCompatActivity
import edu.fje.dam2.databinding.M11AnimacioRequadreBinding

/**
 * Classe que implementa una animació seqüencial utilitzant ViewBinding de Jetpack.
 *
 * @author sergi.grau@fje.edu
 * @version 7.0 (API 36) 2026
 */
class M11_AnimacioRequadre : AppCompatActivity() {

    private lateinit var binding: M11AnimacioRequadreBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = M11AnimacioRequadreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val boto = binding.boto
        val metrics: WindowMetrics = windowManager.currentWindowMetrics
        val bounds = metrics.bounds
        val alcada = bounds.height()
        val amplada = bounds.width()

        boto.setOnClickListener {
            val animacio1 = ObjectAnimator.ofFloat(boto, "x", 0f, (amplada - boto.width).toFloat()).apply { duration = 1000 }
            val animacio2 = ObjectAnimator.ofFloat(boto, "y", 0f, (alcada - boto.height).toFloat()).apply { duration = 1000 }
            val animacio3 = ObjectAnimator.ofFloat(boto, "x", (amplada - boto.width).toFloat(), 0f).apply { duration = 1000 }
            val animacio4 = ObjectAnimator.ofFloat(boto, "y", (alcada - boto.height).toFloat(), 0f).apply { duration = 1000 }

            val animacio = AnimatorSet()
            animacio.playSequentially(animacio1, animacio2, animacio3, animacio4)
            animacio.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(a: Animator) {
                    super.onAnimationEnd(a)
                    animacio.start()
                }
            })
            animacio.start()
        }
    }
}
