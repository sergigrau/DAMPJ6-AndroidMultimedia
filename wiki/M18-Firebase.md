# M18 – Firebase Realtime Database amb Arquitectura MVVM i StateFlow

## Objectiu
Aprendre a integrar **Firebase Realtime Database** seguint l'arquitectura recomanada per Android Jetpack: **MVVM (Model-View-ViewModel)** amb **`ViewModel`** i **`StateFlow`** reactiu.

## Fitxers Involucrats
| Fitxer | Descripció |
|--------|------------|
| `M18_ArtistesViewModel.kt` | ViewModel de Jetpack que encapsula Firebase |
| `M18_Firebase_Artistes.kt` | Activity que observa el StateFlow del ViewModel |
| `M18_Artista.kt` | Model de dades |

## Conceptes Clau

### Desacoblament amb Jetpack ViewModel
En lloc de fer crides directes a Firebase des de l'Activity, `M18_ArtistesViewModel` gestiona la base de dades i exposa la llista com a `StateFlow`:

```kotlin
class M18_ArtistesViewModel : ViewModel() {
    private val dbRef = FirebaseDatabase.getInstance().getReference("artistes")
    private val _artistes = MutableStateFlow<List<M18_Artista>>(emptyList())
    val artistes: StateFlow<List<M18_Artista>> = _artistes.asStateFlow()

    init {
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val llista = snapshot.children.mapNotNull { it.getValue(M18_Artista::class.java) }
                _artistes.value = llista
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun afegirArtista(nom: String, genere: String) {
        val id = dbRef.push().key ?: return
        dbRef.child(id).setValue(M18_Artista(id, nom, genere))
    }
}
```

### Observació Reactiva a la Vista
L'Activity recol·lecta el `StateFlow` utilitzant `repeatOnLifecycle` per garantir que només s'actualitza quan la vista està activa:

```kotlin
lifecycleScope.launch {
    repeatOnLifecycle(Lifecycle.State.STARTED) {
        viewModel.artistes.collect { llista ->
            binding.llistaArtistes.adapter = M18_Llista(this@M18_Firebase_Artistes, llista.toMutableList())
        }
    }
}
```

---
[M17](M17-Fragments) | [Home](Home) | [M19](M19-Animacio-Descriptor)
