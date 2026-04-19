package edu.fje.dam2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


/**
 * Activitat que demostra el funcionament d'una Alarma (un servei
 * del sistema) * per a demanar periodicament el valor d'una cotitzacio cada minut
 * Es crida des del layout en fer click
 * @version 6.0 27.01.2026
 * @author sergi.grau@fje.edu
 */
public class M08_AlarmaActivity extends Activity {

    private static final long TEMPS_REPETICIO = 1000L ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m08_alarma);

    }

    /**
     * Mètode que posa en marxa una Alarma un sol cop
     * Es cridat des del layout en fer click
     */
    public void iniciarAlarma(View view) {
        EditText temps =  findViewById(R.id.temps);
        int i = Integer.parseInt(temps.getText().toString());

        Intent intent = new Intent(this, M08_AlarmaReceiver.class);
        // FLAG_IMMUTABLE requerit des d'API 31
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this.getApplicationContext(), 234324243, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                + (i * 1000L), pendingIntent);

        Toast.makeText(this, "Alarma en " + i + " segons",
                Toast.LENGTH_LONG).show();
    }

    /**
     * Mètode que posa en marxa una Alarma i es repeteix
     * Es cridat des del layout en fer click
     */
    @SuppressLint("ShortAlarm")
    public void iniciarAlarmaRepeticio(View view) {

        Intent intent = new Intent(this, M08_AlarmaReceiver.class);
        // FLAG_IMMUTABLE requerit des d'API 31
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this.getApplicationContext(), 234324243, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                TEMPS_REPETICIO, pendingIntent);

        // TEMPS_REPETICIO està en milisegons, mostrar en segons és més clar
        Toast.makeText(this, "Alarma REPETIDA en " + (TEMPS_REPETICIO / 1000L) + " segons",
                Toast.LENGTH_LONG).show();
    }
}
