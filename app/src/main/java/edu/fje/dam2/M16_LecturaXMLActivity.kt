package edu.fje.dam2

import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.util.HashMap

/**
 * Classe que llegeix un XML contingut en l'aplicació i mostra una ListView.
 *
 * @author sergi.grau@fje.edu
 * @version 6.0 (API 33) 19.04.2026
 */
class M16_LecturaXMLActivity : AppCompatActivity() {

    private lateinit var llistaNoms: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.m16_lectura_xml)
        val llista: ListView = findViewById(R.id.llistaXML)

        val lecturaXML = M16_LecturaXMLUtility()
        var llistaAlumnes: List<M16_LecturaXMLUtility.Alumne>? = null
        try {
            val inputStream = assets.open("m16_curs.xml")
            llistaAlumnes = lecturaXML.analitzarXML(inputStream)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        }

        llistaNoms = ArrayList()
        llistaAlumnes?.forEach { llistaNoms.add(it.toString()) }

        val adapter = ArrayAdaptarEstable(this, android.R.layout.simple_list_item_1, llistaNoms)
        llista.adapter = adapter

        llista.setOnItemClickListener { parent, view, position, _ ->
            val item = parent.getItemAtPosition(position) as String
            view.animate().setDuration(2000).alpha(0f).withEndAction {
                llistaNoms.remove(item)
                adapter.notifyDataSetChanged()
                view.alpha = 1f
            }
        }
    }

    private class ArrayAdaptarEstable(
        context: Context,
        textViewResourceId: Int,
        objects: List<String>
    ) : ArrayAdapter<String>(context, textViewResourceId, objects) {

        private val mIdMap = HashMap<String, Int>()

        init {
            objects.forEachIndexed { i, s -> mIdMap[s] = i }
        }

        override fun getItemId(position: Int): Long = mIdMap[getItem(position)]?.toLong() ?: 0L
        override fun hasStableIds(): Boolean = true
    }
}

