package edu.fje.dam2

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import edu.fje.dam2.databinding.M08AlarmaBinding
import java.util.concurrent.TimeUnit

/**
 * Activitat que demostra el funcionament de tasques programades (Alarmes)
 * utilitzant Jetpack WorkManager.
 *
 * @author sergi.grau@fje.edu
 * @version 7.0 (API 36) 2026
 */
class M08_AlarmaActivity : AppCompatActivity() {

    private lateinit var binding: M08AlarmaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = M08AlarmaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.boto.setOnClickListener { iniciarAlarma(it) }
        binding.botoRep.setOnClickListener { iniciarAlarmaRepeticio(it) }
    }

    fun iniciarAlarma(view: View) {
        val textSegons = binding.temps.text.toString()
        val i = textSegons.toIntOrNull() ?: 5

        val workRequest = OneTimeWorkRequestBuilder<M08_AlarmaWorker>()
            .setInitialDelay(i.toLong(), TimeUnit.SECONDS)
            .build()

        WorkManager.getInstance(this).enqueue(workRequest)

        Toast.makeText(this, "Alarma programada amb WorkManager en $i segons", Toast.LENGTH_LONG).show()
    }

    fun iniciarAlarmaRepeticio(view: View) {
        val periodicWorkRequest = PeriodicWorkRequestBuilder<M08_AlarmaWorker>(15, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(this).enqueue(periodicWorkRequest)

        Toast.makeText(this, "Alarma periòdica programada amb WorkManager (cada 15 min)", Toast.LENGTH_LONG).show()
    }
}
