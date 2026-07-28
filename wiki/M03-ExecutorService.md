# M03 – Tasques en segon pla amb ExecutorService

## 🎯 Objectiu
Aprendre a executar tasques pesades en un fil secundari sense bloquejar la interfície d'usuari, utilizant `ExecutorService` (substitut modern de l'`AsyncTask` deprecated a API 30). Es combina amb animacions `ObjectAnimator` i `AnimatorSet`.

## 📄 Fitxers implicats
| Fitxer | Descripció |
|--------|-----------|
| `M03_ExecutorServiceActivity.kt` | Activity principal |
| `res/layout/m03_asyntask.xml` | Layout amb Button i ListView |

## 🔑 Conceptes clau

### Per què ExecutorService?
`AsyncTask` va ser deprecat a **Android 11 (API 30)** per problemes de fuita de memòria i comportament inconsistent en canvis de configuració. `ExecutorService` és l'alternativa recomanada:

```kotlin
// Creació d'un executor d'un sol fil
private val executor: ExecutorService = Executors.newSingleThreadExecutor()

// Execució d'una tasca en segon pla
executor.execute {
    // Codi que s'executa en fil secundari
    val resultat = calcularAlgoCostós()
    
    // Actualitzar la UI → sempre al fil principal
    runOnUiThread {
        llistaValors.add(resultat)
        adaptador.notifyDataSetChanged()
    }
}
```

### Regla d'or: UI sempre al fil principal
Android no permet modificar la UI des de fils secundaris. Qualsevol actualització visual cal fer-la amb `runOnUiThread { }`:

```kotlin
executor.execute {
    val resultat = comptador++.toString()   // ← fil secundari ✅
    runOnUiThread {
        valors.add(resultat)                // ← fil principal ✅
        adaptador.notifyDataSetChanged()    // ← fil principal ✅
    }
}
```

### WindowMetrics (API 30+)
Per obtenir les dimensions de la pantalla sense el mètode deprecated `getDefaultDisplay()`:

```kotlin
val bounds: Rect = windowManager.currentWindowMetrics.bounds
val amplada = bounds.width()
val alcada  = bounds.height()
```

### AnimatorSet – Animació seqüencial del botó
```kotlin
val animacio1 = ObjectAnimator.ofFloat(boto, "x", 0f, amplada.toFloat())
val animacio2 = ObjectAnimator.ofFloat(boto, "y", 0f, alcada.toFloat())
// ...

val animacio = AnimatorSet()
animacio.playSequentially(animacio1, animacio2, animacio3, animacio4)
animacio.addListener(object : AnimatorListenerAdapter() {
    override fun onAnimationEnd(a: Animator) {
        executarTasca()   // Nova tasca quan acaba l'animació
        animacio.start()  // Reinicia l'animació en bucle
    }
})
animacio.start()
```

### Tancament correcte de l'executor
```kotlin
override fun onDestroy() {
    super.onDestroy()
    executor.shutdown()  // Allibera els recursos del pool de fils
}
```

## 🔄 Flux d'execució

```
Clic al botó
    │
    ▼
Inicia AnimatorSet (4 animacions seqüencials)
    │
    ▼
onAnimationEnd()
    │
    ├─► executor.execute { ← fil secundari
    │       val resultat = comptador++
    │       runOnUiThread {
    │           llista.add(resultat)  ← fil UI
    │           adapter.notifyDataSetChanged()
    │       }
    │   }
    │
    └─► animacio.start()  ← torna a començar
```

## ⚙️ Tipus d'executors disponibles
| Factory | Descripció |
|---------|-----------|
| `newSingleThreadExecutor()` | 1 sol fil, tasques en cua |
| `newFixedThreadPool(n)` | Pool de n fils |
| `newCachedThreadPool()` | Pool dinàmic, crea fils si cal |
| `newScheduledThreadPool(n)` | Per tasques programades |

---
[← M02](M02-Audio-Service) | [Inici](Home) | [M04 →](M04-Notificacions-SnackBar)

