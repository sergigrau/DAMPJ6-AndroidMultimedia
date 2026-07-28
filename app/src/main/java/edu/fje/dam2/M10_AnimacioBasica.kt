package edu.fje.dam2

import android.animation.ObjectAnimator
import android.graphics.Rect
import android.os.Bundle
import android.view.WindowMetrics
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * Classe que implementa una animació bàsica.
 *
 * @author sergi.grau@fje.edu
 * @version 6.0 27.01.2026
 */
class M10_AnimacioBasica : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.m10_animacio_basica)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            val metrics: WindowMetrics = windowManager.currentWindowMetrics
            val bounds: Rect = metrics.bounds
            val alcada = bounds.height()

            var anim = ObjectAnimator.ofFloat(fab, "y", 0f, alcada / 2f)
            anim.duration = 1000
            anim.start()
            anim = ObjectAnimator.ofFloat(fab, "alpha", 1f, 0f)
            anim.duration = 1000
            anim.start()
        }
    }
}

