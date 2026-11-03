# M03 – Tasques en Segon Pla amb ExecutorService

## Objectiu
Aprendre a executar tasques pesades en un fil secundari sense bloquejar la interfície d'usuari, utilitzant `ExecutorService` (el substitut modern de la classe `AsyncTask` deprecada a l'API 30). Combinat amb animacions `ObjectAnimator` i `AnimatorSet`.

## Fitxers Involucrats
| Fitxer | Descripció |
|--------|------------|
| `M03_ExecutorServiceActivity.kt` | Activity principal |
| `res/layout/m03_asyntask.xml` | Layout amb Button i ListView |

## Conceptes Clau

### Per què ExecutorService?
`AsyncTask` va ser deprecada a **Android 11 (API 30)** a causa de problemes de fuites de memòria i comportament inconsistent en canvis de configuració. `ExecutorService` és l'alternativa recomanada:

```kotlin
// Crea un executor d'un sol fil
private val executor: ExecutorService = Executors.newSingleThreadExecutor()

// Executa una tasca en un fil secundari
executor.execute {
    // Codi que s'executa en un fil secundari
    val result = performExpensiveCalculation()

    // Actualitza la IU -> sempre al fil principal
    runOnUiThread {
        valueList.add(result)
        adapter.notifyDataSetChanged()
    }
}
```

### Regla d'or: la IU sempre al fil principal
Android no permet modificar la IU des de fils secundaris. Qualsevol actualització visual s'ha de fer amb `runOnUiThread { }`:

```kotlin
executor.execute {
    val result = counter++.toString()   // fil secundari
    runOnUiThread {
        values.add(result)              // fil principal
        adapter.notifyDataSetChanged()  // fil principal
    }
}
```

### WindowMetrics (API 30+)
Per obtenir les dimensions de la pantalla sense el mètode deprecat `getDefaultDisplay()`:

```kotlin
val bounds: Rect = windowManager.currentWindowMetrics.bounds
val width  = bounds.width()
val height = bounds.height()
```

### AnimatorSet – Animació seqüencial de botons
```kotlin
val anim1 = ObjectAnimator.ofFloat(button, "x", 0f, width.toFloat())
val anim2 = ObjectAnimator.ofFloat(button, "y", 0f, height.toFloat())
// ...

val animSet = AnimatorSet()
animSet.playSequentially(anim1, anim2, anim3, anim4)
animSet.addListener(object : AnimatorListenerAdapter() {
    override fun onAnimationEnd(a: Animator) {
        runTask()       // Nova tasca quan acaba l'animació
        animSet.start() // Reinicia l'animació en bucle
    }
})
animSet.start()
```

### Tancament correcte de l'executor
```kotlin
override fun onDestroy() {
    super.onDestroy()
    executor.shutdown()  // Allibera els recursos del pool de fils
}
```

## Flux d'Execució

```
Clic al botó
    |
    v
Inicia AnimatorSet (4 animacions seqüencials)
    |
    v
onAnimationEnd()
    |
    +-- executor.execute {           <- fil secundari
    |       val result = counter++
    |       runOnUiThread {
    |           list.add(result)     <- fil de la IU
    |           adapter.notifyDataSetChanged()
    |       }
    |   }
    |
    +-- animSet.start()  <- reinicia
```

## Tipus d'Executors Disponibles
| Fàbrica | Descripció |
|---------|------------|
| `newSingleThreadExecutor()` | Un sol fil, tasques en cua |
| `newFixedThreadPool(n)` | Pool de n fils |
| `newCachedThreadPool()` | Pool dinàmic, crea fils segons calgui |
| `newScheduledThreadPool(n)` | Per a tasques programades |

---
[M02](M02-Audio-Service) | [Home](Home) | [M04](M04-Notificacions-SnackBar)
