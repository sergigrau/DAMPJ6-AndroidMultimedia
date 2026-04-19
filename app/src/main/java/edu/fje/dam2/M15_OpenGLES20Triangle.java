
package edu.fje.dam2;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES20;

/**
 * Classe que encapsula un triangle
 * @author sergi.grau@fje.edu
 * @version 5.0 27.01.2020
 */
public class M15_OpenGLES20Triangle {

    private final String vertexShaderCode =
  
            "uniform mat4 uMVPMatrix;" +
            "attribute vec4 vPosition;" +
            "void main() {" +
            "  gl_Position = uMVPMatrix * vPosition;" +
            "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
            "uniform vec4 vColor;" +
            "void main() {" +
            "  gl_FragColor = vColor;" +
            "}";

    private final FloatBuffer vertexBuffer;
    private final int mPrograma;
    private int mGestorPosicio;
    private int mGestorColor;
    private int mGestorMatriuMVP;

    // nombre coordinades per  vertex
    static final int COORDS_PER_VERTEX = 3;
    static float triangleCoords[] = {
            // en sentit contrari a les agulles del rellotge
            0.0f,  0.622008459f, 0.0f,   // top
           -0.5f, -0.311004243f, 0.0f,   // bottom left
            0.5f, -0.311004243f, 0.0f    // bottom right
    };
    private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    float color[] = { 0.63671875f, 0.76953125f, 0.22265625f, 0.0f };

    
    public M15_OpenGLES20Triangle() {
        // inicialitza el buffer de vertex per desar les coordenades
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (num de coordenades * 4 bytes per float)
                triangleCoords.length * 4);
        // ordre de bytes per hardware
        bb.order(ByteOrder.nativeOrder());

        // creem un buffer de coma flotant
        vertexBuffer = bb.asFloatBuffer();
        // afegim les coordenades
        vertexBuffer.put(triangleCoords);
        // llegim la primera coordenada
        vertexBuffer.position(0);

        // prepara shaders i el programa OpenGL
        int vertexShader = M15_OpenGLES20Renderer.loadShader(
                GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = M15_OpenGLES20Renderer.loadShader(
                GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        mPrograma = GLES20.glCreateProgram();             // crea un programa opengl buit
        GLES20.glAttachShader(mPrograma, vertexShader);   // afegeix un vertex shader
        GLES20.glAttachShader(mPrograma, fragmentShader); // afegeix un fragment shader
        GLES20.glLinkProgram(mPrograma);                  // executa el programa

    }

    /**
     * Encapsula les instruccions OpenGL ES instructions per dibuixar aquesta forma
     *
     * @param mvpMatrix -  matriu Model View Project  en la que dibuixem la forma
     */
    public void dibuixar(float[] mvpMatrix) {
        // afegeix un programa a un entorn OpenGL
        GLES20.glUseProgram(mPrograma);

        // gestiona el shader de vertex mitjançant un vector de posició
        mGestorPosicio = GLES20.glGetAttribLocation(mPrograma, "vPosition");

        // gestiona els vertexs dels triangles 
        GLES20.glEnableVertexAttribArray(mGestorPosicio);

        // prepara les coordenades del triangke
        GLES20.glVertexAttribPointer(
                mGestorPosicio, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        // gestiona el color del fragment shader
        mGestorColor = GLES20.glGetUniformLocation(mPrograma, "vColor");

        // assigna el color per a dibuixar la forma
        GLES20.glUniform4fv(mGestorColor, 1, color, 0);

        // gestiona la matriu de transformació
        mGestorMatriuMVP = GLES20.glGetUniformLocation(mPrograma, "uMVPMatrix");
        M15_OpenGLES20Renderer.checkGlError("glGetUniformLocation");

        // aplica les transformacions de projecció i vista
        GLES20.glUniformMatrix4fv(mGestorMatriuMVP, 1, false, mvpMatrix, 0);
        M15_OpenGLES20Renderer.checkGlError("glUniformMatrix4fv");

        // dibuixa el triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

        // ddestruiem el array de vertex
        GLES20.glDisableVertexAttribArray(mGestorPosicio);
    }

}
