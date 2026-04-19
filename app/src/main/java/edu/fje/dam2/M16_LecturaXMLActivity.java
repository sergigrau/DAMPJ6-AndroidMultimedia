package edu.fje.dam2;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.os.Bundle;
import android.content.Context;
import android.content.res.AssetManager;

import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Classe que llegeix un XML contingut en l'aplicació
 * Disposa d'una classe d'utilitat per a facilitar la lectura de l'XML
 * L'ús de AssetManager és la forma recomanada per accedir a fitxers d'actius en lloc de rutes de fitxer (deprecated).
 * L'ús de InputStream per llegir l'XML és la forma recomanada en lloc de rutes de fitxer (deprecated).
 * L'ús de XmlPullParser és la forma recomanada per analitzar XML en Android.
 * L'ús de ListView amb ArrayAdapter és la forma recomanada per mostrar llistes d'items en Android.
 * L'ús de onItemClickListener per gestionar els clics en els items de la llista és la forma recomanada per interactuar amb els elements de la llista.
 * L'ús de animacions per eliminar items de la llista és una forma recomanada per millorar l'experiència d'usuari en Android.
 *
 *
 * @author sergi.grau@fje.edu
 * @version 6.0 (API 33) 19.04.2026
 * */

public class M16_LecturaXMLActivity extends AppCompatActivity {

    private ArrayList<String> llistaNoms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m16_lectura_xml);
        ListView llista = findViewById(R.id.llistaXML);


        M16_LecturaXMLUtility lecturaXML = new M16_LecturaXMLUtility();
        List<M16_LecturaXMLUtility.Alumne> llistaAlumnes = null;
        AssetManager am = getAssets();
        InputStream is;
        try {
            is = am.open("m16_curs.xml");
            llistaAlumnes = lecturaXML.analitzarXML(is);

        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
        }

        llistaNoms = new ArrayList<>();
        assert llistaAlumnes != null;
        for (M16_LecturaXMLUtility.Alumne alumne : llistaAlumnes) {
            llistaNoms.add(alumne.toString());
        }


        final ArrayAdaptarEstable adapter = new ArrayAdaptarEstable(this,
                android.R.layout.simple_list_item_1, llistaNoms);
        llista.setAdapter(adapter);

        llista.setOnItemClickListener((parent, view, position, arg3) -> {
            final String item = (String) parent.getItemAtPosition(position);
            view.animate().setDuration(2000).alpha(0)
                    .withEndAction(() -> {
                        llistaNoms.remove(item);
                        adapter.notifyDataSetChanged();
                        view.setAlpha(1);
                    });
        });
    }

    /**
     * Classe interna privada que implementa l'adaptador
     */
    private static class ArrayAdaptarEstable extends ArrayAdapter<String> {
        HashMap<String, Integer> mIdMap = new HashMap<>();
        public ArrayAdaptarEstable(Context context, int textViewResourceId,
                                   List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}