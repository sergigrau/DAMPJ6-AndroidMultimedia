package edu.fje.dam2

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Activity que permet demostrar el funcionament d'un Servei.
 * Utilitza un ExecutorService per executar tasques en segon pla.
 *
 * @author sergi.grau@fje.edu
 * @version 6.0 27.01.2026
 */
class M03_ExecutorServiceActivity : AppCompatActivity() {

    private val valors = ArrayList<String>()
    private var comptador = 0
    private lateinit var adaptador: ArrayAdapter<String>
    private val executor: ExecutorService = Executors.newSingleThreadExecutor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.m03_asyntask)

        val boto: Button = findViewById(R.id.boto)
        val llista: ListView = findViewById(R.id.llista)

        valors.add("A")
        adaptador = ArrayAdapter(this, android.R.layout.simple_list_item_1, valors)
        llista.adapter = adaptador

        val bounds: Rect = windowManager.currentWindowMetrics.bounds
        val amplada = bounds.width()
        val alcada = bounds.height()

        boto.setOnClickListener {
            val animacio1 = ObjectAnimator.ofFloat(boto, "x", 0f, (amplada - boto.width).toFloat())
            animacio1.duration = 1000
            val animacio2 = ObjectAnimator.ofFloat(boto, "y", 0f, (alcada - boto.height).toFloat())
            animacio2.duration = 1000
            val animacio3 = ObjectAnimator.ofFloat(boto, "x", (amplada - boto.width).toFloat(), 0f)
            animacio3.duration = 1000
            val animacio4 = ObjectAnimator.ofFloat(boto, "y", (alcada - boto.height).toFloat(), 0f)
            animacio4.duration = 1000

            val animacio = AnimatorSet()
            animacio.playSequentially(animacio1, animacio2, animacio3, animacio4)
            animacio.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(a: Animator) {
                    super.onAnimationEnd(a)
                    executarTasca()
                    animacio.start()
                }
            })
            animacio.start()
        }
    }

    private fun executarTasca() {
        executor.execute {
            val resultat = comptador++.toString()
            runOnUiThread {
                Log.v("DAM2", resultat)
                valors.add(resultat)
                adaptador.notifyDataSetChanged()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        executor.shutdown()
    }
}

