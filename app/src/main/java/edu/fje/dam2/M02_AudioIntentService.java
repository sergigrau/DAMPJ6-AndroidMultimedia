package edu.fje.dam2;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;

/**
 * Servei que hereta de Service per a gestionar l'àudio en segon pla.
 * L'activitat que el gestiona és M02_AudioServeiActivity
 *
 * @author sergi.grau@fje.edu
 * @version 6.0 27.01.2026 */
public class M02_AudioIntentService extends Service {

    private MediaPlayer mp;

    @Override
    public void onCreate() {
        super.onCreate();
        mp = MediaPlayer.create(this, R.raw.m02_audio1);
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        if (intent != null) {
            String operacio = intent.getStringExtra("operacio");
            if (operacio != null) {
                switch (operacio) {
                    case "inici":
                        if (!mp.isPlaying()) mp.start();
                        break;
                    case "pausa":
                        if (mp.isPlaying()) mp.pause();
                        break;
                    case "salta":
                        mp.seekTo(10000);
                        break;
                }
            }
        }
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mp != null) {
            mp.release();
            mp = null;
        }
    }
}
