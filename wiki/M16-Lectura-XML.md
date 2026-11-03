# M16 – Lectura XML

## Objectiu
Aprendre a llegir i processar fitxers XML inclosos en els recursos de l'aplicació (`assets/`) mitjançant `XmlPullParser`, i mostrar les dades processades en un `ListView` amb un adaptador personalitzat que admet identificadors estables.

## Fitxers involucrats
| Fitxer | Descripció |
|--------|------------|
| `M16_LecturaXMLActivity.kt` | Activitat principal amb ListView |
| `M16_LecturaXMLUtility.kt` | Classe d'utilitat per al processament d'XML |
| `assets/m16_curs.xml` | Fitxer XML amb dades d'alumnes |
| `res/layout/m16_lectura_xml.xml` | Disseny amb ListView |

## Estructura del fitxer XML

```xml
<!-- assets/m16_curs.xml -->
<curs>
    <alumne dni="12345678A">
        <nom>Joan</nom>
        <cognom>Garcia</cognom>
    </alumne>
    <alumne dni="87654321B">
        <nom>Maria</nom>
        <cognom>Lopez</cognom>
    </alumne>
</curs>
```

## Conceptes clau

### AssetManager – Accés a fitxers d'assets
S'accedeix als fitxers de `assets/` mitjançant `AssetManager`:

```kotlin
val inputStream = assets.open("m16_curs.xml")
```

**Diferència entre `assets/` i `res/`:**
| | `assets/` | `res/raw/` |
|---|---|---|
| Accés | `AssetManager.open()` | `resources.openRawResource(R.raw.xxx)` |
| Subdirectoris | Sí | No |
| Tipus de fitxers | Qualsevol | Qualsevol |
| Referenciat per R | No | Sí |

### XmlPullParser – Processament XML tipus Pull
`XmlPullParser` processa l melement XML a element, consumint-lo de forma seqüencial (patró pull):

```kotlin
fun parseXml(input: InputStream): List<Student> {
    input.use {
        val parser = Xml.newPullParser()
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
        parser.setInput(it, null)
        parser.nextTag()          // Avançar al primer element
        return readStudents(parser)
    }
}
```

**Esdeveniments del parser:**
| Esdeveniment | Descripció |
|--------------|------------|
| `START_DOCUMENT` | Inici del document |
| `START_TAG` | Etiqueta d'obertura `<nom>` |
| `TEXT` | Contingut de text entre etiquetes |
| `END_TAG` | Etiqueta de tancament `</nom>` |
| `END_DOCUMENT` | Final del document |

### Lectura recursiva de nodes

```kotlin
private fun readStudents(parser: XmlPullParser): List<Student> {
    val students = mutableListOf<Student>()
    parser.require(XmlPullParser.START_TAG, null, "curs")  // Valida l'etiqueta

    while (parser.next() != XmlPullParser.END_TAG) {
        if (parser.eventType != XmlPullParser.START_TAG) continue
        when (parser.name) {
            "alumne" -> students.add(readStudent(parser))
            else     -> skipTag(parser)  // Ignorar etiquetes desconegudes
        }
    }
    return students
}

private fun readStudent(parser: XmlPullParser): Student {
    val dni = parser.getAttributeValue(null, "dni")  // Llegir atribut
    var name: String? = null
    var surname: String? = null

    while (parser.next() != XmlPullParser.END_TAG) {
        if (parser.eventType != XmlPullParser.START_TAG) continue
        when (parser.name) {
            "nom"    -> name    = readText(parser)
            "cognom" -> surname = readText(parser)
            else     -> skipTag(parser)
        }
    }
    return Student(name, surname, dni)
}
```

### Classe de dades Student

```kotlin
data class Student(val name: String?, val surname: String?, val dni: String?) {
    override fun toString() = "$name-$surname-$dni"
}
```

### Adaptador amb IDs estables
Per evitar parpellejos quan s'actualitza el `ListView`, l'adaptador implementa `hasStableIds()`:

```kotlin
private class StableArrayAdapter(
    context: Context, layout: Int, objects: List<String>
) : ArrayAdapter<String>(context, layout, objects) {

    private val idMap = HashMap<String, Int>()

    init { objects.forEachIndexed { i, s -> idMap[s] = i } }

    override fun getItemId(position: Int): Long = idMap[getItem(position)]?.toLong() ?: 0L
    override fun hasStableIds(): Boolean = true
}
```

### Animació de supressió d'elements

```kotlin
list.setOnItemClickListener { parent, view, position, _ ->
    val item = parent.getItemAtPosition(position) as String
    view.animate()
        .setDuration(2000)
        .alpha(0f)
        .withEndAction {
            itemNames.remove(item)
            adapter.notifyDataSetChanged()
            view.alpha = 1f  // Restaurar per a la reutilització de ViewHolder
        }
}
```

## Comparativa de parsers XML a Android
| Parser | Model | Recomanat per a |
|--------|-------|-----------------|
| `XmlPullParser` | Pull (basat en esdeveniments) | Eficient, baix ús de memòria |
| `SAXParser` | Push (basat en esdeveniments) | Documents grans |
| `DOM` | Arbre en memòria | Documents petits, navegació |

---
[M15](M15-OpenGL-ES-20) | [Home](Home) | [M17](M17-Fragments)
