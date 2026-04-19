package edu.fje.dam2;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Activity que permet demostrar el funcionament d'un Servei
 * Utilitza un ExecutorService per executar tasques en segon pla, ja que AsyncTask està deprecated a partir de Android 11 (API 30).
 * L'animació del botó es realitza amb ObjectAnimator i AnimatorSet, i després de cada cicle d'animació, s'executa una tasca que actualitza la llista amb un nou valor.
 * La tasca en segon pla incrementa un comptador i actualitza la interfície d'usuari amb el nou valor, utilitzant runOnUiThread per assegurar que les actualitzacions de la UI es realitzen en el fil principal.
 *
 * @author sergi.grau@fje.edu
 * @version 6.0 27.01.2026
 */
public class M03_ExecutorServiceActivity extends AppCompatActivity {

    private final ArrayList<String> valors = new ArrayList<>();
    private int comptador = 0;
    private ArrayAdapter adaptador;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m03_asyntask);

        final Button boto = findViewById(R.id.boto);

        ListView llista = findViewById(R.id.llista);
        valors.add("A");
        adaptador = new ArrayAdapter(this, android.R.layout.simple_list_item_1, valors);
        llista.setAdapter(adaptador);

        // Usar WindowMetrics en lloc de getDefaultDisplay() (deprecated a API 30)
        Rect bounds = getWindowManager().getCurrentWindowMetrics().getBounds();
        final int amplada = bounds.width();
        final int alcada = bounds.height();

        boto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ObjectAnimator animacio1 =
                        ObjectAnimator.ofFloat(boto, "x", 0, amplada - boto.getWidth());
                animacio1.setDuration(1000);
                ObjectAnimator animacio2 =
                        ObjectAnimator.ofFloat(boto, "y", 0, alcada - boto.getHeight());
                animacio2.setDuration(1000);
                ObjectAnimator animacio3 =
                        ObjectAnimator.ofFloat(boto, "x", amplada - boto.getWidth(), 0);
                animacio3.setDuration(1000);
                ObjectAnimator animacio4 =
                        ObjectAnimator.ofFloat(boto, "y", alcada - boto.getHeight(), 0);
                animacio4.setDuration(1000);

                final AnimatorSet animacio = new AnimatorSet();
                animacio.playSequentially(animacio1, animacio2, animacio3, animacio4);

                animacio.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator a) {
                        super.onAnimationEnd(a);
                        executarTasca();
                        animacio.start();
                    }
                });
                animacio.start();
            }
        });
    }

    /**
     * Executa la tasca en segon pla amb ExecutorService (substitueix AsyncTask deprecated)
     */
    private void executarTasca() {
        executor.execute(() -> {
            final String resultat = String.valueOf(comptador++);
            runOnUiThread(() -> {
                Log.v("DAM2", resultat);
                valors.add(resultat);
                adaptador.notifyDataSetChanged();
            });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }
}
