package edu.fje.dam2

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import edu.fje.dam2.databinding.M20AnimacioSpriteBinding

/**
 * Classe que demostra el funcionament de les animacions Sprite
 * amb ViewBinding i Jetpack Lifecycle.
 *
 * @author sergi.grau@fje.edu
 * @version 7.0 (API 36) 2026
 */
class M20_AnimacioSprite : AppCompatActivity(), DefaultLifecycleObserver {

    private lateinit var binding: M20AnimacioSpriteBinding
    private lateinit var animationDrawable: AnimationDrawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super<AppCompatActivity>.onCreate(savedInstanceState)
        binding = M20AnimacioSpriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.spriteImatge.setBackgroundResource(R.drawable.sprite)
        animationDrawable = binding.spriteImatge.background as AnimationDrawable

        lifecycle.addObserver(this)
    }

    override fun onStart(owner: LifecycleOwner) {
        super<DefaultLifecycleObserver>.onStart(owner)
        animationDrawable.start()
    }

    override fun onStop(owner: LifecycleOwner) {
        super<DefaultLifecycleObserver>.onStop(owner)
        animationDrawable.stop()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            if (animationDrawable.isRunning) animationDrawable.stop()
            else animationDrawable.start()
            return true
        }
        return super.onTouchEvent(event)
    }
}
