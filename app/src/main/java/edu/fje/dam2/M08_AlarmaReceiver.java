package edu.fje.dam2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.VibratorManager;
import android.widget.Toast;

/**
 * BroadcastReceiver que el sistema mostra cada vegada
 * que l'alarma arriba a la seva fi
 * @version 6.0 (API 33)
 * @author sergi.grau@fje.edu
 */
public class M08_AlarmaReceiver extends BroadcastReceiver {
    public M08_AlarmaReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Vibrant Alarma", Toast.LENGTH_LONG).show();

        // VibratorManager substitueix Vibrator directament (deprecated a API 31)
        VibratorManager vibratorManager =
                (VibratorManager) context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE);
        Vibrator vibrator = vibratorManager.getDefaultVibrator();

        VibrationEffect effect = VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE);
        vibrator.vibrate(effect);

        // També reproduir un so per fer evidència d'alarma (so per defecte d'alarma o notificació)
        try {
            Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            if (alarmUri == null) {
                alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            }
            Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);
            if (ringtone != null) {
                ringtone.play();
            }
        } catch (Exception e) {
            // No cal llençar, només enregistrar si voleu; mantindrem silenciós per compatibilitat
        }
    }
}
