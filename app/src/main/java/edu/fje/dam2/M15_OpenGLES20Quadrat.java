
package edu.fje.dam2;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.opengl.GLES20;
/**
 * Classe que encapsula un quadrat
 * @author sergi grau
 * @version 5.0 27.01.2020
 *
 */
public class M15_OpenGLES20Quadrat {

    private final String codiShaderVertex =
            
            "uniform mat4 uMVPMatrix;" +
            "attribute vec4 vPosition;" +
            "void main() {" + 
            "  gl_Position = uMVPMatrix * vPosition;" +
            "}";

    private final String codiShaderFragment =
            "precision mediump float;" +
            "uniform vec4 vColor;" +
            "void main() {" +
            "  gl_FragColor = vColor;" +
            "}";

    private final FloatBuffer vertexBuffer;
    private final ShortBuffer drawListBuffer;
    private final int mPrograma;
    private int mGestorPosicio;
    private int mGestorColor;
    private int mGestorMatriuMVP;

    static final int COORDS_PER_VERTEX = 3;
    static float coordenades[] = {
            -0.5f,  0.5f, 0.0f,   // top left
            -0.5f, -0.5f, 0.0f,   // bottom left
             0.5f, -0.5f, 0.0f,   // bottom right
             0.5f,  0.5f, 0.0f }; // top right

    private final short ordreDibuix[] = { 0, 1, 2, 0, 2, 3 }; // ordre de dibuix

    private final int dadesPerVertex = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    float color[] = { 1f, 0.709803922f, 0.898039216f, 1.0f };

    
    public M15_OpenGLES20Quadrat() {
        ByteBuffer bb = ByteBuffer.allocateDirect(
                coordenades.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(coordenades);
        vertexBuffer.position(0);

        ByteBuffer dlb = ByteBuffer.allocateDirect(
                ordreDibuix.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(ordreDibuix);
        drawListBuffer.position(0);

        int vertexShader = M15_OpenGLES20Renderer.loadShader(
                GLES20.GL_VERTEX_SHADER,
                codiShaderVertex);
        int fragmentShader = M15_OpenGLES20Renderer.loadShader(
                GLES20.GL_FRAGMENT_SHADER,
                codiShaderFragment);

        mPrograma = GLES20.glCreateProgram();             // crea un programa openGL buit
        GLES20.glAttachShader(mPrograma, vertexShader);   // afegeix els vertex
        GLES20.glAttachShader(mPrograma, fragmentShader); // afegeix els fragments
        GLES20.glLinkProgram(mPrograma);                  // crea els executables OpenGL 
    }

   
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
                dadesPerVertex, vertexBuffer);

       
        mGestorColor = GLES20.glGetUniformLocation(mPrograma, "vColor");

        // assignem el color al quadrat
        GLES20.glUniform4fv(mGestorColor, 1, color, 0);

        // obtenim el gestor de la matriu de transformació de la forma
        mGestorMatriuMVP = GLES20.glGetUniformLocation(mPrograma, "uMVPMatrix");
        M15_OpenGLES20Renderer.checkGlError("glGetUniformLocation");

        // aplica les transformacions
        GLES20.glUniformMatrix4fv(mGestorMatriuMVP, 1, false, mvpMatrix, 0);
        M15_OpenGLES20Renderer.checkGlError("glUniformMatrix4fv");

        // dibuixa el quadrat
        GLES20.glDrawElements(
                GLES20.GL_TRIANGLES, ordreDibuix.length,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // desactiva el vertex array
        GLES20.glDisableVertexAttribArray(mGestorPosicio);
    }

}