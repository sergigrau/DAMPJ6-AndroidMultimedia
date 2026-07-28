package edu.fje.dam2

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

/**
 * Classe que demostra el funcionament de Canvas.
 * Dibuixa directament sobre una View, utilitza un Bitmap com a suport de dibuix.
 *
 * @author sergi.grau@fje.edu
 * @version 6.0 27.01.2026
 */
class M05_CanvasBasic : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.m05_canvas_basic_principal)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val imageView: ImageView = findViewById(R.id.imatge)
        val bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.BLACK
        canvas.drawRect(
            (Math.random() * 100).toFloat(), (Math.random() * 100).toFloat(),
            (Math.random() * 100).toFloat(), (Math.random() * 100).toFloat(), paint
        )
        canvas.drawCircle(50f, 50f, 10f, paint)
        for (i in 0 until 100) {
            paint.color = Color.argb(
                (Math.random() * 255).toInt(), (Math.random() * 255).toInt(),
                (Math.random() * 255).toInt(), (Math.random() * 255).toInt()
            )
            canvas.drawRect(
                (Math.random() * 100).toFloat(), (Math.random() * 100).toFloat(),
                (Math.random() * 100).toFloat(), (Math.random() * 100).toFloat(), paint
            )
        }
        imageView.setImageBitmap(bitmap)
    }
}

