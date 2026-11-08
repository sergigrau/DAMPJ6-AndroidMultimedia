# M03 – Tasques en Segon Pla amb Kotlin Coroutines i lifecycleScope

## Objectiu
Aprendre a executar tasques asíncrones en segon pla utilitzant **Kotlin Coroutines** combinat amb **`lifecycleScope`** de Jetpack Lifecycle, garantint la cancel·lació automàtica i la seguretat de la IU.

## Fitxers Involucrats
| Fitxer | Descripció |
|--------|------------|
| `M03_ExecutorServiceActivity.kt` | Activity amb Coroutines i ViewBinding |

## Conceptes Clau

### Kotlin Coroutines + lifecycleScope
En lloc de l'antic `ExecutorService` o `AsyncTask`, la recomanació moderna de Jetpack és utilitzar Coroutines:

```kotlin
private fun executarTascaCorrutina() {
    lifecycleScope.launch(Dispatchers.IO) {
        // Càlcul asíncron en fil secundari (IO)
        val resultat = (comptador++).toString()

        // Canvi al fil principal per actualitzar la IU
        withContext(Dispatchers.Main) {
            valors.add(resultat)
            adaptador.notifyDataSetChanged()
        }
    }
}
```

### Avantatges de lifecycleScope
- **Seguretat de Cicle de Vida:** Si l'Activitat es destrueix, totes les corrutines iniciades a `lifecycleScope` es cancel·len automàticament.
- **Sense fuites de memòria:** Evita que fils residuals mantinguin referències a l'Activity.

---
[M02](M02-Audio-Service) | [Home](Home) | [M04](M04-Notificacions-SnackBar)
