package edu.fje.dam2

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.material.snackbar.Snackbar
import edu.fje.dam2.databinding.M04NotificacionsSnackBarBinding

/**
 * Classe que fa ús del sistema de notificacions amb NotificationCompat de Jetpack
 * i SnackBar de Material Design.
 *
 * @author sergi.grau@fje.edu
 * @version 7.0 (API 36) 2026
 */
class M04_Notificacions_SnackBar : AppCompatActivity() {

    private lateinit var binding: M04NotificacionsSnackBarBinding
    private val CHANNEL_ID = "canal_notificacions_m04"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = M04NotificacionsSnackBarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        crearCanalNotificacio()

        binding.fab.setOnClickListener { view ->
            mostrarNotificacioJetpack()

            Snackbar.make(view, "Notificació Jetpack enviada!", Snackbar.LENGTH_LONG)
                .setAction("Veure") {
                    Toast.makeText(applicationContext, "Acció veure més", Toast.LENGTH_LONG).show()
                }.show()
        }
    }

    private fun crearCanalNotificacio() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Canal M04"
            val descriptionText = "Canal de notificacions d'exemple M04"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun mostrarNotificacioJetpack() {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Notificació Jetpack Core")
            .setContentText("Aquesta notificació ha estat creada amb NotificationCompat.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        val notificationManager = NotificationManagerCompat.from(this)
        try {
            notificationManager.notify(1001, builder.build())
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }
}
