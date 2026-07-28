package edu.fje.dam2

import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.util.Log

/**
 * Renderer per a OpenGL ES 2.0.
 *
 * @author sergi.grau@fje.edu
 * @version 5.0 27.01.2020
 */
class M15_OpenGLES20Renderer : GLSurfaceView.Renderer {

    companion object {
        private const val TAG = "OpenGLES20Renderer"

        fun loadShader(tipus: Int, codiShader: String): Int {
            val shader = GLES20.glCreateShader(tipus)
            GLES20.glShaderSource(shader, codiShader)
            GLES20.glCompileShader(shader)
            return shader
        }

        fun checkGlError(glOperation: String) {
            var error: Int
            while (GLES20.glGetError().also { error = it } != GLES20.GL_NO_ERROR) {
                Log.e(TAG, "$glOperation: glError $error")
                throw RuntimeException("$glOperation: glError $error")
            }
        }
    }

    private lateinit var mTriangle: M15_OpenGLES20Triangle
    private lateinit var mQuadrat: M15_OpenGLES20Quadrat

    private val mMVPMatrix = FloatArray(16)
    private val mMatriuProjeccio = FloatArray(16)
    private val mMatriuVista = FloatArray(16)
    private val mMatriuRotacio = FloatArray(16)

    private var angle = 0f

    override fun onSurfaceCreated(unused: GL10, config: EGLConfig) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        mTriangle = M15_OpenGLES20Triangle()
        mQuadrat = M15_OpenGLES20Quadrat()
    }

    override fun onDrawFrame(unused: GL10) {
        val scratch = FloatArray(16)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
        Matrix.setLookAtM(mMatriuVista, 0, 0f, 0f, -3f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
        Matrix.multiplyMM(mMVPMatrix, 0, mMatriuProjeccio, 0, mMatriuVista, 0)
        mQuadrat.dibuixar(mMVPMatrix)
        Matrix.setRotateM(mMatriuRotacio, 0, angle, 0f, 0f, 1.0f)
        Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mMatriuRotacio, 0)
        mTriangle.dibuixar(scratch)
    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        val ratio = width.toFloat() / height
        Matrix.frustumM(mMatriuProjeccio, 0, -ratio, ratio, -1f, 1f, 3f, 7f)
    }

    fun getAngle(): Float = angle
    fun setAngle(a: Float) { angle = a }
}

