package edu.fje.dam2

import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.google.android.material.snackbar.Snackbar
import edu.fje.dam2.databinding.M01AudioPrincipalBinding

/**
 * Activitat que mostra el funcionament de Jetpack Media3 ExoPlayer
 * i la gestió automàtica del focus d'àudio.
 *
 * @author sergi.grau@fje.edu
 * @version 7.0 (API 36) 2026
 */
class M01_AudioActivity : AppCompatActivity() {

    private lateinit var binding: M01AudioPrincipalBinding
    private var player: ExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = M01AudioPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val audioAttributes = AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
            .build()

        player = ExoPlayer.Builder(this)
            .setAudioAttributes(audioAttributes, true)
            .build().apply {
                val uri = Uri.parse("android.resource://$packageName/${R.raw.m02_audio1}")
                setMediaItem(MediaItem.fromUri(uri))
                prepare()

                addListener(object : Player.Listener {
                    override fun onPlaybackStateChanged(state: Int) {
                        if (state == Player.STATE_READY) {
                            binding.barraProgress.max = duration.toInt()
                        }
                    }

                    override fun onIsPlayingChanged(isPlaying: Boolean) {
                        val iconRes = if (isPlaying) android.R.drawable.ic_media_pause else android.R.drawable.ic_media_play
                        binding.fab.setImageResource(iconRes)
                    }
                })
            }

        binding.fab.setImageResource(android.R.drawable.ic_media_play)
        binding.fab.setOnClickListener { view ->
            player?.let { p ->
                val text: String
                if (p.isPlaying) {
                    text = "Pausant Àudio (Media3)"
                    p.pause()
                } else {
                    text = "Reproduint Àudio (Media3)"
                    p.play()
                }
                Snackbar.make(view, text, Snackbar.LENGTH_LONG).show()
            }
        }

        binding.barraProgress.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    player?.seekTo(progress.toLong())
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.release()
        player = null
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
