package edu.fje.dam2

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 * Classe que demostra el funcionament de Canvas. Dibuixa directament sobre una View.
 *
 * @author sergi.grau@fje.edu
 * @version 6.0 27.01.2026
 */
class M06_VistaPropia(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var xPos: Float = 0f
    private var yPos: Float = 0f
    var cadena: String? = null
    private var color: Int = 0

    init {
        val a: TypedArray = context.theme.obtainStyledAttributes(
            attrs, R.styleable.M06_VistaPropia, 0, 0
        )
        try {
            xPos = a.getFloat(R.styleable.M06_VistaPropia_x, 0f)
            cadena = a.getString(R.styleable.M06_VistaPropia_cadena)
        } finally {
            a.recycle()
        }
    }

    override fun onDraw(canvas: Canvas) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = color
        canvas.drawRect(xPos, yPos, 450f, 600f, paint)

        paint.color = Color.BLACK
        paint.textSize = 60f
        canvas.drawText(cadena ?: "", 40f, 250f, paint)
    }

    override fun getX(): Float = xPos
    override fun setX(x: Float) { xPos = x }
    override fun getY(): Float = yPos
    override fun setY(y: Float) { yPos = y }

    fun getColor(): Int = color
    fun setColor(c: Int) { color = c }
}

