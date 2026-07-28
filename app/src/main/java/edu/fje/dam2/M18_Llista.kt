package edu.fje.dam2

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

/**
 * Adaptador de llista per als artistes de Firebase.
 *
 * @author sergi.grau@fje.edu
 * @version 6.0 27.01.2026
 */
class M18_Llista(private val context: Activity, private val artistes: List<M18_Artista>) :
    ArrayAdapter<M18_Artista>(context, R.layout.m18_llista, artistes) {

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val listViewItem = context.layoutInflater.inflate(R.layout.m18_llista, null, true)
        val nom: TextView = listViewItem.findViewById(R.id.nom)
        val genere: TextView = listViewItem.findViewById(R.id.genere)
        val artist = artistes[position]
        nom.text = artist.nom
        genere.text = artist.genere
        return listViewItem
    }
}

