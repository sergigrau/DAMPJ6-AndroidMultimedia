package edu.fje.dam2;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

/**
 * Classe que demostra el funcionament de Canvas.
 * Dibuixa directament sobre una View, utilitza Vistes personalitzades
 * @author sergi.grau@fje.edu
 * @version 6.0 27.01.2026
 */

public class M06_CanvasPersonalitzat extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m06_canvas_personalitzat_principal);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        M06_VistaPropia vistaPropia = findViewById(R.id.vista);
        //vistaPropia.setX(20);
        //vistaPropia.setY(40);
        vistaPropia.setColor(Color.RED);
        //vistaPropia.setCadena("Android");
    }

}
