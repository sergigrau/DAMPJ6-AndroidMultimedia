# M02 – Àudio en Segon Pla amb Service

## Objectiu
Aprendre a reproduir àudio en segon pla utilitzant un `Service` d'Android, de manera que la música continuï sonant fins i tot quan l'usuari tanca l'Activity o navega cap a una altra aplicació.

## Fitxers Involucrats
| Fitxer | Descripció |
|--------|------------|
| `M02_AudioIntentService.kt` | Servei que gestiona el MediaPlayer |
| `M02_AudioServiceActivity.kt` | Activity que envia ordres al Servei |
| `res/layout/m01_audio_principal.xml` | Layout reutilitzat de M01 |

## Conceptes Clau

### Service vs Activity
| | Activity | Service |
|---|---|---|
| **Visible** | Sí | No |
| **Cicle de vida** | Lligat a la IU | Independent |
| **Àudio en segon pla** | No | Sí |
| **Destruït en sortir** | Sí | Configurable |

### Estructura del Servei

```kotlin
class M02_AudioIntentService : Service() {

    private var mp: MediaPlayer? = null

    override fun onCreate() {
        mp = MediaPlayer.create(this, R.raw.m02_audio1)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.getStringExtra("operacio")?.let { operation ->
            when (operation) {
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

### Comunicació d'Activity a Service

L'Activity envia ordres al Servei passant `extras` a través d'un `Intent`:

```kotlin
// Inicia el Servei amb una operació
val intent = Intent(this, M02_AudioIntentService::class.java)
intent.putExtra("operacio", "inici")
startService(intent)

// Canvia l'operació
intent.putExtra("operacio", "pausa")
startService(intent)
```

### Valors de retorn de `onStartCommand`
| Constant | Comportament |
|----------|--------------|
| `START_STICKY` | El sistema reinicia el Servei si es destrueix (intent = null) |
| `START_NOT_STICKY` | El sistema NO reinicia el Servei si es destrueix |
| `START_REDELIVER_INTENT` | Reinicia i torna a lliurar l'últim Intent |

### Declaració al Manifest
```xml
<service android:name=".M02_AudioIntentService" />
```

## Diagrama de Comunicació

```
M02_AudioServiceActivity
        |
        |  startService(intent.putExtra("operacio","inici"))
        v
M02_AudioIntentService
        |
        +-- "inici" -> mp.start()
        +-- "pausa" -> mp.pause()
        +-- "salta" -> mp.seekTo(10000)
```

## Diferències respecte a M01
- A M01, el MediaPlayer viu a l'Activity i s'atura quan es tanca l'Activity.
- A M02, el MediaPlayer viu al Servei i continua sonant en segon pla.

---
[M01](M01-Audio-MediaPlayer) | [Home](Home) | [M03](M03-ExecutorService)
