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

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Classe que llegeix un XML contingut en l'aplicació
 *
 * @author sergi.grau@fje.edu
 * @version 5.0 27.01.2020
 */

public class M16_LecturaXMLActivity extends AppCompatActivity {

    private ListView llista;
    private ArrayList<String> llistaNoms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m16_lectura_xml);
        llista =  findViewById(R.id.llistaXML);


        M16_LecturaXMLUtility lecturaXML = new M16_LecturaXMLUtility();
        List<M16_LecturaXMLUtility.Alumne> llistaAlumnes = null;
        AssetManager am = getAssets();
        InputStream is;
        try {
            is = am.open("m16_curs.xml");
            llistaAlumnes = lecturaXML.analitzarXML(is);

        } catch (IOException e) {

            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }


        llistaNoms = new ArrayList<String>();
        for (M16_LecturaXMLUtility.Alumne alumne : llistaAlumnes) {
            llistaNoms.add(alumne.toString());
        }


        final ArrayAdaptarEstable adapter = new ArrayAdaptarEstable(this,
                android.R.layout.simple_list_item_1, llistaNoms);
        llista.setAdapter(adapter);

        llista.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position,
                                    long arg3) {
                final String item = (String) parent.getItemAtPosition(position);
                view.animate().setDuration(2000).alpha(0)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                llistaNoms.remove(item);
                                adapter.notifyDataSetChanged();
                                view.setAlpha(1);
                            }
                        });

            }
        });
    }

    /**
     * Classe interna privada que implementa l'adaptador
     */
    private class ArrayAdaptarEstable extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

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