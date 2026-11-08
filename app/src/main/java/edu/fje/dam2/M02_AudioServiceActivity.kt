package edu.fje.dam2

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import edu.fje.dam2.databinding.M01AudioPrincipalBinding

/**
 * Activitat que controla la reproducció d'àudio en segon pla utilitzant
 * Jetpack Media3 MediaSessionService.
 *
 * @author sergi.grau@fje.edu
 * @version 7.0 (API 36) 2026
 */
class M02_AudioServiceActivity : AppCompatActivity() {

    private lateinit var binding: M01AudioPrincipalBinding
    private var isReproduint = false
    private lateinit var serviceIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = M01AudioPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        serviceIntent = Intent(this, M02_AudioIntentService::class.java)

        binding.fab.setImageResource(android.R.drawable.ic_media_play)
        binding.fab.setOnClickListener { view ->
            val text: String
            if (!isReproduint) {
                text = "Reproduint Àudio amb Media3 Service"
                binding.fab.setImageResource(android.R.drawable.ic_media_pause)
                serviceIntent.putExtra("operacio", "inici")
                startService(serviceIntent)
                isReproduint = true
            } else {
                text = "Pausant Àudio amb Media3 Service"
                binding.fab.setImageResource(android.R.drawable.ic_media_play)
                serviceIntent.putExtra("operacio", "pausa")
                startService(serviceIntent)
                isReproduint = false
            }
            Snackbar.make(view, text, Snackbar.LENGTH_LONG).show()
        }

        binding.barraProgress.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {}
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
