package edu.fje.dam2

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

/**
 * Classe que demostra el funcionament de les animacions des d'un fitxer descriptor.
 *
 * @author sergi.grau@fje.edu
 * @version 6.0 (API 33) 19.04.2026
 */
class M19_AnimacioDescriptor : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.m11_animacio_requadre)

        val boto: Button = findViewById(R.id.boto)

        val set = AnimatorInflater.loadAnimator(this, R.animator.animacio) as AnimatorSet
        set.setTarget(boto)
        set.start()
    }
}

