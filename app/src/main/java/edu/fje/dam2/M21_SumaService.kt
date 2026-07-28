package edu.fje.dam2

import android.app.Service
import android.content.Intent
import android.os.IBinder
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Servei que calcula la suma de dos nombres en segon pla.
 *
 * @author sergi.grau@fje.edu
 * @version 6.0 (API 33) 19.04.2026
 */
class M21_SumaService : Service() {

    private val executor: ExecutorService = Executors.newSingleThreadExecutor()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            val n1 = it.getIntExtra("n1", 0)
            val n2 = it.getIntExtra("n2", 0)
            executor.execute {
                val result = n1 + n2
                val resultIntent = Intent("edu.fje.smx8.SUMA_RESULTAT")
                resultIntent.putExtra("suma", result)
                sendBroadcast(resultIntent)
                stopSelf(startId)
            }
        }
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        executor.shutdown()
    }
}

