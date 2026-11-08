package edu.fje.dam2

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import edu.fje.dam2.databinding.M11AnimacioRequadreBinding

/**
 * Classe que demostra el funcionament de les animacions des d'un fitxer descriptor XML
 * utilitzant ViewBinding de Jetpack.
 *
 * @author sergi.grau@fje.edu
 * @version 7.0 (API 36) 2026
 */
class M19_AnimacioDescriptor : AppCompatActivity() {

    private lateinit var binding: M11AnimacioRequadreBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = M11AnimacioRequadreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val set = AnimatorInflater.loadAnimator(this, R.animator.animacio) as AnimatorSet
        set.setTarget(binding.boto)
        set.start()
    }
}
