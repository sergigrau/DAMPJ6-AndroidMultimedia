package edu.fje.dam2

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

/**
 * Classe que demostra el funcionament de Canvas amb Vistes personalitzades.
 *
 * @author sergi.grau@fje.edu
 * @version 6.0 27.01.2026
 */
class M06_CanvasPersonalitzat : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.m06_canvas_personalitzat_principal)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val vistaPropia: M06_VistaPropia = findViewById(R.id.vista)
        vistaPropia.setColor(Color.RED)
    }
}

