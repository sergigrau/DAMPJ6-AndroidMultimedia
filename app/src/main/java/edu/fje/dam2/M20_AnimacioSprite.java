package edu.fje.dam2;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;

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
public class M20_AnimacioSprite extends AppCompatActivity {

    AnimationDrawable a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m20_animacio_sprite);

        ImageView imatge = (ImageView) findViewById(R.id.spriteImatge);
        imatge.setBackgroundResource(R.drawable.sprite);
        a = (AnimationDrawable) imatge.getBackground();
        a.start();
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            a.stop();
            return true;
        }
        return super.onTouchEvent(event);
    }

}
