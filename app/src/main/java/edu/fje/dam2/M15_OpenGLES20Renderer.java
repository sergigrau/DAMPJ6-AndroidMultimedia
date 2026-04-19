package edu.fje.dam2;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

/**
 * Aquesta interfície defineix els mètodes necessaris per dibuixar gràfics en un
 * GLSurfaceView . s'ha de proporcionar una implementació d'aquesta interfície
 * com una classe separada i adjuntar-la a la instància GLSurfaceView utilitzant
 * GLSurfaceView.setRenderer() . Classe que subministra la possibilitat de
 * treballar amb l'objecte GLSurfaceView object. Sobreescriu els mètodes de
 * cicle de vida de OpenGLES:
 * <ul>
 * <li>{@link GLSurfaceView.Renderer#onSurfaceCreated}</li>
 * <li>{@link GLSurfaceView.Renderer#onDrawFrame}</li>
 * <li>{@link GLSurfaceView.Renderer#onSurfaceChanged}</li>
 * </ul>
 */
public class M15_OpenGLES20Renderer implements GLSurfaceView.Renderer {

	private static final String TAG = "OpenGLES20Renderer";
	private M15_OpenGLES20Triangle mTriangle;
	private M15_OpenGLES20Quadrat mQuadrat;

	// mMVPMatrix és una abreviatura de "Model View Projection Matrix"
	private final float[] mMVPMatrix = new float[16];
	private final float[] mMatriuProjeccio = new float[16];
	private final float[] mMatriuVista = new float[16];
	private final float[] mMatriuRotacio = new float[16];

	private float angle;

	@Override
	public void onSurfaceCreated(GL10 unused, EGLConfig config) {

		// assignem el color de fons
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		mTriangle = new M15_OpenGLES20Triangle();
		mQuadrat = new M15_OpenGLES20Quadrat();
	}

	@Override
    public void onDrawFrame(GL10 unused) {
        float[] scratch = new float[16];

        // dibuixem el color de fons
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // assignem la posició de la càmera (matriu Vista)
        Matrix.setLookAtM(mMatriuVista, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // calculem la transformació de la projecció i la vista
        Matrix.multiplyMM(mMVPMatrix, 0, mMatriuProjeccio, 0, mMatriuVista, 0);

        // dribuixem el quadrat
        mQuadrat.dibuixar(mMVPMatrix);

        // Crea una rotació per al triangle 

        // Utilitzeu el codi següent per generar la rotació constant. 
        // Escriure el codi quan usant TouchEvents. 
        // long time = SystemClock.uptimeMillis() % 4000L;
        // float angle = 0.090f * ((int) time);

        Matrix.setRotateM(mMatriuRotacio, 0, angle, 0, 0, 1.0f);

        // Combina la matriu de rotació amb la de projecció i amb la vista de la càmera
        // L'ordre del producte de les matrius és molt important
        Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mMatriuRotacio, 0);

        // dibuxa el triangle
        mTriangle.dibuixar(scratch);
    }

	@Override
	public void onSurfaceChanged(GL10 unused, int width, int height) {
		// ajusta el viewport
		GLES20.glViewport(0, 0, width, height);

		float ratio = (float) width / height;

		// aquesta projecció s'aplica a les coordenades en el mètode onDraw()
		Matrix.frustumM(mMatriuProjeccio, 0, -ratio, ratio, -1, 1, 3, 7);

	}

	/**
	 * mètode d'utilitat per crear el shader
	 * 
	
	 * @param tipus          
	 * @param codiShader          
	 * @return - retorna un id per al shader
	 */
	public static int loadShader(int tipus, String codiShader) {

		//crea un vertex del tipus (GLES20.GL_VERTEX_SHADER)
		//o del tipus (GLES20.GL_FRAGMENT_SHADER)
		int shader = GLES20.glCreateShader(tipus);

		// afegeix el codi al shader i el compila
		GLES20.glShaderSource(shader, codiShader);
		GLES20.glCompileShader(shader);

		return shader;
	}
	//per depurar
	public static void checkGlError(String glOperation) {
		int error;
		while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
			Log.e(TAG, glOperation + ": glError " + error);
			throw new RuntimeException(glOperation + ": glError " + error);
		}
	}

	
	public float getAngle() {
		return angle;
	}

	
	public void setAngle(float a) {
		angle = a;
	}

}