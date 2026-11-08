# M16 – Lectura XML amb Coroutines, RecyclerView i ListAdapter

## Objectiu
Llegir i processar un fitxer XML contingut en els assets de l'aplicació en un fil secundari amb **Kotlin Coroutines** (`lifecycleScope`), i mostrar els resultats en un **`RecyclerView`** amb **`ListAdapter`** i **`DiffUtil`**.

## Fitxers Involucrats
| Fitxer | Descripció |
|--------|------------|
| `M16_LecturaXMLActivity.kt` | Activity amb RecyclerView, ListAdapter i Coroutines |
| `assets/m16_curs.xml` | Fitxer XML amb dades |

## Conceptes Clau

### Parsing Asíncron amb Coroutines
El parsing XML d'assets s'executa a `Dispatchers.IO` i els resultats es publiquen al fil principal (`Dispatchers.Main`):

```kotlin
lifecycleScope.launch(Dispatchers.IO) {
    val lecturaXML = M16_LecturaXMLUtility()
    val alumnes = assets.open("m16_curs.xml").use { inputStream ->
        lecturaXML.analitzarXML(inputStream)
    }
    val llistaNoms = alumnes.map { it.toString() }

    withContext(Dispatchers.Main) {
        adapter.submitList(llistaNoms)
    }
}
```

### RecyclerView + ListAdapter + DiffUtil
`ListAdapter` gestiona les diferències de la llista de manera eficient i animada sense bloquejar la interfície:

```kotlin
private class AlumneAdapter(
    private val onItemClick: (String) -> Unit
) : ListAdapter<String, AlumneAdapter.AlumneViewHolder>(AlumneDiffCallback) { ... }

private object AlumneDiffCallback : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String) = oldItem == newItem
    override fun areContentsTheSame(oldItem: String, newItem: String) = oldItem == newItem
}
```

---
[M15](M15-OpenGL-ES-20) | [Home](Home) | [M17](M17-Fragments)
