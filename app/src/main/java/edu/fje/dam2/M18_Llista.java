package edu.fje.dam2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;
/**
 * Classe que controla la llista on es mostren els artistes
 * @author sergi.grau@fje.edu
 * @version 6.0 27.01.2026 */

public class M18_Llista  extends ArrayAdapter<M18_Artista> {
    private final Activity context;
    List<M18_Artista> artistes;

    public M18_Llista(Activity context, List<M18_Artista> artistes) {
        super(context, R.layout.m18_llista, artistes);
        this.context = context;
        this.artistes = artistes;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        @SuppressLint("ViewHolder") View listViewItem = inflater.inflate(R.layout.m18_llista, null, true);

        TextView nom =  listViewItem.findViewById(R.id.nom);
        TextView genere = listViewItem.findViewById(R.id.genere);

        M18_Artista artist = artistes.get(position);
        nom.setText(artist.getNom());
        genere.setText(artist.getGenere());

        return listViewItem;
    }
}
