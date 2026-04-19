package edu.fje.dam2;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

/**
 * Classe principal que mostra el funcionament d'OpenGLES 2.0
 *
 * @author sergi.grau@fje.edu
 * @version 5.0 27.01.2020
 * 
 */
public class M15_OpenGLES20Activity extends Activity {

	private GLSurfaceView mGLView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Aquesta classe és una View on es pot dibuixar i manipular
		// objectes mitjançant crides a l'API d'OpenGL i és similar en funció a
		// un SurfaceView
		mGLView = new M15_OpenGLES20GLSurfaceView(this);
		setContentView(mGLView);
	}

	@Override
	protected void onPause() {
		super.onPause();
		// La següent crida pausa el subprocés de representació.
		// Si l'aplicació OpenGL requereix molta memòria,
		// cal tenir en compte la de-assignació dels objectes
		mGLView.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// La següent crida pausa el subprocés de representació.
		// Si l'aplicació OpenGL requereix molta memòria,
		// cal tenir en compte la de-assignació dels objectes
		mGLView.onResume();
	}
}