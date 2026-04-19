package edu.fje.dam2;

import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

/**
 * Activitat que mostra el funcionament de la
 * classe MediaPlayer i de la gestió del focus
 * d'àudio. Per a gestionar el focus d'àudio cal afegir al manifest de l'aplicació
 * el següent codi dins de l'etiqueta application:
 *   <meta-data
 * android:name="preloaded_fonts"
 * android:resource="@array/preloaded_fonts" />
 *
 * @author sergi.grau@fje.edu
 * @version 6.0 27.01.2026
 */

public class M01_AudioActivity extends AppCompatActivity {

    private MediaPlayer mp;
    private final String LOG = "edu.fje.dam2";
    private SeekBar barraProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m01_audio_principal);
        barraProgress = findViewById(R.id.barraProgress);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setImageResource(android.R.drawable.ic_media_pause);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text;
                if (mp.isPlaying()) {
                    text = "Pausant Audio";
                    fab.setImageResource(android.R.drawable.ic_media_play);
                    mp.pause();
                } else {
                    text = "Reproduint Audio";
                    fab.setImageResource(android.R.drawable.ic_media_pause);
                    mp.start();
                }
                Snackbar.make(view, text, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mp = MediaPlayer.create(this, R.raw.m02_audio1);
        barraProgress.setMax(mp.getDuration());

        AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();

        AudioFocusRequest audioFocusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK)
                .setAudioAttributes(audioAttributes)
                .setOnAudioFocusChangeListener(mAudioFocusListener)
                .build();

        int requestResult = am.requestAudioFocus(audioFocusRequest);
        if (requestResult == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            mp.start();
            Log.d(LOG, "audioFocus listener aconseguit amb èxit");
        } else if (requestResult == AudioManager.AUDIOFOCUS_REQUEST_FAILED) {
            mp.stop();
        } else {
            Log.d(LOG, "error en la petició del listener de focus");
        }

        barraProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mp.seekTo(barraProgress.getProgress());
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.m01_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private final AudioManager.OnAudioFocusChangeListener mAudioFocusListener = new AudioManager.OnAudioFocusChangeListener() {
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_LOSS:
                    mp.stop();
                    Log.d(LOG, "AudioFocus: rebut AUDIOFOCUS_LOSS");
                    mp.release();
                    mp = null;
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    if (mp.isPlaying()) mp.pause();
                    Log.d(LOG, "AudioFocus: rebut AUDIOFOCUS_LOSS_TRANSIENT");
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    mp.setVolume(0.5f, 0.5f);
                    Log.d(LOG, "AudioFocus: rebut AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK");
                    break;
                case AudioManager.AUDIOFOCUS_GAIN:
                    mp.start();
                    mp.setVolume(1.0f, 1.0f);
                    Log.d(LOG, "AudioFocus: rebut AUDIOFOCUS_GAIN");
                    break;
                default:
                    Log.e(LOG, "codi desconegut");
            }
        }
    };
}
