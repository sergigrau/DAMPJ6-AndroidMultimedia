package edu.fje.dam2;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.content.BroadcastReceiver;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Classe que demostra el funcionament dels serveis a Android
 * Disposa d'una classe d'utilitat per a facilitar la manipulació de la taula
 * per detectar els canvis afegeix   android:configChanges="orientation|screenSize
 * a l'activity en el manifest
 *  * @author sergi.grau@fje.edu
 *  * @version 6.0 (API 33) 19.04.2026
 */
public class M21_SumaServeiActivity extends AppCompatActivity {

    private Intent intent;
    private EditText n1, n2;
    private BroadcastReceiver resultatReceiver;
    private TextView resultat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.m21_activity_suma_servei);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        n1 = findViewById(R.id.numero1);
        n2 = findViewById(R.id.numero2);
        resultat = findViewById(R.id.resultat);

        findViewById(R.id.botoSuma).setOnClickListener(v -> {
            intent = new Intent(this, M21_SumaService.class);
            intent.putExtra("n1", Integer.parseInt(n1.getText().toString()));
            intent.putExtra("n2", Integer.parseInt(n2.getText().toString()));
            startService(intent);
        });

        resultatReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.v("DAM2", "dades rebudes");
                int res = intent.getIntExtra("suma",0);
                resultat.setText(String.valueOf(res));
            }
        };

        registerReceiver(resultatReceiver, new IntentFilter("edu.fje.smx8.SUMA_RESULTAT"), Context.RECEIVER_EXPORTED);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(resultatReceiver);
    }
}