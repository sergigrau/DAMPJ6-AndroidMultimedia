

package edu.fje.dam2;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que llegeix un XML contingut en l'aplicació
 *
 * @author sergi.grau@fje.edu
 * @version 5.0 27.01.2020
 */
public class M16_LecturaXMLUtility {
    private static final String ns = null;


    /**
     * Mètode que llegeix l'XML i retorna una lista amb els alumnes continguts
     *
     * @param in
     * @return llista d'alumnes
     * @throws XmlPullParserException
     * @throws IOException
     */
    public List<Alumne> analitzarXML(InputStream in) throws XmlPullParserException, IOException {
        try {

            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return llegirAlumnes(parser);
        } finally {
            in.close();
        }
    }

    /**
     * Mètode que llegeix els alumnes continguts en el XML
     *
     * @param parser
     * @return
     * @throws XmlPullParserException
     * @throws IOException
     */
    private List<Alumne> llegirAlumnes(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<Alumne> alumnes = new ArrayList<Alumne>();

        parser.require(XmlPullParser.START_TAG, ns, "curs");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // cerquem l'element alumne
            if (name.equals("alumne")) {
                alumnes.add(llegirAlumne(parser));
            } else {
                saltarEtiqueta(parser);
            }
        }
        return alumnes;
    }

    // Aquesta classe representa un alumne
    public static class Alumne {
        public final String nom;
        public final String cognom;
        public final String dni;

        private Alumne(String nom, String cognom, String dni) {
            this.nom = nom;
            this.cognom = cognom;
            this.dni = dni;
        }

        @Override
        public String toString() {
            return nom + "-" + cognom + "-" + dni;
        }
    }

    /**
     * Mètode que analitza el contingut d'un alumne. Si troba tots els subelements fa el tractament, altrament
     * ignora l'etiqueta
     *
     * @param parser
     * @return un objecte de la classe Alumne
     * @throws XmlPullParserException
     * @throws IOException
     */
    private Alumne llegirAlumne(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "alumne");
        String nom = null;
        String cognom = null;
        String dni = parser.getAttributeValue(null, "dni");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("nom")) {
                nom = llegirNom(parser);
            } else if (name.equals("cognom")) {
                cognom = llegirCognom(parser);
            } else {
                saltarEtiqueta(parser);
            }
        }
        return new Alumne(nom, cognom, dni);
    }

    /**
     * Mètode que processa l'element nom
     *
     * @param parser
     * @return cadena amb el nom de l'alumne
     * @throws IOException
     * @throws XmlPullParserException
     */
    private String llegirNom(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "nom");
        String nom = llegirText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "nom");
        return nom;
    }

    /**
     * Mètode que processa el cognom
     *
     * @param parser
     * @return cadena amb el cognom
     * @throws IOException
     * @throws XmlPullParserException
     */
    private String llegirCognom(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "cognom");
        String cognom = llegirText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "cognom");
        return cognom;
    }

    /**
     * Mètode que llegeix el contingut dels element identificats
     *
     * @param parser
     * @return
     * @throws IOException
     * @throws XmlPullParserException
     */
    private String llegirText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String text = "";
        if (parser.next() == XmlPullParser.TEXT) {
            text = parser.getText();
            parser.nextTag();
        }
        return text;
    }


    /**
     * Mètode que permet ignorar les etiquetes que no han de ser processades
     *
     * @param parser
     * @throws XmlPullParserException
     * @throws IOException
     */
    private void saltarEtiqueta(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int profunditat = 1;
        while (profunditat != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    profunditat--;
                    break;
                case XmlPullParser.START_TAG:
                    profunditat++;
                    break;
            }
        }
    }
}
