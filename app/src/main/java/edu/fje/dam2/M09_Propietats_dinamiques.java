package edu.fje.dam2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

/**
 * Classe que assigna propietats a vistes creades de manera dinàmica
 * Aquest exemple mostra com crear un botó de manera dinàmica i assignar-li propietats, com el text i les dimensions, i afegir-lo a un layout existent.
 * En aquest cas, es crea un botó amb el text "Sergi Grau" i s'afegeix a un LinearLayout que ja està definit al layout XML de l'activitat.
 *
 *
 * @author sergi.grau@fje.edu
 * @version 6.0 27.01.2026
 */

public class M09_Propietats_dinamiques extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m09_propietats_dinamiques);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button boto = new Button(this);
        boto.setText("Sergi Grau");
        LinearLayout contingut = findViewById(R.id.contingut);
        LinearLayout ll = new LinearLayout(this);
        // Orientació vertical per apilar vistes
        ll.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        int margin = (int) (16 * getResources().getDisplayMetrics().density);
        lp.setMargins(margin, margin, margin, margin);
        ll.setLayoutParams(lp);
        contingut.addView(ll);
        LinearLayout.LayoutParams btnLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        btnLp.setMargins(margin, margin, margin, margin);
        ll.addView(boto, btnLp);

        boto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(M09_Propietats_dinamiques.this, "Botó clicat: Sergi Grau", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
