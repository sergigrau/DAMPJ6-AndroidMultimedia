package edu.fje.dam2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
/**
 * Classe Paint
 * Classe que representa la vista on es pot pintar
 *
 * @author sergi.grau@fje.edu
 * @version 6.0 27.01.2026

 */

public class M07_VistaPaint extends View {

    public static int MIDA_PINZELL = 20;
    public static final int COLOR_PER_DEFECTE = Color.RED;
    public static final int COLOR_FONS_PER_DEFECTE = Color.WHITE;
    private static final float TOLERANCIA_TACTE = 4;
    private float xAnterior, yAnterior;
    private Path rutaActual;
    private final Paint pintura;
    private final ArrayList<M07_PathDit> tracos = new ArrayList<>();
    private int colorActual;
    private int colorFons = COLOR_FONS_PER_DEFECTE;
    private int ampladaTrac;
    private boolean relleu;
    private boolean difumina;
    private final MaskFilter filtroRelleu;
    private final MaskFilter filtroDifumina;
    private Bitmap bitmap;
    private Canvas canvasBitmap;
    private final Paint pinturaBitmap = new Paint(Paint.DITHER_FLAG);

    public M07_VistaPaint(Context context) {
        this(context, null);
    }

    public M07_VistaPaint(Context context, AttributeSet attrs) {
        super(context, attrs);
        pintura = new Paint();
        pintura.setAntiAlias(true);
        pintura.setDither(true);
        pintura.setColor(COLOR_PER_DEFECTE);
        pintura.setStyle(Paint.Style.STROKE);
        pintura.setStrokeJoin(Paint.Join.ROUND);
        pintura.setStrokeCap(Paint.Cap.ROUND);
        pintura.setXfermode(null);
        pintura.setAlpha(0xff);

        filtroRelleu = new EmbossMaskFilter(new float[] {1, 1, 1}, 0.4f, 6, 3.5f);
        filtroDifumina = new BlurMaskFilter(5, BlurMaskFilter.Blur.NORMAL);
    }

    public void init(DisplayMetrics metrics) {
        int height = metrics.heightPixels;
        int width = metrics.widthPixels;

        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        canvasBitmap = new Canvas(bitmap);

        colorActual = COLOR_PER_DEFECTE;
        ampladaTrac = MIDA_PINZELL;
    }

    public void normal() {
        relleu = false;
        difumina = false;
    }

    public void posaRelleu() {
        relleu = true;
        difumina = false;
    }

    public void posaDifumina() {
        relleu = false;
        difumina = true;
    }

    public void neteja() {
        colorFons = COLOR_FONS_PER_DEFECTE;
        tracos.clear();
        normal();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.save();
        canvasBitmap.drawColor(colorFons);

        for (M07_PathDit fp : tracos) {
            pintura.setColor(fp.color);
            pintura.setStrokeWidth(fp.ampladaTrac);
            pintura.setMaskFilter(null);

            if (fp.relleu)
                pintura.setMaskFilter(filtroRelleu);
            else if (fp.difumina)
                pintura.setMaskFilter(filtroDifumina);

            canvasBitmap.drawPath(fp.ruta, pintura);
        }

        canvas.drawBitmap(bitmap, 0, 0, pinturaBitmap);
        canvas.restore();
    }

    private void touchStart(float x, float y) {
        rutaActual = new Path();
        M07_PathDit fp = new M07_PathDit(colorActual, relleu, difumina, ampladaTrac, rutaActual);
        tracos.add(fp);

        rutaActual.reset();
        rutaActual.moveTo(x, y);
        xAnterior = x;
        yAnterior = y;
    }

    private void touchMove(float x, float y) {
        float dx = Math.abs(x - xAnterior);
        float dy = Math.abs(y - yAnterior);

        if (dx >= TOLERANCIA_TACTE || dy >= TOLERANCIA_TACTE) {
            rutaActual.quadTo(xAnterior, yAnterior, (x + xAnterior) / 2, (y + yAnterior) / 2);
            xAnterior = x;
            yAnterior = y;
        }
    }

    private void touchUp() {
        rutaActual.lineTo(xAnterior, yAnterior);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN :
                touchStart(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE :
                touchMove(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP :
                touchUp();
                invalidate();
                break;
        }

        return true;
    }
}
