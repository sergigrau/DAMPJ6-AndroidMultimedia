package edu.fje.dam2

import android.app.Activity
import android.opengl.GLSurfaceView
import android.os.Bundle

/**
 * Classe principal que mostra el funcionament d'OpenGLES 2.0.
 *
 * @author sergi.grau@fje.edu
 * @version 5.0 27.01.2020
 */
class M15_OpenGLES20Activity : Activity() {

    private lateinit var mGLView: GLSurfaceView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mGLView = M15_OpenGLES20GLSurfaceView(this)
        setContentView(mGLView)
    }

    override fun onPause() {
        super.onPause()
        mGLView.onPause()
    }

    override fun onResume() {
        super.onResume()
        mGLView.onResume()
    }
}

