package edu.fje.dam2

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

/**
 * Activitat que mostra el funcionament de la
 * classe MediaPlayer amb un IntentService.
 *
 * @author sergi.grau@fje.edu
 * @version 6.0 27.01.2026
 */
class M02_AudioServiceActivity : AppCompatActivity() {

    private val isReproduint = false
    private lateinit var intent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent = Intent(this, M02_AudioIntentService::class.java)
        intent.putExtra("operacio", "inici")
        startService(intent)

        setContentView(R.layout.m01_audio_principal)
        val barraProgress: SeekBar = findViewById(R.id.barraProgress)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setImageResource(android.R.drawable.ic_media_pause)
        fab.setOnClickListener { view ->
            val text: String
            if (!isReproduint) {
                text = "Pausant Audio"
                fab.setImageResource(android.R.drawable.ic_media_play)
                intent.putExtra("operacio", "inici")
                startService(intent)
            } else {
                text = "Reproduint Audio"
                fab.setImageResource(android.R.drawable.ic_media_pause)
                intent.putExtra("operacio", "pausa")
                startService(intent)
            }
            Snackbar.make(view, text, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        barraProgress.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {}
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.m01_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_settings) return true
        return super.onOptionsItemSelected(item)
    }
}

