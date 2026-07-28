package edu.fje.dam2

import android.graphics.Path

/**
 * Classe que representa un traç de pintura.
 *
 * @author sergi.grau@fje.edu
 * @version 6.0 27.01.2026
 */
data class M07_PathDit(
    val color: Int,
    val relleu: Boolean,
    val difumina: Boolean,
    val ampladaTrac: Int,
    val ruta: Path
)

