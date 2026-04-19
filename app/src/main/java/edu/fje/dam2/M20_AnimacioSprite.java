package edu.fje.dam2;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class M20_AnimacioSprite extends AppCompatActivity {

    AnimationDrawable a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m20_animacio_sprite);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
