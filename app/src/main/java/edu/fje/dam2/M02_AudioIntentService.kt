package edu.fje.dam2

import android.content.Intent
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService

/**
 * Servei d'àudio en segon pla utilitzant Jetpack Media3 MediaSessionService.
 *
 * @author sergi.grau@fje.edu
 * @version 7.0 (API 36) 2026
 */
class M02_AudioIntentService : MediaSessionService() {

    private var player: ExoPlayer? = null
    private var mediaSession: MediaSession? = null

    override fun onCreate() {
        super.onCreate()
        val p = ExoPlayer.Builder(this).build().apply {
            val uri = Uri.parse("android.resource://$packageName/${R.raw.m02_audio1}")
            setMediaItem(MediaItem.fromUri(uri))
            prepare()
        }
        player = p
        mediaSession = MediaSession.Builder(this, p).build()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        intent?.getStringExtra("operacio")?.let { operacio ->
            when (operacio) {
                "inici" -> player?.play()
                "pausa" -> player?.pause()
                "salta" -> player?.seekTo(10000)
            }
        }
        return START_NOT_STICKY
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }

    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
        player = null
        super.onDestroy()
    }
}
