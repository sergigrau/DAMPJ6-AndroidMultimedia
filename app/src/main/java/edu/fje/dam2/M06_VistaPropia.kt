package edu.fje.dam2

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes

/**
 * Custom View utilitzant les extensions de Jetpack Core KTX (withStyledAttributes).
 *
 * @author sergi.grau@fje.edu
 * @version 7.0 (API 36) 2026
 */
class M06_VistaPropia(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var xPos: Float = 0f
    private var yPos: Float = 0f
    var cadena: String? = null
    private var color: Int = Color.RED

    init {
        context.withStyledAttributes(attrs, R.styleable.M06_VistaPropia) {
            xPos = getFloat(R.styleable.M06_VistaPropia_x, 0f)
            cadena = getString(R.styleable.M06_VistaPropia_cadena)
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
