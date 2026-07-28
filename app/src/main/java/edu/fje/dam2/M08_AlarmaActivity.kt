package edu.fje.dam2

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast

/**
 * Activitat que demostra el funcionament d'una Alarma.
 *
 * @author sergi.grau@fje.edu
 * @version 6.0 27.01.2026
 */
class M08_AlarmaActivity : Activity() {

    companion object {
        private const val TEMPS_REPETICIO = 1000L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.m08_alarma)
    }

    fun iniciarAlarma(view: View) {
        val temps: EditText = findViewById(R.id.temps)
        val i = Integer.parseInt(temps.text.toString())

        val intent = Intent(this, M08_AlarmaReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext, 234324243, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (i * 1000L), pendingIntent)

        Toast.makeText(this, "Alarma en $i segons", Toast.LENGTH_LONG).show()
    }

    @SuppressLint("ShortAlarm")
    fun iniciarAlarmaRepeticio(view: View) {
        val intent = Intent(this, M08_AlarmaReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext, 234324243, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), TEMPS_REPETICIO, pendingIntent)

        Toast.makeText(this, "Alarma REPETIDA en ${TEMPS_REPETICIO / 1000L} segons", Toast.LENGTH_LONG).show()
    }
}

