package edu.fje.dam2

import android.graphics.Rect
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import edu.fje.dam2.databinding.M07CanvasPrincipalBinding

/**
 * Activitat que mostra el funcionament de Canvas utilitzant ViewBinding de Jetpack.
 *
 * @author sergi.grau@fje.edu
 * @version 7.0 (API 36) 2026
 */
class M07_CanvasPath : AppCompatActivity() {

    private lateinit var binding: M07CanvasPrincipalBinding
    private lateinit var vistaPaint: M07_VistaPaint

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = M07CanvasPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        vistaPaint = binding.contingut.paintView
        val bounds: Rect = windowManager.currentWindowMetrics.bounds
        val metrics = DisplayMetrics().apply {
            widthPixels = bounds.width()
            heightPixels = bounds.height()
        }
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
