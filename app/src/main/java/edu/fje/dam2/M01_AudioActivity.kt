package edu.fje.dam2

import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

/**
 * Activitat que mostra el funcionament de la
 * classe MediaPlayer i de la gestió del focus d'àudio.
 *
 * @author sergi.grau@fje.edu
 * @version 6.0 27.01.2026
 */
class M01_AudioActivity : AppCompatActivity() {

    private var mp: MediaPlayer? = null
    private val LOG = "edu.fje.dam2"
    private lateinit var barraProgress: SeekBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.m01_audio_principal)
        barraProgress = findViewById(R.id.barraProgress)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setImageResource(android.R.drawable.ic_media_pause)
        fab.setOnClickListener { view ->
            val text: String
            if (mp?.isPlaying == true) {
                text = "Pausant Audio"
                fab.setImageResource(android.R.drawable.ic_media_play)
                mp?.pause()
            } else {
                text = "Reproduint Audio"
                fab.setImageResource(android.R.drawable.ic_media_pause)
                mp?.start()
            }
            Snackbar.make(view, text, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        mp = MediaPlayer.create(this, R.raw.m02_audio1)
        barraProgress.max = mp!!.duration

        val am = getSystemService(AUDIO_SERVICE) as AudioManager

        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()

        val audioFocusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK)
            .setAudioAttributes(audioAttributes)
            .setOnAudioFocusChangeListener(mAudioFocusListener)
            .build()

        val requestResult = am.requestAudioFocus(audioFocusRequest)
        when (requestResult) {
            AudioManager.AUDIOFOCUS_REQUEST_GRANTED -> {
                mp?.start()
                Log.d(LOG, "audioFocus listener aconseguit amb èxit")
            }
            AudioManager.AUDIOFOCUS_REQUEST_FAILED -> mp?.stop()
            else -> Log.d(LOG, "error en la petició del listener de focus")
        }

        barraProgress.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                mp?.seekTo(barraProgress.progress)
            }
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

    private val mAudioFocusListener = AudioManager.OnAudioFocusChangeListener { focusChange ->
        when (focusChange) {
            AudioManager.AUDIOFOCUS_LOSS -> {
                mp?.stop()
                Log.d(LOG, "AudioFocus: rebut AUDIOFOCUS_LOSS")
                mp?.release()
                mp = null
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                if (mp?.isPlaying == true) mp?.pause()
                Log.d(LOG, "AudioFocus: rebut AUDIOFOCUS_LOSS_TRANSIENT")
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                mp?.setVolume(0.5f, 0.5f)
                Log.d(LOG, "AudioFocus: rebut AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK")
            }
            AudioManager.AUDIOFOCUS_GAIN -> {
                mp?.start()
                mp?.setVolume(1.0f, 1.0f)
                Log.d(LOG, "AudioFocus: rebut AUDIOFOCUS_GAIN")
            }
            else -> Log.e(LOG, "codi desconegut")
        }
    }
}

