package edu.fje.dam2;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

/**
 * Aquesta classe actua com un recipient de vista on els gràfics OpenGL ÉS es
 * poden dibuixar a la pantalla. Aquesta vista també es pot utilitzar per
 * capturar esdeveniments de toc.
 * 
 * @author sergi grau
 * @version 5.0 27.01.2020
 * 
 */
public class M15_OpenGLES20GLSurfaceView extends GLSurfaceView {

	private final M15_OpenGLES20Renderer renderer;

	public M15_OpenGLES20GLSurfaceView(Context context) {
		super(context);

		// crea un context OpenGL ES 2.0 .
		setEGLContextClientVersion(2);

		// assigna el objecte Renderer per dibuixar sobre GLSurfaceView
		renderer = new M15_OpenGLES20Renderer();
		setRenderer(renderer);

		// Render la vista només quan canvien les dades
		setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
	}

	private final float TOUCH_ESCALA_FACTOR = 180.0f / 320;
	private float previaX;
	private float previaY;

	@Override
	public boolean onTouchEvent(MotionEvent e) {
	

		float x = e.getX();
		float y = e.getY();

		switch (e.getAction()) {
		case MotionEvent.ACTION_MOVE:

			float dx = x - previaX;
			float dy = y - previaY;

			// direcció contraria de rotació a la dreta del mig de la linia
			if (y > getHeight() / 2) {
				dx = dx * -1;
			}

			// direcció contraria de rotació a la esquerra del mig de la linia
			if (x < getWidth() / 2) {
				dy = dy * -1;
			}

			renderer.setAngle(renderer.getAngle()
					+ ((dx + dy) * TOUCH_ESCALA_FACTOR)); // = 180.0f / 320
			requestRender();
		}

		previaX = x;
		previaY = y;
		return true;
	}

}
