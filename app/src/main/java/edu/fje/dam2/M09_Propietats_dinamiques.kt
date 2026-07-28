package edu.fje.dam2

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

/**
 * Classe que assigna propietats a vistes creades de manera dinàmica.
 *
 * @author sergi.grau@fje.edu
 * @version 6.0 27.01.2026
 */
class M09_Propietats_dinamiques : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.m09_propietats_dinamiques)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val boto = Button(this)
        boto.text = "Sergi Grau"
        val contingut: LinearLayout = findViewById(R.id.contingut)
        val ll = LinearLayout(this)
        ll.orientation = LinearLayout.VERTICAL

        val margin = (16 * resources.displayMetrics.density).toInt()
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        )
        lp.setMargins(margin, margin, margin, margin)
        ll.layoutParams = lp
        contingut.addView(ll)

        val btnLp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        )
        btnLp.setMargins(margin, margin, margin, margin)
        ll.addView(boto, btnLp)

        boto.setOnClickListener {
            Toast.makeText(this, "Botó clicat: Sergi Grau", Toast.LENGTH_SHORT).show()
        }
    }
}

