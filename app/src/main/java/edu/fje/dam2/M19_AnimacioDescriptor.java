package edu.fje.dam2;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

/**
 * Classe que demostra el funcionament de les animacions a Android
 * Disposa d'una classe d'utilitat per a facilitar la manipulació de la taula
 * per detectar els canvis afegeix   android:configChanges="orientation|screenSize
 * a l'activity en el manifest
 * @author sergi.grau@fje.edu
 * @version 6.0 (API 33) 19.04.2026
 */
public class M19_AnimacioDescriptor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m11_animacio_requadre);

        final Button boto = (Button) findViewById(R.id.boto);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final int amplada = size.x;
        final int alcada = size.y;

        AnimatorSet set = (AnimatorSet)
                AnimatorInflater.loadAnimator(this,R.animator.animacio);
        set.setTarget(boto);
        set.start();

    }
}
