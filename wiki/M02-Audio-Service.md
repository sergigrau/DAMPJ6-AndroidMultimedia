# M02 – Background Audio with Service

## Objective
Learn how to play audio in the background using an Android `Service`, so the music keeps playing even when the user closes the Activity or navigates to another application.

## Files Involved
| File | Description |
|------|-------------|
| `M02_AudioIntentService.kt` | Service that manages the MediaPlayer |
| `M02_AudioServiceActivity.kt` | Activity that sends commands to the Service |
| `res/layout/m01_audio_principal.xml` | Layout reused from M01 |

## Key Concepts

### Service vs Activity
| | Activity | Service |
|---|---|---|
| **Visible** | Yes | No |
| **Lifecycle** | Tied to the UI | Independent |
| **Background audio** | No | Yes |
| **Destroyed on exit** | Yes | Configurable |

### Service Structure

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

### Activity to Service Communication

The Activity sends commands to the Service by passing `extras` through an `Intent`:

```kotlin
// Start the Service with an operation
val intent = Intent(this, M02_AudioIntentService::class.java)
intent.putExtra("operacio", "inici")
startService(intent)

// Change operation
intent.putExtra("operacio", "pausa")
startService(intent)
```

### Return values of `onStartCommand`
| Constant | Behaviour |
|----------|-----------|
| `START_STICKY` | The system restarts the Service if it is killed (intent = null) |
| `START_NOT_STICKY` | The system does NOT restart the Service if it is killed |
| `START_REDELIVER_INTENT` | Restarts and redelivers the last Intent |

### Manifest Declaration
```xml
<service android:name=".M02_AudioIntentService" />
```

## Communication Diagram

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

## Differences from M01
- In M01, the MediaPlayer lives in the Activity and stops when the Activity is closed.
- In M02, the MediaPlayer lives in the Service and keeps playing in the background.

---
[M01](M01-Audio-MediaPlayer) | [Home](Home) | [M03](M03-ExecutorService)
