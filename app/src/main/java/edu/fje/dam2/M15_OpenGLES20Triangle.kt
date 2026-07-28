package edu.fje.dam2

import android.opengl.GLES20
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

/**
 * Classe que encapsula un triangle per a OpenGL ES 2.0.
 *
 * @author sergi.grau@fje.edu
 * @version 5.0 27.01.2020
 */
class M15_OpenGLES20Triangle {

    companion object {
        const val COORDS_PER_VERTEX = 3
        val triangleCoords = floatArrayOf(
            0.0f,  0.622008459f, 0.0f,
           -0.5f, -0.311004243f, 0.0f,
            0.5f, -0.311004243f, 0.0f
        )
    }

    private val vertexShaderCode =
        "uniform mat4 uMVPMatrix;" +
        "attribute vec4 vPosition;" +
        "void main() { gl_Position = uMVPMatrix * vPosition; }"

    private val fragmentShaderCode =
        "precision mediump float;" +
        "uniform vec4 vColor;" +
        "void main() { gl_FragColor = vColor; }"

    private val vertexBuffer: FloatBuffer
    private val mPrograma: Int
    private var mGestorPosicio = 0
    private var mGestorColor = 0
    private var mGestorMatriuMVP = 0

    private val vertexCount = triangleCoords.size / COORDS_PER_VERTEX
    private val vertexStride = COORDS_PER_VERTEX * 4
    val color = floatArrayOf(0.63671875f, 0.76953125f, 0.22265625f, 0.0f)

    init {
        vertexBuffer = ByteBuffer.allocateDirect(triangleCoords.size * 4)
            .order(ByteOrder.nativeOrder()).asFloatBuffer()
            .apply { put(triangleCoords); position(0) }

        val vertexShader = M15_OpenGLES20Renderer.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader = M15_OpenGLES20Renderer.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)

        mPrograma = GLES20.glCreateProgram().also {
            GLES20.glAttachShader(it, vertexShader)
            GLES20.glAttachShader(it, fragmentShader)
            GLES20.glLinkProgram(it)
        }
    }

    fun dibuixar(mvpMatrix: FloatArray) {
        GLES20.glUseProgram(mPrograma)
        mGestorPosicio = GLES20.glGetAttribLocation(mPrograma, "vPosition")
        GLES20.glEnableVertexAttribArray(mGestorPosicio)
        GLES20.glVertexAttribPointer(mGestorPosicio, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer)
        mGestorColor = GLES20.glGetUniformLocation(mPrograma, "vColor")
        GLES20.glUniform4fv(mGestorColor, 1, color, 0)
        mGestorMatriuMVP = GLES20.glGetUniformLocation(mPrograma, "uMVPMatrix")
        M15_OpenGLES20Renderer.checkGlError("glGetUniformLocation")
        GLES20.glUniformMatrix4fv(mGestorMatriuMVP, 1, false, mvpMatrix, 0)
        M15_OpenGLES20Renderer.checkGlError("glUniformMatrix4fv")
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount)
        GLES20.glDisableVertexAttribArray(mGestorPosicio)
    }
}

