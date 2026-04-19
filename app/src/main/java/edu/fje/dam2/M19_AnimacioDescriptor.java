package edu.fje.dam2;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class M19_AnimacioDescriptor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m11_animacio_requadre);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
