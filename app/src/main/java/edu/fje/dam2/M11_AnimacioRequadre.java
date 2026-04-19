package edu.fje.dam2;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.WindowMetrics;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Classe que implementa una animació seqüencial
 * @author sergi.grau@fje.edu
 * @version 6.0 27.01.2026
 */
public class M11_AnimacioRequadre extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m11_animacio_requadre);

        final Button boto = findViewById(R.id.boto);

        WindowMetrics metrics = getWindowManager().getCurrentWindowMetrics();
        Rect bounds = metrics.getBounds();
        final int alcada = bounds.height();
        final int amplada = bounds.width();

        boto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ObjectAnimator animacio1 =
                        ObjectAnimator.ofFloat(boto, "x", 0, amplada-boto.getWidth());
                animacio1.setDuration(1000);
                ObjectAnimator animacio2 =
                        ObjectAnimator.ofFloat(boto, "y", 0, alcada-boto.getHeight());
                animacio1.setDuration(1000);
                ObjectAnimator animacio3 =
                        ObjectAnimator.ofFloat(boto, "x",  amplada-boto.getWidth(),0);
                animacio1.setDuration(1000);
                ObjectAnimator animacio4 =
                        ObjectAnimator.ofFloat(boto, "y", alcada-boto.getHeight(),0);
                animacio1.setDuration(1000);

                final AnimatorSet animacio = new AnimatorSet();
                animacio.playSequentially(animacio1, animacio2, animacio3, animacio4);

                animacio.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator a) {
                        super.onAnimationEnd(a);
                        animacio.start();
                    }

                });
                animacio.start();
            }
        });
    }
}
