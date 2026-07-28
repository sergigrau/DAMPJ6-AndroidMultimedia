package edu.fje.dam2

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.MotionEvent
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

/**
 * Classe que demostra el funcionament de les animacions Sprite.
 *
 * @author sergi.grau@fje.edu
 * @version 6.0 (API 33) 19.04.2026
 */
class M20_AnimacioSprite : AppCompatActivity() {

    private lateinit var a: AnimationDrawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.m20_animacio_sprite)

        val imatge: ImageView = findViewById(R.id.spriteImatge)
        imatge.setBackgroundResource(R.drawable.sprite)
        a = imatge.background as AnimationDrawable
        a.start()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            a.stop()
            return true
        }
        return super.onTouchEvent(event)
    }
}

