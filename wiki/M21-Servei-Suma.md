# M21 – Servei de Càlcul amb Jetpack WorkManager

## Objectiu
Aprendre a realitzar càlculs asíncrons en segon pla utilitzant **Jetpack WorkManager** (`CoroutineWorker`) i rebre els resultats de forma reactiva observant el **`WorkInfo` LiveData**.

## Fitxers Involucrats
| Fitxer | Descripció |
|--------|------------|
| `M21_SumaWorker.kt` | `CoroutineWorker` que realitza el càlcul de la suma |
| `M21_SumaServeiActivity.kt` | Activity que encola la tasca i observa el resultat |

## Conceptes Clau

### CoroutineWorker amb Dades d'Entrada i Sortida
El Worker rep paràmetres d'entrada (`n1` i `n2`) a través d'`inputData`, realitza la suma de manera asíncrona i retorna el resultat a través de `workDataOf`:

```kotlin
class M21_SumaWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val n1 = inputData.getInt("n1", 0)
        val n2 = inputData.getInt("n2", 0)
        val suma = n1 + n2

        val outputData = workDataOf("suma" to suma)
        return Result.success(outputData)
    }
}
```

### Observació Reactiva de WorkInfo
A l'Activitat, enlloc de `BroadcastReceiver`s manuals, s'enclava la petició i s'observa l'estat del treball amb `getWorkInfoByIdLiveData`:

```kotlin
binding.botoSuma.setOnClickListener {
    val num1 = binding.numero1.text.toString().toIntOrNull() ?: 0
    val num2 = binding.numero2.text.toString().toIntOrNull() ?: 0

    val workRequest = OneTimeWorkRequestBuilder<M21_SumaWorker>()
        .setInputData(workDataOf("n1" to num1, "n2" to num2))
        .build()

    val workManager = WorkManager.getInstance(this)
    workManager.enqueue(workRequest)

    workManager.getWorkInfoByIdLiveData(workRequest.id).observe(this) { workInfo ->
        if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED) {
            val resultatSuma = workInfo.outputData.getInt("suma", 0)
            binding.resultat.text = resultatSuma.toString()
        }
    }
}
```

---
[M20](M20-Animacio-Sprite) | [Home](Home)
