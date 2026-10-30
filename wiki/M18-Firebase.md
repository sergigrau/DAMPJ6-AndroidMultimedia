# M18 – Firebase Realtime Database

## Objective
Learn how to integrate **Firebase Realtime Database** into an Android application to store, read and display data in real time in the cloud, without authentication. Implements a basic CRUD for musical artists.

## Files Involved
| File | Description |
|------|-------------|
| `M18_Firebase_Artistes.kt` | Main activity with list and form |
| `M18_Artista.kt` | Artist entity data class |
| `M18_Llista.kt` | Custom ListView adapter |
| `res/layout/m18_firebase.xml` | Main layout |
| `res/layout/m18_llista.xml` | Layout for each list item |
| `google-services.json` | Firebase credentials (not included in repo) |

## Key Concepts

### Firebase Setup
1. Create a project at [Firebase Console](https://console.firebase.google.com/).
2. Add an Android app with the package `edu.fje.dam2`.
3. Download `google-services.json` and place it in `app/`.
4. Enable **Realtime Database** in test mode.

### Data Class with Firebase
Firebase requires a class with a **no-argument constructor** and public properties:

```kotlin
@IgnoreExtraProperties
data class M18_Artista(
    val id: String = "",
    val nom: String = "",
    val genere: String = ""
)
// Kotlin data class with default values = implicit no-arg constructor
```

> `@IgnoreExtraProperties` tells Firebase to ignore database fields that do not exist in the class.

### Database Reference

```kotlin
// Get a reference to the "artistes" node
val DBArtistes: DatabaseReference = FirebaseDatabase.getInstance().getReference("artistes")
```

### Reading Data in Real Time (ValueEventListener)
```kotlin
DBArtistes.addValueEventListener(object : ValueEventListener {
    override fun onDataChange(dataSnapshot: DataSnapshot) {
        artists.clear()
        for (postSnapshot in dataSnapshot.children) {
            val artist = postSnapshot.getValue(M18_Artista::class.java)
            artist?.let { artists.add(it) }
        }
        // Update the UI
        listView.adapter = M18_Llista(this@M18_Firebase_Artistes, artists)
    }

    override fun onCancelled(databaseError: DatabaseError) {
        // Handle error
    }
})
```

**Listener types:**
| Listener | When triggered |
|----------|---------------|
| `addValueEventListener` | On every change (real time) |
| `addListenerForSingleValueEvent` | Only once |

### Writing Data (push + setValue)

```kotlin
private fun addArtist() {
    val nameText = nameField.text.toString().trim()
    val genreText = genreSpinner.selectedItem.toString()

    if (nameText.isNotEmpty()) {
        // push() generates a unique key
        val id = DBArtistes.push().key

        if (id != null) {
            val artist = M18_Artista(id, nameText, genreText)
            DBArtistes.child(id).setValue(artist)  // Writes to /artistes/{id}
        }
    }
}
```

**Firebase database structure:**
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
      "genere": "Classical"
    }
  }
}
```

### Custom Adapter M18_Llista

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
The BOM automatically synchronises the versions of all Firebase libraries:

```kotlin
// build.gradle.kts
implementation(platform("com.google.firebase:firebase-bom:34.12.0"))
implementation("com.google.firebase:firebase-database")  // No explicit version needed
```

## Firebase Security Rules
For testing (no authentication):
```json
{
  "rules": {
    ".read": true,
    ".write": true
  }
}
```

> These rules allow public access. In production, authentication is required.

## Firebase CRUD Operations
| Operation | Method |
|-----------|--------|
| **Create** | `ref.push().setValue(obj)` |
| **Read** | `ref.addValueEventListener(...)` |
| **Update** | `ref.child(id).updateChildren(map)` |
| **Delete** | `ref.child(id).removeValue()` |

---
[M17](M17-Fragments) | [Home](Home) | [M19](M19-Animacio-Descriptor)
