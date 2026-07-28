# M02 – Àudio en segon pla amb Service

## 🎯 Objectiu
Aprendre a reproduir àudio en segon pla mitjançant un `Service` Android, de manera que la música continuï sonant encara que l'usuari tanqui l'Activity o navegui a una altra aplicació.

## 📄 Fitxers implicats
| Fitxer | Descripció |
|--------|-----------|
| `M02_AudioIntentService.kt` | Service que gestiona el MediaPlayer |
| `M02_AudioServiceActivity.kt` | Activity que envia ordres al Service |
| `res/layout/m01_audio_principal.xml` | Layout reutilitzat de M01 |

## 🔑 Conceptes clau

### Service vs Activity
| | Activity | Service |
|---|---|---|
| **Visible** | Sí | No |
| **Cicle de vida** | Lligat a la UI | Independent |
| **Àudio en segon pla** | ❌ | ✅ |
| **Destrucció al sortir** | Sí | Configurable |

### Estructura del Service

```kotlin
class M02_AudioIntentService : Service() {

    private var mp: MediaPlayer? = null

    override fun onCreate() {
        mp = MediaPlayer.create(this, R.raw.m02_audio1)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.getStringExtra("operacio")?.let { operacio ->
            when (operacio) {
                "inici" -> if (mp?.isPlaying == false) mp?.start()
                "pausa" -> if (mp?.isPlaying == true) mp?.pause()
                "salta" -> mp?.seekTo(10000)
            }
        }
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder? = null

    override fun onDestroy() {
        mp?.release()
        mp = null
    }
}
```

### Comunicació Activity → Service

L'Activity envia ordres al Service passant `extras` a través d'un `Intent`:

```kotlin
// Iniciar el Service amb una operació
val intent = Intent(this, M02_AudioIntentService::class.java)
intent.putExtra("operacio", "inici")
startService(intent)

// Canviar operació
intent.putExtra("operacio", "pausa")
startService(intent)
```

### Valors de retorn d'`onStartCommand`
| Constant | Comportament |
|----------|-------------|
| `START_STICKY` | El sistema reinicia el Service si el mata (intent = null) |
| `START_NOT_STICKY` | El sistema **no** reinicia el Service si el mata |
| `START_REDELIVER_INTENT` | Reinicia i reentrega l'últim Intent |

### Declaració al Manifest
```xml
<service android:name=".M02_AudioIntentService" />
```

## 🔄 Diagrama de comunicació

```
M02_AudioServiceActivity
        │
        │  startService(intent.putExtra("operacio","inici"))
        ▼
M02_AudioIntentService
        │
        ├─► "inici" → mp.start()
        ├─► "pausa" → mp.pause()
        └─► "salta" → mp.seekTo(10000)
```

## ⚠️ Diferències amb M01
- A M01, el MediaPlayer viu a l'Activity → s'atura quan es tanca.
- A M02, el MediaPlayer viu al Service → continua reproduint en segon pla.

---
[← M01](M01-Audio-MediaPlayer) | [Inici](Home) | [M03 →](M03-ExecutorService)

