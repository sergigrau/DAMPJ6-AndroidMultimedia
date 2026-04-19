package edu.fje.dam2;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Servei que substitueix IntentService per a API 33.
 */
public class M21_SumaService extends Service {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            final int n1 = intent.getIntExtra("n1", 0);
            final int n2 = intent.getIntExtra("n2", 0);
            executor.execute(() -> {
                int result = n1 + n2;
                Intent resultIntent = new Intent("edu.fje.smx8.SUMA_RESULTAT");
                resultIntent.putExtra("suma", result);
                sendBroadcast(resultIntent);
                stopSelf(startId);
            });
        }
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }
}