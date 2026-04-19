package edu.fje.dam2;

import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

/**
 * Activitat que mostra el funcionament de la
 * classe MediaPlayer amb un IntentService
 * Es pot controlar la reproducció des de l'activitat, però el servei segueix reproduint encara que es tanqui l'activitat
 * @author sergi.grau@fje.edu
 * @version 6.0 27.01.2026
 */

public class M02_AudioServiceActivity extends AppCompatActivity {
    private final boolean isReproduint= false;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        intent= new Intent(this, M02_AudioIntentService.class);
        intent.putExtra("operacio", "inici");
        startService(intent);

        setContentView(R.layout.m01_audio_principal);
        SeekBar barraProgress = findViewById(R.id.barraProgress);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setImageResource(android.R.drawable.ic_media_pause);
        fab.setOnClickListener(view -> {
            String text;

            if (!isReproduint) {
                text = "Pausant Audio";
                fab.setImageResource(android.R.drawable.ic_media_play);
                intent.putExtra("operacio", "inici");
                startService(intent);

            } else {
                text = "Reproduint Audio";
                fab.setImageResource(android.R.drawable.ic_media_pause);
                intent.putExtra("operacio", "pausa");
                startService(intent);
            }

            Snackbar.make(view, text, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        });


       // barraProgress.setMax(mp.getDuration());



        barraProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

             @Override
             public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                //mp.seekTo(barraProgress.getProgress());
             }
             @Override
             public void onStartTrackingTouch(SeekBar seekBar) {
             }
             @Override
             public void onStopTrackingTouch(SeekBar seekBar) {

             }
         }
        );
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
}
