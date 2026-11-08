package edu.fje.dam2

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.createBitmap
import edu.fje.dam2.databinding.M05CanvasBasicPrincipalBinding

/**
 * Classe que demostra el funcionament de Canvas amb utilitats Graphics KTX de Jetpack Core.
 * Dibuixa directament sobre un Bitmap i ho mostra amb ViewBinding.
 *
 * @author sergi.grau@fje.edu
 * @version 7.0 (API 36) 2026
 */
class M05_CanvasBasic : AppCompatActivity() {

    private lateinit var binding: M05CanvasBasicPrincipalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = M05CanvasBasicPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val bitmap = createBitmap(100, 100)
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
        binding.contingut.imatge.setImageBitmap(bitmap)
    }
}
