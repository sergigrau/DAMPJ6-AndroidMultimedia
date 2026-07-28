package edu.fje.dam2

import com.google.firebase.database.IgnoreExtraProperties

/**
 * Classe Entitat que representa un Artista.
 *
 * @author sergi.grau@fje.edu
 * @version 6.0 27.01.2026
 */
@IgnoreExtraProperties
data class M18_Artista(
    val id: String = "",
    val nom: String = "",
    val genere: String = ""
)


