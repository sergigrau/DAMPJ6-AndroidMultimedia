package edu.fje.dam2

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder

/**
 * Servei que hereta de Service per a gestionar l'àudio en segon pla.
 *
 * @author sergi.grau@fje.edu
 * @version 6.0 27.01.2026
 */
class M02_AudioIntentService : Service() {

    private var mp: MediaPlayer? = null

    override fun onCreate() {
        super.onCreate()
        mp = MediaPlayer.create(this, R.raw.m02_audio1)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.getStringExtra("operacio")?.let { operacio ->
            when (operacio) {
                "inici" -> if (mp?.isPlaying == false) mp?.start()
                "pausa" -> if (mp?.isPlaying == true) mp?.pause()
                "salta" -> mp?.seekTo(10000)
            }
        }
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        mp?.release()
        mp = null
    }
}

