package edu.fje.dam2

import android.annotation.SuppressLint
import android.app.Fragment
import android.app.FragmentManager
import android.app.FragmentTransaction
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout

/**
 * Classe que demostra el funcionament dels fragments amb Android.
 *
 * @author sergi.grau@fje.edu
 * @version 6.0 (API 33) 19.04.2026
 */
@Suppress("DEPRECATION")
@SuppressLint("ValidFragment")
class M17_FragmentsActivity : AppCompatActivity(), M17_DetallFragmentActivity.OnNomAfegitListener {

    companion object {
        val CURSOS = ArrayList<String>()
    }

    private lateinit var ll: LinearLayout

    override fun onAttachFragment(fragment: Fragment) {
        if (fragment is M17_DetallFragmentActivity) {
            fragment.setOnNomAfegitListener(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.m17_fragments)
        ll = findViewById(R.id.elLayout)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        CURSOS.add("DAW2")
        CURSOS.add("DAM2")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.m17_fragments, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onNomAfegit(nom: String) {
        CURSOS.add(nom)
        Log.v("DAM", CURSOS.toString())
        val llistaFragment = fragmentManager.findFragmentById(R.id.contenidorFragmentLlista) as? M17_LlistaFragmentActivity
        llistaFragment?.afegirNom(nom)
    }

    @Suppress("DEPRECATION")
    class FragmentInterna : Fragment() {
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.m17_fragment_detall2, container, false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        return when (item.itemId) {
            R.id.afegirLlista -> {
                transaction.replace(R.id.contenidorFragmentLlista, M17_LlistaFragmentActivity())
                transaction.commit(); true
            }
            R.id.afegirDetall -> {
                transaction.replace(R.id.contenidorFragmentDetall, M17_DetallFragmentActivity())
                transaction.commit(); true
            }
            R.id.canviarDetall -> {
                transaction.replace(R.id.contenidorFragmentDetall, FragmentInterna())
                transaction.commit(); true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onConfigurationChanged(novaConfiguracio: Configuration) {
        super.onConfigurationChanged(novaConfiguracio)
        val lp = CoordinatorLayout.LayoutParams(
            CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT
        )
        ll.layoutParams = lp
        when (novaConfiguracio.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show()
                ll.orientation = LinearLayout.HORIZONTAL
            }
            Configuration.ORIENTATION_PORTRAIT -> {
                Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show()
                ll.orientation = LinearLayout.VERTICAL
            }
        }
    }
}

