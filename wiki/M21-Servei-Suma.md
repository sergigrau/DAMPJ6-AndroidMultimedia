# M21 – Servei de càlcul amb BroadcastReceiver

## 🎯 Objectiu
Aprendre a implementar un `Service` que realitza un càlcul (suma de dos nombres) en un fil secundari i comunica el resultat a l'Activity a través d'un `BroadcastReceiver`. Demostra el patró de comunicació bidireccional entre Activity i Service.

## 📄 Fitxers implicats
| Fitxer | Descripció |
|--------|-----------|
| `M21_SumaServeiActivity.kt` | Activity amb formulari i receptor del resultat |
| `M21_SumaService.kt` | Service que calcula la suma en segon pla |
| `res/layout/m21_activity_suma_servei.xml` | Layout amb camps i resultat |

## 🔑 Conceptes clau

### Arquitectura de la comunicació

```
[M21_SumaServeiActivity]
    │  startService(intent amb n1, n2)
    ▼
[M21_SumaService]
    │  ExecutorService.execute { result = n1 + n2 }
    │  sendBroadcast(intent amb resultat)
    ▼
[BroadcastReceiver (dins l'Activity)]
    │  onReceive() → actualitzar TextView
    ▼
[UI actualitzada]
```

### Service amb ExecutorService

```kotlin
class M21_SumaService : Service() {

    private val executor = Executors.newSingleThreadExecutor()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            val n1 = it.getIntExtra("n1", 0)
            val n2 = it.getIntExtra("n2", 0)

            executor.execute {
                val result = n1 + n2  // Càlcul en fil secundari
                val resultIntent = Intent("edu.fje.smx8.SUMA_RESULTAT")
                resultIntent.putExtra("suma", result)
                sendBroadcast(resultIntent)  // Enviar resultat com a Broadcast
                stopSelf(startId)            // Aturar el Service quan acaba
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

### BroadcastReceiver a l'Activity

```kotlin
private lateinit var resultatReceiver: BroadcastReceiver

// Definir el receiver
resultatReceiver = object : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val res = intent.getIntExtra("suma", 0)
        resultat.text = res.toString()  // Actualitza la UI
    }
}

// Registrar el receiver (API 33+: cal especificar EXPORTED o NOT_EXPORTED)
registerReceiver(
    resultatReceiver,
    IntentFilter("edu.fje.smx8.SUMA_RESULTAT"),
    Context.RECEIVER_EXPORTED  // Pot rebre broadcasts d'altres apps
)

// Desregistrar quan es destrueix l'Activity
override fun onDestroy() {
    super.onDestroy()
    unregisterReceiver(resultatReceiver)
}
```

### Llançar el Service des de l'Activity

```kotlin
findViewById<View>(R.id.botoSuma).setOnClickListener {
    val intent = Intent(this, M21_SumaService::class.java).apply {
        putExtra("n1", n1.text.toString().toInt())
        putExtra("n2", n2.text.toString().toInt())
    }
    startService(intent)
}
```

### EdgeToEdge – Disseny pantalla completa
```kotlin
EdgeToEdge.enable(this)
ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
    val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
    v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
    insets
}
```

`EdgeToEdge` permet que el contingut s'estengui per darrere de les barres del sistema (status bar, navigation bar), amb el padding adequat per no tapar contingut important.

### Context.RECEIVER_EXPORTED (API 33+)
Des d'Android 14 (API 34), és **obligatori** especificar si el receiver pot rebre broadcasts d'altres aplicacions:

| Flag | Descripció |
|------|-----------|
| `Context.RECEIVER_EXPORTED` | Pot rebre broadcasts d'altres apps |
| `Context.RECEIVER_NOT_EXPORTED` | Només broadcasts de la mateixa app |

### Declaració al Manifest
```xml
<service android:name=".M21_SumaService"
         android:exported="false" />
```

## 📊 Comparativa: BroadcastReceiver vs altres alternatives

| Mecanisme | Avantatge | Inconvenient |
|-----------|-----------|-------------|
| **BroadcastReceiver** | Simple, desacoblat | Asíncron, no garantit |
| **ResultReceiver** | Directe, tipat | Més complex |
| **LiveData/Flow** | Reactiu, cicle de vida | Requereix ViewModel |
| **Bound Service** | Bidireccional | Més complex de gestionar |
| **Coroutines** | Modern, llegible | Requereix Kotlin Coroutines |

## 📋 Resum del cicle de vida del Service

```
startService()
    │
    ▼
onCreate()  ← es crida una sola vegada
    │
    ▼
onStartCommand()  ← es crida cada vegada que s'inicia el service
    │
    │ [tasca completada]
    ▼
stopSelf() / stopService()
    │
    ▼
onDestroy()
```

---
[← M20](M20-Animacio-Sprite) | [Inici](Home)

