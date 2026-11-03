# M18 – Firebase Realtime Database

## Objectiu
Aprendre a integrar **Firebase Realtime Database** en una aplicació Android per emmagatzemar, llegir i mostrar dades en temps real al núvol, sense autenticació. Implementa un CRUD bàsic per a artistes musicals.

## Fitxers involucrats
| Fitxer | Descripció |
|--------|------------|
| `M18_Firebase_Artistes.kt` | Activitat principal amb llista i formulari |
| `M18_Artista.kt` | Classe de dades de l'entitat Artista |
| `M18_Llista.kt` | Adaptador personalitzat de ListView |
| `res/layout/m18_firebase.xml` | Disseny principal |
| `res/layout/m18_llista.xml` | Disseny per a cada element de la llista |
| `google-services.json` | Credencials de Firebase (no inclòs al repositori) |

## Conceptes clau

### Configuració de Firebase
1. Crear un projecte a la [Consola de Firebase](https://console.firebase.google.com/).
2. Afegir una aplicació Android amb el paquet `edu.fje.dam2`.
3. Descarregar `google-services.json` i col·locar-lo a `app/`.
4. Habilitar **Realtime Database** en mode de proves.

### Classe de dades amb Firebase
Firebase requereix una classe amb un **constructor sense arguments** i propietats públiques:

```kotlin
@IgnoreExtraProperties
data class M18_Artista(
    val id: String = "",
    val nom: String = "",
    val genere: String = ""
)
// Classe de dades Kotlin amb valors per defecte = constructor sense arguments implícit
```

> `@IgnoreExtraProperties` indica a Firebase que ignori els camps de la base de dades que no existeixen a la classe.

### Referència de la base de dades

```kotlin
// Obtenir una referència al node "artistes"
val DBArtistes: DatabaseReference = FirebaseDatabase.getInstance().getReference("artistes")
```

### Lectura de dades en temps real (ValueEventListener)
```kotlin
DBArtistes.addValueEventListener(object : ValueEventListener {
    override fun onDataChange(dataSnapshot: DataSnapshot) {
        artists.clear()
        for (postSnapshot in dataSnapshot.children) {
            val artist = postSnapshot.getValue(M18_Artista::class.java)
            artist?.let { artists.add(it) }
        }
        // Actualitzar la UI
        listView.adapter = M18_Llista(this@M18_Firebase_Artistes, artists)
    }

    override fun onCancelled(databaseError: DatabaseError) {
        // Gestionar l'error
    }
})
```

**Tipus de listeners:**
| Listener | Quan es dispara |
|----------|-----------------|
| `addValueEventListener` | A cada canvi (temps real) |
| `addListenerForSingleValueEvent` | Només una vegada |

### Escriure dades (push + setValue)

```kotlin
private fun addArtist() {
    val nameText = nameField.text.toString().trim()
    val genreText = genreSpinner.selectedItem.toString()

    if (nameText.isNotEmpty()) {
        // push() genera una clau única
        val id = DBArtistes.push().key

        if (id != null) {
            val artist = M18_Artista(id, nameText, genreText)
            DBArtistes.child(id).setValue(artist)  // Escriu a /artistes/{id}
        }
    }
}
```

**Estructura de la base de dades Firebase:**
```json
{
  "artistes": {
    "-NxKjhABC123": {
      "id": "-NxKjhABC123",
      "nom": "AC/DC",
      "genere": "Rock"
    },
    "-NxKjhXYZ456": {
      "id": "-NxKjhXYZ456",
      "nom": "Mozart",
      "genere": "Clàssica"
    }
  }
}
```

### Adaptador personalitzat M18_Llista

```kotlin
class M18_Llista(private val context: Activity, private val artists: List<M18_Artista>) :
    ArrayAdapter<M18_Artista>(context, R.layout.m18_llista, artists) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val listViewItem = context.layoutInflater.inflate(R.layout.m18_llista, null, true)
        val name: TextView  = listViewItem.findViewById(R.id.nom)
        val genre: TextView = listViewItem.findViewById(R.id.genere)
        val artist = artists[position]
        name.text  = artist.nom
        genre.text = artist.genere
        return listViewItem
    }
}
```

### Firebase BOM (Bill of Materials)
El BOM sincronitza automàticament les meves versions de totes les llibreries de Firebase:

```kotlin
// build.gradle.kts
implementation(platform("com.google.firebase:firebase-bom:34.12.0"))
implementation("com.google.firebase:firebase-database")  // No cal versió explícita
```

## Regles de seguretat de Firebase
Per a proves (sense autenticació):
```json
{
  "rules": {
    ".read": true,
    ".write": true
  }
}
```

> Aquestes regles permeten l'accés públic. En producció, cal requerir autenticació.

## Operacions CRUD a Firebase
| Operació | Mètode |
|-----------|--------|
| **Crear** | `ref.push().setValue(obj)` |
| **Llegir** | `ref.addValueEventListener(...)` |
| **Actualitzar** | `ref.child(id).updateChildren(map)` |
| **Esborrar** | `ref.child(id).removeValue()` |

---
[M17](M17-Fragments) | [Home](Home) | [M19](M19-Animacio-Descriptor)
