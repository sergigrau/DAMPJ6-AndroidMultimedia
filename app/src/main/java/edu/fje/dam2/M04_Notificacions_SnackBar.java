package edu.fje.dam2;

import android.os.Bundle;

import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

/**
 * Classe que fa us del sistema de notificaicons amb SnackBar. Es pot aplicar
 * a qualsevol View, però és recomanable un CoordinatorLayout
 * Snackbar és una notificació que apareix a la part inferior de la pantalla, i desapareix després d'un temps o quan l'usuari interactua amb ella. Es pot personalitzar amb un text, una acció i un temps de durada.
 * Per crear un Snackbar, es pot utilitzar el mètode make() de la classe
 * Snackbar, que rep com a paràmetres la vista on es vol mostrar el Snackbar, el text que es vol mostrar i la durada del Snackbar. Per exemple:
 * Snackbar.make(view, "Reproduïnt...", Snackbar.LENGTH_LONG).show();
 * Per afegir una acció al Snackbar, es pot utilitzar el mètode setAction() de la classe Snackbar, que rep com a paràmetres el text de l'acció i un OnClickListener que s'executarà quan l'usuari faci clic a l'acció. Per exemple:
 *
 * @author sergi.grau@fje.edu
 * @version 6.0 27.01.2026
 */
public class M04_Notificacions_SnackBar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m04_notificacions_snack_bar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Reproduïnt...", Snackbar.LENGTH_LONG)
                .setAction("Veure", v -> Toast.makeText(getApplicationContext(), "Acció veure més",
                        Toast.LENGTH_LONG).show()).show());
    }

}
