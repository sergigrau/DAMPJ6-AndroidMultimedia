package edu.fje.dam2

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

/**
 * Classe que fa ús del sistema de notificacions amb SnackBar.
 *
 * @author sergi.grau@fje.edu
 * @version 6.0 27.01.2026
 */
class M04_Notificacions_SnackBar : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.m04_notificacions_snack_bar)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Reproduïnt...", Snackbar.LENGTH_LONG)
                .setAction("Veure") {
                    Toast.makeText(applicationContext, "Acció veure més", Toast.LENGTH_LONG).show()
                }.show()
        }
    }
}

