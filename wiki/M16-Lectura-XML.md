# M16 – Lectura i anàlisi d'XML

## 🎯 Objectiu
Aprendre a llegir i analitzar fitxers XML inclosos en els recursos de l'aplicació (`assets/`) usant `XmlPullParser`, i mostrar les dades parseades en un `ListView` amb un adaptador personalitzat que suporta IDs estables.

## 📄 Fitxers implicats
| Fitxer | Descripció |
|--------|-----------|
| `M16_LecturaXMLActivity.kt` | Activity principal amb ListView |
| `M16_LecturaXMLUtility.kt` | Classe d'utilitat per parsejar l'XML |
| `assets/m16_curs.xml` | Fitxer XML amb dades d'alumnes |
| `res/layout/m16_lectura_xml.xml` | Layout amb ListView |

## 🔑 Estructura del fitxer XML

```xml
<!-- assets/m16_curs.xml -->
<curs>
    <alumne dni="12345678A">
        <nom>Joan</nom>
        <cognom>García</cognom>
    </alumne>
    <alumne dni="87654321B">
        <nom>Maria</nom>
        <cognom>López</cognom>
    </alumne>
</curs>
```

## 🔑 Conceptes clau

### AssetManager – Accés a fitxers d'assets
Els fitxers a `assets/` s'accedeixen amb `AssetManager`:

```kotlin
val inputStream = assets.open("m16_curs.xml")
```

**Diferència entre `assets/` i `res/`:**
| | `assets/` | `res/raw/` |
|---|---|---|
| Accés | `AssetManager.open()` | `resources.openRawResource(R.raw.xxx)` |
| Subcarpetes | ✅ Sí | ❌ No |
| Tipus de fitxers | Qualsevol | Qualsevol |
| Referenciat per R | ❌ | ✅ |

### XmlPullParser – Anàlisi pull de XML
`XmlPullParser` processa l'XML element per element, consumint-lo seqüencialment (patró *pull*):

```kotlin
fun analitzarXML(input: InputStream): List<Alumne> {
    input.use {
        val parser = Xml.newPullParser()
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
        parser.setInput(it, null)
        parser.nextTag()          // Avança al primer element
        return llegirAlumnes(parser)
    }
}
```

**Events del parser:**
| Event | Descripció |
|-------|-----------|
| `START_DOCUMENT` | Inici del document |
| `START_TAG` | Inici d'etiqueta `<nom>` |
| `TEXT` | Contingut de text entre etiquetes |
| `END_TAG` | Tancament d'etiqueta `</nom>` |
| `END_DOCUMENT` | Fi del document |

### Lectura recursiva de nodes

```kotlin
private fun llegirAlumnes(parser: XmlPullParser): List<Alumne> {
    val alumnes = mutableListOf<Alumne>()
    parser.require(XmlPullParser.START_TAG, null, "curs")  // Valida l'etiqueta

    while (parser.next() != XmlPullParser.END_TAG) {
        if (parser.eventType != XmlPullParser.START_TAG) continue
        when (parser.name) {
            "alumne" -> alumnes.add(llegirAlumne(parser))
            else     -> saltarEtiqueta(parser)  // Ignorar etiquetes desconegudes
        }
    }
    return alumnes
}

private fun llegirAlumne(parser: XmlPullParser): Alumne {
    val dni = parser.getAttributeValue(null, "dni")  // Llegir atribut
    var nom: String? = null
    var cognom: String? = null

    while (parser.next() != XmlPullParser.END_TAG) {
        if (parser.eventType != XmlPullParser.START_TAG) continue
        when (parser.name) {
            "nom"    -> nom    = llegirText(parser)
            "cognom" -> cognom = llegirText(parser)
            else     -> saltarEtiqueta(parser)
        }
    }
    return Alumne(nom, cognom, dni)
}
```

### Data class Alumne

```kotlin
data class Alumne(val nom: String?, val cognom: String?, val dni: String?) {
    override fun toString() = "$nom-$cognom-$dni"
}
```

### Adaptador amb IDs estables
Per evitar parpelleig en actualitzar el `ListView`, l'adaptador implementa `hasStableIds()`:

```kotlin
private class ArrayAdaptarEstable(
    context: Context, layout: Int, objects: List<String>
) : ArrayAdapter<String>(context, layout, objects) {

    private val mIdMap = HashMap<String, Int>()

    init { objects.forEachIndexed { i, s -> mIdMap[s] = i } }

    override fun getItemId(position: Int): Long = mIdMap[getItem(position)]?.toLong() ?: 0L
    override fun hasStableIds(): Boolean = true
}
```

### Animació d'eliminació d'elements

```kotlin
llista.setOnItemClickListener { parent, view, position, _ ->
    val item = parent.getItemAtPosition(position) as String
    view.animate()
        .setDuration(2000)
        .alpha(0f)
        .withEndAction {
            llistaNoms.remove(item)
            adapter.notifyDataSetChanged()
            view.alpha = 1f  // Restaurar per a la reutilització del ViewHolder
        }
}
```

## 📌 Comparativa de parsers XML a Android
| Parser | Model | Ús recomanat |
|--------|-------|-------------|
| `XmlPullParser` | Pull (event-driven) | ✅ Eficient, poc ús de memòria |
| `SAXParser` | Push (event-driven) | Documents grans |
| `DOM` | Arbre en memòria | Documents petits, navegació |

---
[← M15](M15-OpenGL-ES-20) | [Inici](Home) | [M17 →](M17-Fragments)

