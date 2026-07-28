package edu.fje.dam2

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.activity.EdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/**
 * Activity que utilitza un servei per a calcular la suma de dos nombres.
 *
 * @author sergi.grau@fje.edu
 * @version 6.0 (API 33) 19.04.2026
 */
class M21_SumaServeiActivity : AppCompatActivity() {

    private var intent: Intent? = null
    private lateinit var n1: EditText
    private lateinit var n2: EditText
    private lateinit var resultat: TextView
    private lateinit var resultatReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EdgeToEdge.enable(this)
        setContentView(R.layout.m21_activity_suma_servei)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars: Insets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        n1 = findViewById(R.id.numero1)
        n2 = findViewById(R.id.numero2)
        resultat = findViewById(R.id.resultat)

        findViewById<android.view.View>(R.id.botoSuma).setOnClickListener {
            intent = Intent(this, M21_SumaService::class.java).apply {
                putExtra("n1", n1.text.toString().toInt())
                putExtra("n2", n2.text.toString().toInt())
            }
            startService(intent)
        }

        resultatReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                Log.v("DAM2", "dades rebudes")
                val res = intent.getIntExtra("suma", 0)
                resultat.text = res.toString()
            }
        }

        registerReceiver(resultatReceiver, IntentFilter("edu.fje.smx8.SUMA_RESULTAT"), Context.RECEIVER_EXPORTED)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(resultatReceiver)
    }
}

