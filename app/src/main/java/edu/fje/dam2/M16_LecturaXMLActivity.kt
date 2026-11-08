package edu.fje.dam2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.fje.dam2.databinding.M16LecturaXmlBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Activitat que llegeix un XML contingut en l'aplicació en segon pla
 * utilitzant Kotlin Coroutines i el mostra en un Jetpack RecyclerView amb ListAdapter.
 *
 * @author sergi.grau@fje.edu
 * @version 7.0 (API 36) 2026
 */
class M16_LecturaXMLActivity : AppCompatActivity() {

    private lateinit var binding: M16LecturaXmlBinding
    private lateinit var adapter: AlumneAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = M16LecturaXmlBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = AlumneAdapter { alumne ->
            val actualList = adapter.currentList.toMutableList()
            actualList.remove(alumne)
            adapter.submitList(actualList)
        }

        binding.llistaXML.layoutManager = LinearLayoutManager(this)
        binding.llistaXML.adapter = adapter

        carregarXmlCorrutina()
    }

    private fun carregarXmlCorrutina() {
        lifecycleScope.launch(Dispatchers.IO) {
            val lecturaXML = M16_LecturaXMLUtility()
            val alumnes = try {
                assets.open("m16_curs.xml").use { inputStream ->
                    lecturaXML.analitzarXML(inputStream)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }

            val llistaNoms = alumnes.map { it.toString() }

            withContext(Dispatchers.Main) {
                adapter.submitList(llistaNoms)
            }
        }
    }

    private class AlumneAdapter(
        private val onItemClick: (String) -> Unit
    ) : ListAdapter<String, AlumneAdapter.AlumneViewHolder>(AlumneDiffCallback) {

        class AlumneViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlumneViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(android.R.layout.simple_list_item_1, parent, false) as TextView
            return AlumneViewHolder(view)
        }

        override fun onBindViewHolder(holder: AlumneViewHolder, position: Int) {
            val item = getItem(position)
            holder.textView.text = item
            holder.textView.setOnClickListener { onItemClick(item) }
        }
    }

    private object AlumneDiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem
        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem
    }
}
