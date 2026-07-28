package edu.fje.dam2

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Spinner
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
 * Activity que accedeix a Firebase per a afegir i mostrar artistes.
 *
 * @author sergi.grau@fje.edu
 * @version 6.0 27.01.2026
 */
class M18_Firebase_Artistes : AppCompatActivity() {

    companion object {
        const val NOM = "dam2.fje.edu.nom"
        const val ID = "dam2.fje.edu.id"
    }

    private lateinit var nom: EditText
    private lateinit var genere: Spinner
    private lateinit var afegirArtista: Button
    private lateinit var llistaArtistes: ListView
    private val artistes = mutableListOf<M18_Artista>()
    private lateinit var DBArtistes: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.m18_firebase)
        DBArtistes = FirebaseDatabase.getInstance().getReference("artistes")
        nom = findViewById(R.id.editNom)
        genere = findViewById(R.id.generes)
        llistaArtistes = findViewById(R.id.llistaArtistes)
        afegirArtista = findViewById(R.id.afegirArtistes)

        afegirArtista.setOnClickListener { afegirArtista() }
        llistaArtistes.setOnItemClickListener { _, _, i, _ ->
            val artist = artistes[i]
            val intent = Intent(applicationContext, M18_Firebase_Artistes::class.java)
            intent.putExtra(ID, artist.id)
            intent.putExtra(NOM, artist.nom)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        DBArtistes.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                artistes.clear()
                for (postSnapshot in dataSnapshot.children) {
                    val artista = postSnapshot.getValue(M18_Artista::class.java)
                    artista?.let { artistes.add(it) }
                }
                llistaArtistes.adapter = M18_Llista(this@M18_Firebase_Artistes, artistes)
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun afegirArtista() {
        val nomText = nom.text.toString().trim()
        val genereText = genere.selectedItem.toString()
        if (!TextUtils.isEmpty(nomText)) {
            val id = DBArtistes.push().key
            if (id != null) {
                val artista = M18_Artista(id, nomText, genereText)
                DBArtistes.child(id).setValue(artista)
            }
            nom.setText("")
            Toast.makeText(this, "Artista afegit", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Cal entrar un nom", Toast.LENGTH_LONG).show()
        }
    }
}

