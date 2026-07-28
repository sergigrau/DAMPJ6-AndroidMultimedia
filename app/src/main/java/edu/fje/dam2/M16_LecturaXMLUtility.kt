package edu.fje.dam2

import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream

/**
 * Classe que llegeix un XML contingut en l'aplicació.
 *
 * @author sergi.grau@fje.edu
 * @version 6.0 (API 33) 19.04.2026
 */
class M16_LecturaXMLUtility {

    companion object {
        private val ns: String? = null
    }

    @Throws(XmlPullParserException::class, IOException::class)
    fun analitzarXML(input: InputStream): List<Alumne> {
        input.use {
            val parser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(it, null)
            parser.nextTag()
            return llegirAlumnes(parser)
        }
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun llegirAlumnes(parser: XmlPullParser): List<Alumne> {
        val alumnes = mutableListOf<Alumne>()
        parser.require(XmlPullParser.START_TAG, ns, "curs")
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) continue
            if (parser.name == "alumne") alumnes.add(llegirAlumne(parser))
            else saltarEtiqueta(parser)
        }
        return alumnes
    }

    data class Alumne(val nom: String?, val cognom: String?, val dni: String?) {
        override fun toString() = "$nom-$cognom-$dni"
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun llegirAlumne(parser: XmlPullParser): Alumne {
        parser.require(XmlPullParser.START_TAG, ns, "alumne")
        var nom: String? = null
        var cognom: String? = null
        val dni = parser.getAttributeValue(null, "dni")
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) continue
            when (parser.name) {
                "nom"    -> nom    = llegirNom(parser)
                "cognom" -> cognom = llegirCognom(parser)
                else     -> saltarEtiqueta(parser)
            }
        }
        return Alumne(nom, cognom, dni)
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun llegirNom(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "nom")
        val nom = llegirText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "nom")
        return nom
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun llegirCognom(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "cognom")
        val cognom = llegirText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "cognom")
        return cognom
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun llegirText(parser: XmlPullParser): String {
        var text = ""
        if (parser.next() == XmlPullParser.TEXT) {
            text = parser.text
            parser.nextTag()
        }
        return text
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun saltarEtiqueta(parser: XmlPullParser) {
        if (parser.eventType != XmlPullParser.START_TAG) throw IllegalStateException()
        var profunditat = 1
        while (profunditat != 0) {
            when (parser.next()) {
                XmlPullParser.END_TAG   -> profunditat--
                XmlPullParser.START_TAG -> profunditat++
            }
        }
    }
}

