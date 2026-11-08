package edu.fje.dam2

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import edu.fje.dam2.databinding.M03AsyntaskBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Activity que permet demostrar l'execució de tasques en segon pla
 * utilitzant Kotlin Coroutines amb Jetpack Lifecycle Scope.
 *
 * @author sergi.grau@fje.edu
 * @version 7.0 (API 36) 2026
 */
class M03_ExecutorServiceActivity : AppCompatActivity() {

    private lateinit var binding: M03AsyntaskBinding
    private val valors = ArrayList<String>()
    private var comptador = 0
    private lateinit var adaptador: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = M03AsyntaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        valors.add("A")
        adaptador = ArrayAdapter(this, android.R.layout.simple_list_item_1, valors)
        binding.llista.adapter = adaptador

        val bounds: Rect = windowManager.currentWindowMetrics.bounds
        val amplada = bounds.width()
        val alcada = bounds.height()

        binding.boto.setOnClickListener {
            val animacio1 = ObjectAnimator.ofFloat(binding.boto, "x", 0f, (amplada - binding.boto.width).toFloat())
            animacio1.duration = 1000
            val animacio2 = ObjectAnimator.ofFloat(binding.boto, "y", 0f, (alcada - binding.boto.height).toFloat())
            animacio2.duration = 1000
            val animacio3 = ObjectAnimator.ofFloat(binding.boto, "x", (amplada - binding.boto.width).toFloat(), 0f)
            animacio3.duration = 1000
            val animacio4 = ObjectAnimator.ofFloat(binding.boto, "y", (alcada - binding.boto.height).toFloat(), 0f)
            animacio4.duration = 1000

            val animacio = AnimatorSet()
            animacio.playSequentially(animacio1, animacio2, animacio3, animacio4)
            animacio.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(a: Animator) {
                    super.onAnimationEnd(a)
                    executarTascaCorrutina()
                    animacio.start()
                }
            })
            animacio.start()
        }
    }

    private fun executarTascaCorrutina() {
        lifecycleScope.launch(Dispatchers.IO) {
            val resultat = (comptador++).toString()
            withContext(Dispatchers.Main) {
                Log.v("DAM2", resultat)
                valors.add(resultat)
                adaptador.notifyDataSetChanged()
            }
        }
    }
}
