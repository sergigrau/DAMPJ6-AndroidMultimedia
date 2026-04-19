package edu.fje.dam2;

import android.graphics.Path;

/**
 * Classe que representa un traç de pintura
 *
 * @author sergi.grau@dje.edu
 * @version 6.0 27.01.2026
 * */
public class M07_PathDit {

    public int color;
    // Variables en català
    public boolean relleu;
    public boolean difumina;
    public int ampladaTrac;
    public Path ruta;

    public M07_PathDit(int color, boolean relleu, boolean difumina, int ampladaTrac, Path ruta) {
        this.color = color;
        this.relleu = relleu;
        this.difumina = difumina;
        this.ampladaTrac = ampladaTrac;
        this.ruta = ruta;
    }
}
