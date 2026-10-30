# M16 – XML Parsing

## Objective
Learn how to read and parse XML files included in the application's resources (`assets/`) using `XmlPullParser`, and display the parsed data in a `ListView` with a custom adapter that supports stable IDs.

## Files Involved
| File | Description |
|------|-------------|
| `M16_LecturaXMLActivity.kt` | Main activity with ListView |
| `M16_LecturaXMLUtility.kt` | Utility class for XML parsing |
| `assets/m16_curs.xml` | XML file with student data |
| `res/layout/m16_lectura_xml.xml` | Layout with ListView |

## XML File Structure

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

## Key Concepts

### AssetManager – Accessing Asset Files
Files in `assets/` are accessed via `AssetManager`:

```kotlin
val inputStream = assets.open("m16_curs.xml")
```

**Difference between `assets/` and `res/`:**
| | `assets/` | `res/raw/` |
|---|---|---|
| Access | `AssetManager.open()` | `resources.openRawResource(R.raw.xxx)` |
| Subdirectories | Yes | No |
| File types | Any | Any |
| Referenced by R | No | Yes |

### XmlPullParser – Pull XML Parsing
`XmlPullParser` processes XML element by element, consuming it sequentially (pull pattern):

```kotlin
fun parseXml(input: InputStream): List<Student> {
    input.use {
        val parser = Xml.newPullParser()
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
        parser.setInput(it, null)
        parser.nextTag()          // Advance to the first element
        return readStudents(parser)
    }
}
```

**Parser events:**
| Event | Description |
|-------|-------------|
| `START_DOCUMENT` | Start of the document |
| `START_TAG` | Opening tag `<name>` |
| `TEXT` | Text content between tags |
| `END_TAG` | Closing tag `</name>` |
| `END_DOCUMENT` | End of the document |

### Recursive Node Reading

```kotlin
private fun readStudents(parser: XmlPullParser): List<Student> {
    val students = mutableListOf<Student>()
    parser.require(XmlPullParser.START_TAG, null, "curs")  // Validates the tag

    while (parser.next() != XmlPullParser.END_TAG) {
        if (parser.eventType != XmlPullParser.START_TAG) continue
        when (parser.name) {
            "alumne" -> students.add(readStudent(parser))
            else     -> skipTag(parser)  // Ignore unknown tags
        }
    }
    return students
}

private fun readStudent(parser: XmlPullParser): Student {
    val dni = parser.getAttributeValue(null, "dni")  // Read attribute
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

### Student Data Class

```kotlin
data class Student(val name: String?, val surname: String?, val dni: String?) {
    override fun toString() = "$name-$surname-$dni"
}
```

### Adapter with Stable IDs
To avoid flickering when updating the `ListView`, the adapter implements `hasStableIds()`:

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

### Item Removal Animation

```kotlin
list.setOnItemClickListener { parent, view, position, _ ->
    val item = parent.getItemAtPosition(position) as String
    view.animate()
        .setDuration(2000)
        .alpha(0f)
        .withEndAction {
            itemNames.remove(item)
            adapter.notifyDataSetChanged()
            view.alpha = 1f  // Restore for ViewHolder reuse
        }
}
```

## XML Parser Comparison in Android
| Parser | Model | Recommended for |
|--------|-------|----------------|
| `XmlPullParser` | Pull (event-driven) | Efficient, low memory usage |
| `SAXParser` | Push (event-driven) | Large documents |
| `DOM` | In-memory tree | Small documents, navigation |

---
[M15](M15-OpenGL-ES-20) | [Home](Home) | [M17](M17-Fragments)
