# M21 – Servei de càlcul amb BroadcastReceiver

## Objectiu
Aprendre a implementar un `Service` que realitza un càlcul (suma de dos nombres) en un fil secundari i comunica el resultat a l'Activitat mitjançant un `BroadcastReceiver`. Demostra el patró de comunicació bidireccional entre una Activitat i un Servei.

## Fitxers involucrats
| Fitxer | Descripció |
|--------|------------|
| `M21_SumaServeiActivity.kt` | Activitat amb formulari i receptor de resultats |
| `M21_SumaService.kt` | Servei que calcula la suma en segon pla |
| `res/layout/m21_activity_suma_servei.xml` | Disseny amb camps i resultat |

## Conceptes clau

### Arquitectura de comunicació

```
[M21_SumaServeiActivity]
    |  startService(intent amb n1, n2)
    v
[M21_SumaService]
    |  ExecutorService.execute { resultat = n1 + n2 }
    |  sendBroadcast(intent amb resultat)
    v
[BroadcastReceiver (dins de l'Activitat)]
    |  onReceive() -> actualitzar TextView
    v
[UI actualitzada]
```

### Servei amb ExecutorService

```kotlin
class M21_SumaService : Service() {

    private val executor = Executors.newSingleThreadExecutor()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            val n1 = it.getIntExtra("n1", 0)
            val n2 = it.getIntExtra("n2", 0)

            executor.execute {
                val result = n1 + n2  // Càlcul en un fil secundari
                val resultIntent = Intent("edu.fje.smx8.SUMA_RESULTAT")
                resultIntent.putExtra("suma", result)
                sendBroadcast(resultIntent)  // Enviar resultat com a Broadcast
                stopSelf(startId)            // Aturar el Servei en acabar
            }
        }
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder? = null

    override fun onDestroy() {
        executor.shutdown()
    }
}
```

### BroadcastReceiver a l'Activitat

```kotlin
private lateinit var resultReceiver: BroadcastReceiver

// Definir el receptor
resultReceiver = object : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val res = intent.getIntExtra("suma", 0)
        resultView.text = res.toString()  // Actualitzar la UI
    }
}

// Registrar el receptor (API 33+: cal especificar EXPORTED o NOT_EXPORTED)
registerReceiver(
    resultReceiver,
    IntentFilter("edu.fje.smx8.SUMA_RESULTAT"),
    Context.RECEIVER_EXPORTED
)

// Desregistrar quan l'Activitat es destrueixi
override fun onDestroy() {
    super.onDestroy()
    unregisterReceiver(resultReceiver)
}
```

### Iniciar el Servei des de l'Activitat

```kotlin
buttonSum.setOnClickListener {
    val intent = Intent(this, M21_SumaService::class.java).apply {
        putExtra("n1", n1Field.text.toString().toInt())
        putExtra("n2", n2Field.text.toString().toInt())
    }
    startService(intent)
}
```

### Context.RECEIVER_EXPORTED (API 33+)
A partir d'Android 14 (API 34), és **obligatori** especificar si el receptor pot rebre broadcasts d'altres aplicacions:

| Flag | Descripció |
|------|-------------|
| `Context.RECEIVER_EXPORTED` | Pot rebre broadcasts d'altres apps |
| `Context.RECEIVER_NOT_EXPORTED` | Només broadcasts de la mateixa app |

### Declaració al Manifest
```xml
<service android:name=".M21_SumaService"
         android:exported="false" />
```

## Comparativa: BroadcastReceiver vs Altres alternatives

| Mecanisme | Avantatge | Inconvenient |
|-----------|-----------|--------------|
| **BroadcastReceiver** | Sencill, desacoblat | Asíncron, no garantit |
| **ResultReceiver** | Directe, tipat | Més complex |
| **LiveData/Flow** | Reactiu, conscient del cicle de vida | Requereix ViewModel |
| **Bound Service** | Bidireccional | Més complex de gestionar |
| **Coroutines** | Modern, llegible | Requereix Kotlin Coroutines |

## Resum del cicle de vida d'un Servei

```
startService()
    |
    v
onCreate()        <- es crida només una vegada
    |
    v
onStartCommand()  <- es crida cada vegada que s'inicia el servei
    |
    | [tasca completada]
    v
stopSelf() / stopService()
    |
    v
onDestroy()
```

---
[M20](M20-Animacio-Sprite) | [Home](Home)
