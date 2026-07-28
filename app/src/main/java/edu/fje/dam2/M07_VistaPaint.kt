package edu.fje.dam2

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.EmbossMaskFilter
import android.graphics.MaskFilter
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View

/**
 * Classe Paint - Vista on es pot pintar.
 *
 * @author sergi.grau@fje.edu
 * @version 6.0 27.01.2026
 */
class M07_VistaPaint @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    companion object {
        var MIDA_PINZELL = 20
        const val COLOR_PER_DEFECTE = Color.RED
        const val COLOR_FONS_PER_DEFECTE = Color.WHITE
        private const val TOLERANCIA_TACTE = 4f
    }

    private var xAnterior = 0f
    private var yAnterior = 0f
    private var rutaActual: Path? = null
    private val pintura: Paint = Paint().apply {
        isAntiAlias = true
        isDither = true
        color = COLOR_PER_DEFECTE
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        xfermode = null
        alpha = 0xff
    }
    private val tracos = ArrayList<M07_PathDit>()
    private var colorActual = COLOR_PER_DEFECTE
    private var colorFons = COLOR_FONS_PER_DEFECTE
    private var ampladaTrac = 0
    private var relleu = false
    private var difumina = false
    private val filtroRelleu: MaskFilter = EmbossMaskFilter(floatArrayOf(1f, 1f, 1f), 0.4f, 6f, 3.5f)
    private val filtroDifumina: MaskFilter = BlurMaskFilter(5f, BlurMaskFilter.Blur.NORMAL)
    private var bitmap: Bitmap? = null
    private var canvasBitmap: Canvas? = null
    private val pinturaBitmap = Paint(Paint.DITHER_FLAG)

    fun init(metrics: DisplayMetrics) {
        bitmap = Bitmap.createBitmap(metrics.widthPixels, metrics.heightPixels, Bitmap.Config.ARGB_8888)
        canvasBitmap = Canvas(bitmap!!)
        colorActual = COLOR_PER_DEFECTE
        ampladaTrac = MIDA_PINZELL
    }

    fun normal() { relleu = false; difumina = false }
    fun posaRelleu() { relleu = true; difumina = false }
    fun posaDifumina() { relleu = false; difumina = true }

    fun neteja() {
        colorFons = COLOR_FONS_PER_DEFECTE
        tracos.clear()
        normal()
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.save()
        canvasBitmap?.drawColor(colorFons)
        for (fp in tracos) {
            pintura.color = fp.color
            pintura.strokeWidth = fp.ampladaTrac.toFloat()
            pintura.maskFilter = null
            if (fp.relleu) pintura.maskFilter = filtroRelleu
            else if (fp.difumina) pintura.maskFilter = filtroDifumina
            canvasBitmap?.drawPath(fp.ruta, pintura)
        }
        bitmap?.let { canvas.drawBitmap(it, 0f, 0f, pinturaBitmap) }
        canvas.restore()
    }

    private fun touchStart(x: Float, y: Float) {
        rutaActual = Path()
        tracos.add(M07_PathDit(colorActual, relleu, difumina, ampladaTrac, rutaActual!!))
        rutaActual!!.reset()
        rutaActual!!.moveTo(x, y)
        xAnterior = x
        yAnterior = y
    }

    private fun touchMove(x: Float, y: Float) {
        val dx = Math.abs(x - xAnterior)
        val dy = Math.abs(y - yAnterior)
        if (dx >= TOLERANCIA_TACTE || dy >= TOLERANCIA_TACTE) {
            rutaActual?.quadTo(xAnterior, yAnterior, (x + xAnterior) / 2, (y + yAnterior) / 2)
            xAnterior = x
            yAnterior = y
        }
    }

    private fun touchUp() {
        rutaActual?.lineTo(xAnterior, yAnterior)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> { touchStart(x, y); invalidate() }
            MotionEvent.ACTION_MOVE -> { touchMove(x, y); invalidate() }
            MotionEvent.ACTION_UP  -> { touchUp(); invalidate() }
        }
        return true
    }
}

