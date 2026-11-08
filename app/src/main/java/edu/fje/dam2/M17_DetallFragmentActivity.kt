package edu.fje.dam2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment

/**
 * Fragment que mostra el detall i permet afegir noms.
 *
 * @author sergi.grau@fje.edu
 * @version 6.0 (API 36) 19.04.2026
 */
class M17_DetallFragmentActivity : Fragment() {

    private lateinit var botoEntrada: Button
    private lateinit var textEntrada: EditText
    private var callback: OnNomAfegitListener? = null

    interface OnNomAfegitListener {
        fun onNomAfegit(nom: String)
    }

    fun setOnNomAfegitListener(callback: OnNomAfegitListener) {
        this.callback = callback
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.m17_fragment_detall, container, false)
        botoEntrada = view.findViewById(R.id.botoEntrada)
        textEntrada = view.findViewById(R.id.textEntrada)
        botoEntrada.setOnClickListener {
            callback?.onNomAfegit(textEntrada.text.toString())
        }
        return view
    }
}
