# M18 – Firebase Realtime Database

## 🎯 Objectiu
Aprendre a integrar **Firebase Realtime Database** en una aplicació Android per emmagatzemar, llegir i mostrar dades en temps real al núvol, sense autenticació. Implementa un CRUD bàsic d'artistes musicals.

## 📄 Fitxers implicats
| Fitxer | Descripció |
|--------|-----------|
| `M18_Firebase_Artistes.kt` | Activity principal amb llista i formulari |
| `M18_Artista.kt` | Data class de l'entitat Artista |
| `M18_Llista.kt` | Adaptador personalitzat del ListView |
| `res/layout/m18_firebase.xml` | Layout principal |
| `res/layout/m18_llista.xml` | Layout de cada element de la llista |
| `google-services.json` | Credencials de Firebase (no inclòs al repo) |

## 🔑 Conceptes clau

### Configuració de Firebase
1. Crear un projecte a [Firebase Console](https://console.firebase.google.com/)
2. Afegir una app Android amb el package `edu.fje.dam2`
3. Descarregar `google-services.json` i col·locar-lo a `app/`
4. Habilitar **Realtime Database** en mode test

### Data class amb Firebase
Firebase necessita una classe amb un **constructor sense arguments** i propietats públiques:

```kotlin
@IgnoreExtraProperties
data class M18_Artista(
    val id: String = "",
    val nom: String = "",
    val genere: String = ""
)
// Kotlin data class amb valors per defecte = constructor implícit sense args ✅
```

> `@IgnoreExtraProperties` fa que Firebase ignori camps de la BD que no existeixin a la classe.

### Referència a la base de dades

```kotlin
// Obtenir referència al node "artistes"
val DBArtistes: DatabaseReference = FirebaseDatabase.getInstance().getReference("artistes")
```

### Llegir dades en temps real (ValueEventListener)
```kotlin
DBArtistes.addValueEventListener(object : ValueEventListener {
    override fun onDataChange(dataSnapshot: DataSnapshot) {
        artistes.clear()
        for (postSnapshot in dataSnapshot.children) {
            val artista = postSnapshot.getValue(M18_Artista::class.java)
            artista?.let { artistes.add(it) }
        }
        // Actualitzar la UI
        llistaArtistes.adapter = M18_Llista(this@M18_Firebase_Artistes, artistes)
    }

    override fun onCancelled(databaseError: DatabaseError) {
        // Gestionar error
    }
})
```

**Diferència entre listeners:**
| Listener | Quan s'activa |
|----------|--------------|
| `addValueEventListener` | En cada canvi (temps real) |
| `addListenerForSingleValueEvent` | Una sola vegada |

### Escriure dades (push + setValue)

```kotlin
private fun afegirArtista() {
    val nomText = nom.text.toString().trim()
    val genereText = genere.selectedItem.toString()

    if (nomText.isNotEmpty()) {
        // push() genera una clau única
        val id = DBArtistes.push().key

        if (id != null) {
            val artista = M18_Artista(id, nomText, genereText)
            DBArtistes.child(id).setValue(artista)  // Escriu a /artistes/{id}
        }
    }
}
```

**Estructura a Firebase:**
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
class M18_Llista(private val context: Activity, private val artistes: List<M18_Artista>) :
    ArrayAdapter<M18_Artista>(context, R.layout.m18_llista, artistes) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val listViewItem = context.layoutInflater.inflate(R.layout.m18_llista, null, true)
        val nom: TextView     = listViewItem.findViewById(R.id.nom)
        val genere: TextView  = listViewItem.findViewById(R.id.genere)
        val artist = artistes[position]
        nom.text    = artist.nom
        genere.text = artist.genere
        return listViewItem
    }
}
```

### Firebase BOM (Bill of Materials)
El BOM sincronitza automàticament les versions de totes les biblioteques Firebase:

```kotlin
// build.gradle.kts
implementation(platform("com.google.firebase:firebase-bom:34.12.0"))
implementation("com.google.firebase:firebase-database")  // Sense versió explícita
```

## 🔒 Regles de seguretat Firebase
Per a tests (sense autenticació):
```json
{
  "rules": {
    ".read": true,
    ".write": true
  }
}
```

> ⚠️ **Atenció**: aquestes regles permeten accés públic. En producció, cal autenticació.

## 📊 Operacions CRUD a Firebase
| Operació | Mètode |
|----------|--------|
| **Create** | `ref.push().setValue(obj)` |
| **Read** | `ref.addValueEventListener(...)` |
| **Update** | `ref.child(id).updateChildren(map)` |
| **Delete** | `ref.child(id).removeValue()` |

---
[← M17](M17-Fragments) | [Inici](Home) | [M19 →](M19-Animacio-Descriptor)

