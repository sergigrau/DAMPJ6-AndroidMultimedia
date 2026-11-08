package edu.fje.dam2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment

/**
 * Fragment que mostra la llista de cursos.
 *
 * @author sergi.grau@fje.edu
 * @version 6.0 (API 36) 19.04.2026
 */
class M17_LlistaFragmentActivity : Fragment() {

    private lateinit var llista: ListView
    private lateinit var itemsAdapter: ArrayAdapter<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.m17_fragment_llista, container, false)
        llista = view.findViewById(R.id.llista)
        itemsAdapter = ArrayAdapter(view.context, android.R.layout.simple_list_item_1, M17_FragmentsActivity.CURSOS)
        llista.adapter = itemsAdapter
        return view
    }

    fun afegirNom(nom: String) {
        itemsAdapter.notifyDataSetChanged()
    }
}
