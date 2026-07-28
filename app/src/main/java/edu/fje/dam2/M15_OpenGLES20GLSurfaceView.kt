package edu.fje.dam2

import android.content.Context
import android.opengl.GLSurfaceView
import android.view.MotionEvent

/**
 * Vista recipient per als gràfics OpenGL ES.
 *
 * @author sergi.grau@fje.edu
 * @version 5.0 27.01.2020
 */
class M15_OpenGLES20GLSurfaceView(context: Context) : GLSurfaceView(context) {

    private val renderer: M15_OpenGLES20Renderer

    init {
        setEGLContextClientVersion(2)
        renderer = M15_OpenGLES20Renderer()
        setRenderer(renderer)
        renderMode = RENDERMODE_WHEN_DIRTY
    }

    private val TOUCH_ESCALA_FACTOR = 180.0f / 320
    private var previaX = 0f
    private var previaY = 0f

    override fun onTouchEvent(e: MotionEvent): Boolean {
        val x = e.x
        val y = e.y

        if (e.action == MotionEvent.ACTION_MOVE) {
            var dx = x - previaX
            var dy = y - previaY
            if (y > height / 2) dx *= -1
            if (x < width / 2) dy *= -1
            renderer.setAngle(renderer.getAngle() + (dx + dy) * TOUCH_ESCALA_FACTOR)
            requestRender()
        }

        previaX = x
        previaY = y
        return true
    }
}

