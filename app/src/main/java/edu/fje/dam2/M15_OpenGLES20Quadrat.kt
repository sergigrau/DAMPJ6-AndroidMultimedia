package edu.fje.dam2

import android.opengl.GLES20
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

/**
 * Classe que encapsula un quadrat per a OpenGL ES 2.0.
 *
 * @author sergi.grau@fje.edu
 * @version 5.0 27.01.2020
 */
class M15_OpenGLES20Quadrat {

    companion object {
        const val COORDS_PER_VERTEX = 3
        val coordenades = floatArrayOf(
            -0.5f,  0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
             0.5f, -0.5f, 0.0f,
             0.5f,  0.5f, 0.0f
        )
    }

    private val codiShaderVertex =
        "uniform mat4 uMVPMatrix;" +
        "attribute vec4 vPosition;" +
        "void main() { gl_Position = uMVPMatrix * vPosition; }"

    private val codiShaderFragment =
        "precision mediump float;" +
        "uniform vec4 vColor;" +
        "void main() { gl_FragColor = vColor; }"

    private val vertexBuffer: FloatBuffer
    private val drawListBuffer: ShortBuffer
    private val mPrograma: Int
    private var mGestorPosicio = 0
    private var mGestorColor = 0
    private var mGestorMatriuMVP = 0

    private val ordreDibuix = shortArrayOf(0, 1, 2, 0, 2, 3)
    private val dadesPerVertex = COORDS_PER_VERTEX * 4
    val color = floatArrayOf(1f, 0.709803922f, 0.898039216f, 1.0f)

    init {
        vertexBuffer = ByteBuffer.allocateDirect(coordenades.size * 4)
            .order(ByteOrder.nativeOrder()).asFloatBuffer()
            .apply { put(coordenades); position(0) }

        drawListBuffer = ByteBuffer.allocateDirect(ordreDibuix.size * 2)
            .order(ByteOrder.nativeOrder()).asShortBuffer()
            .apply { put(ordreDibuix); position(0) }

        val vertexShader = M15_OpenGLES20Renderer.loadShader(GLES20.GL_VERTEX_SHADER, codiShaderVertex)
        val fragmentShader = M15_OpenGLES20Renderer.loadShader(GLES20.GL_FRAGMENT_SHADER, codiShaderFragment)

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
        GLES20.glVertexAttribPointer(mGestorPosicio, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, dadesPerVertex, vertexBuffer)
        mGestorColor = GLES20.glGetUniformLocation(mPrograma, "vColor")
        GLES20.glUniform4fv(mGestorColor, 1, color, 0)
        mGestorMatriuMVP = GLES20.glGetUniformLocation(mPrograma, "uMVPMatrix")
        M15_OpenGLES20Renderer.checkGlError("glGetUniformLocation")
        GLES20.glUniformMatrix4fv(mGestorMatriuMVP, 1, false, mvpMatrix, 0)
        M15_OpenGLES20Renderer.checkGlError("glUniformMatrix4fv")
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, ordreDibuix.size, GLES20.GL_UNSIGNED_SHORT, drawListBuffer)
        GLES20.glDisableVertexAttribArray(mGestorPosicio)
    }
}

