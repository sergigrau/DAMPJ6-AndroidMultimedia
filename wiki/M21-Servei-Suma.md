# M21 – Calculation Service with BroadcastReceiver

## Objective
Learn how to implement a `Service` that performs a calculation (sum of two numbers) on a secondary thread and communicates the result to the Activity through a `BroadcastReceiver`. Demonstrates the bidirectional communication pattern between an Activity and a Service.

## Files Involved
| File | Description |
|------|-------------|
| `M21_SumaServeiActivity.kt` | Activity with form and result receiver |
| `M21_SumaService.kt` | Service that calculates the sum in the background |
| `res/layout/m21_activity_suma_servei.xml` | Layout with fields and result |

## Key Concepts

### Communication Architecture

```
[M21_SumaServeiActivity]
    |  startService(intent with n1, n2)
    v
[M21_SumaService]
    |  ExecutorService.execute { result = n1 + n2 }
    |  sendBroadcast(intent with result)
    v
[BroadcastReceiver (inside the Activity)]
    |  onReceive() -> update TextView
    v
[UI updated]
```

### Service with ExecutorService

```kotlin
class M21_SumaService : Service() {

    private val executor = Executors.newSingleThreadExecutor()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            val n1 = it.getIntExtra("n1", 0)
            val n2 = it.getIntExtra("n2", 0)

            executor.execute {
                val result = n1 + n2  // Calculation on a secondary thread
                val resultIntent = Intent("edu.fje.smx8.SUMA_RESULTAT")
                resultIntent.putExtra("suma", result)
                sendBroadcast(resultIntent)  // Send result as a Broadcast
                stopSelf(startId)            // Stop the Service when done
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

### BroadcastReceiver in the Activity

```kotlin
private lateinit var resultReceiver: BroadcastReceiver

// Define the receiver
resultReceiver = object : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val res = intent.getIntExtra("suma", 0)
        resultView.text = res.toString()  // Update the UI
    }
}

// Register the receiver (API 33+: must specify EXPORTED or NOT_EXPORTED)
registerReceiver(
    resultReceiver,
    IntentFilter("edu.fje.smx8.SUMA_RESULTAT"),
    Context.RECEIVER_EXPORTED
)

// Unregister when the Activity is destroyed
override fun onDestroy() {
    super.onDestroy()
    unregisterReceiver(resultReceiver)
}
```

### Starting the Service from the Activity

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
From Android 14 (API 34), it is **mandatory** to specify whether the receiver can receive broadcasts from other applications:

| Flag | Description |
|------|-------------|
| `Context.RECEIVER_EXPORTED` | Can receive broadcasts from other apps |
| `Context.RECEIVER_NOT_EXPORTED` | Only broadcasts from the same app |

### Manifest Declaration
```xml
<service android:name=".M21_SumaService"
         android:exported="false" />
```

## Comparison: BroadcastReceiver vs Other Alternatives

| Mechanism | Advantage | Disadvantage |
|-----------|-----------|-------------|
| **BroadcastReceiver** | Simple, decoupled | Asynchronous, not guaranteed |
| **ResultReceiver** | Direct, typed | More complex |
| **LiveData/Flow** | Reactive, lifecycle-aware | Requires ViewModel |
| **Bound Service** | Bidirectional | More complex to manage |
| **Coroutines** | Modern, readable | Requires Kotlin Coroutines |

## Service Lifecycle Summary

```
startService()
    |
    v
onCreate()        <- called only once
    |
    v
onStartCommand()  <- called every time the service is started
    |
    | [task completed]
    v
stopSelf() / stopService()
    |
    v
onDestroy()
```

---
[M20](M20-Animacio-Sprite) | [Home](Home)
