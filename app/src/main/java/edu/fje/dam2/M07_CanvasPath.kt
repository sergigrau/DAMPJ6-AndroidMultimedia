package edu.fje.dam2

import android.os.Bundle
import android.graphics.Rect
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

/**
 * Activitat que mostra el funcionament de Canvas, fa ús de Paint i Path.
 *
 * @author sergi.grau@fje.edu
 * @version 6.0 27.01.2026
 */
class M07_CanvasPath : AppCompatActivity() {

    private lateinit var vistaPaint: M07_VistaPaint

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.m07_canvas_principal)

        val toolbar: Toolbar? = findViewById(R.id.toolbar)
        toolbar?.let { setSupportActionBar(it) }

        vistaPaint = findViewById(R.id.paintView)
        val bounds: Rect = windowManager.currentWindowMetrics.bounds
        val metrics = DisplayMetrics()
        metrics.widthPixels = bounds.width()
        metrics.heightPixels = bounds.height()
        vistaPaint.init(metrics)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.m07_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.emboss -> { vistaPaint.posaRelleu(); true }
            R.id.blur   -> { vistaPaint.posaDifumina(); true }
            R.id.clear  -> { vistaPaint.neteja(); true }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

