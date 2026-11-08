package edu.fje.dam2

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import edu.fje.dam2.databinding.M09PropietatsDinamiquesBinding

/**
 * Classe que assigna propietats a vistes creades de manera dinàmica
 * utilitzant Jetpack ConstraintLayout i ConstraintSet.
 *
 * @author sergi.grau@fje.edu
 * @version 7.0 (API 36) 2026
 */
class M09_Propietats_dinamiques : AppCompatActivity() {

    private lateinit var binding: M09PropietatsDinamiquesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = M09PropietatsDinamiquesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val constraintLayout = ConstraintLayout(this).apply {
            id = View.generateViewId()
            layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
            )
        }

        binding.contingut.contingut.addView(constraintLayout)

        val boto = Button(this).apply {
            id = View.generateViewId()
            text = "Sergi Grau (Dynamic Jetpack)"
            setOnClickListener {
                Toast.makeText(this@M09_Propietats_dinamiques, "Botó clicat: Sergi Grau", Toast.LENGTH_SHORT).show()
            }
        }
        constraintLayout.addView(boto)

        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)
        constraintSet.connect(boto.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 64)
        constraintSet.connect(boto.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 32)
        constraintSet.connect(boto.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 32)
        constraintSet.applyTo(constraintLayout)
    }
}
