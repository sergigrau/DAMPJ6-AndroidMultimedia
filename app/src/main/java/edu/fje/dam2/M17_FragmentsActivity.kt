package edu.fje.dam2

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import edu.fje.dam2.databinding.M17FragmentsBinding

/**
 * Classe que demostra el funcionament dels fragments amb Jetpack Fragment KTX.
 *
 * @author sergi.grau@fje.edu
 * @version 7.0 (API 36) 2026
 */
class M17_FragmentsActivity : AppCompatActivity(), M17_DetallFragmentActivity.OnNomAfegitListener {

    companion object {
        val CURSOS = ArrayList<String>()
    }

    private lateinit var binding: M17FragmentsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = M17FragmentsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        if (CURSOS.isEmpty()) {
            CURSOS.add("DAW2")
            CURSOS.add("DAM2")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.m17_fragments, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onNomAfegit(nom: String) {
        CURSOS.add(nom)
        Log.v("DAM", CURSOS.toString())
        val llistaFragment = supportFragmentManager.findFragmentById(R.id.contenidorFragmentLlista) as? M17_LlistaFragmentActivity
        llistaFragment?.afegirNom(nom)
    }

    class FragmentInterna : Fragment() {
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.m17_fragment_detall2, container, false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.afegirLlista -> {
                supportFragmentManager.commit {
                    replace(R.id.contenidorFragmentLlista, M17_LlistaFragmentActivity())
                }
                true
            }
            R.id.afegirDetall -> {
                supportFragmentManager.commit {
                    replace(R.id.contenidorFragmentDetall, M17_DetallFragmentActivity())
                }
                true
            }
            R.id.canviarDetall -> {
                supportFragmentManager.commit {
                    replace(R.id.contenidorFragmentDetall, FragmentInterna())
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onConfigurationChanged(novaConfiguracio: Configuration) {
        super.onConfigurationChanged(novaConfiguracio)
        val lp = CoordinatorLayout.LayoutParams(
            CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT
        )
        binding.elLayout.layoutParams = lp
        when (novaConfiguracio.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show()
                binding.elLayout.orientation = android.widget.LinearLayout.HORIZONTAL
            }
            Configuration.ORIENTATION_PORTRAIT -> {
                Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show()
                binding.elLayout.orientation = android.widget.LinearLayout.VERTICAL
            }
        }
    }
}
