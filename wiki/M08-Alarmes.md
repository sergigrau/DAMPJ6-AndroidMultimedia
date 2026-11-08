# M08 – Tasques Programades i Alarmes amb Jetpack WorkManager

## Objectiu
Aprendre a programar tasques temporitzades i en segon pla utilitzant **Jetpack WorkManager** (`CoroutineWorker`), que gestiona automàticament les restriccions del sistema, la bateria i el cicle de vida.

## Fitxers Involucrats
| Fitxer | Descripció |
|--------|------------|
| `M08_AlarmaWorker.kt` | Subclasse de `CoroutineWorker` que executa la tasca |
| `M08_AlarmaActivity.kt` | Activity que programa tasques puntuals i periòdiques |

## Conceptes Clau

### CoroutineWorker
Subclasse de WorkManager especialment dissenyada per a tasques asíncrones en Kotlin Coroutines:

```kotlin
class M08_AlarmaWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        // Codi que s'executa en segon pla
        fazerVibrar()
        reproducirSonido()
        return Result.success()
    }
}
```

### Programar amb OneTimeWorkRequestBuilder i PeriodicWorkRequestBuilder
```kotlin
// Tasca d'un sol tret amb retard inicial
val workRequest = OneTimeWorkRequestBuilder<M08_AlarmaWorker>()
    .setInitialDelay(5, TimeUnit.SECONDS)
    .build()

WorkManager.getInstance(this).enqueue(workRequest)

// Tasca periòdica
val periodicRequest = PeriodicWorkRequestBuilder<M08_AlarmaWorker>(15, TimeUnit.MINUTES)
    .build()

WorkManager.getInstance(this).enqueue(periodicRequest)
```

---
[M07](M07-Canvas-Path) | [Home](Home) | [M09](M09-Propietats-Dinamiques)
