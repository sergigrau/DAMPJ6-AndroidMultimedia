package edu.fje.dam2

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.WindowMetrics
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

/**
 * Classe que implementa una animació seqüencial.
 *
 * @author sergi.grau@fje.edu
 * @version 6.0 27.01.2026
 */
class M11_AnimacioRequadre : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.m11_animacio_requadre)

        val boto: Button = findViewById(R.id.boto)
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

