package edu.fje.dam2

import android.opengl.GLSurfaceView
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

/**
 * Classe principal que mostra el funcionament d'OpenGLES 2.0
 * gestionat mitjançant DefaultLifecycleObserver de Jetpack Lifecycle.
 *
 * @author sergi.grau@fje.edu
 * @version 7.0 (API 36) 2026
 */
class M15_OpenGLES20Activity : AppCompatActivity(), DefaultLifecycleObserver {

    private lateinit var mGLView: GLSurfaceView

    override fun onCreate(savedInstanceState: Bundle?) {
        super<AppCompatActivity>.onCreate(savedInstanceState)
        mGLView = M15_OpenGLES20GLSurfaceView(this)
        setContentView(mGLView)

        lifecycle.addObserver(this)
    }

    override fun onResume(owner: LifecycleOwner) {
        super<DefaultLifecycleObserver>.onResume(owner)
        mGLView.onResume()
    }

    override fun onPause(owner: LifecycleOwner) {
        super<DefaultLifecycleObserver>.onPause(owner)
        mGLView.onPause()
    }
}
