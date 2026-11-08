# M02 – Àudio en Segon Pla amb Jetpack Media3 MediaSessionService

## Objectiu
Aprendre a reproduir àudio en segon pla utilitzant **Jetpack Media3 `MediaSessionService`** i **`MediaSession`**, permetent mantenir la reproducció activa en segon pla amb integració del cicle de vida modern de Media3.

## Fitxers Involucrats
| Fitxer | Descripció |
|--------|------------|
| `M02_AudioIntentService.kt` | Servei heretat de `MediaSessionService` |
| `M02_AudioServiceActivity.kt` | Activity que envia ordres al servei |

## Conceptes Clau

### MediaSessionService
En l'ecosistema de **Jetpack Media3**, els serveis d'àudio hereten de `MediaSessionService`. Això permet publicar una `MediaSession` vinculada a un `ExoPlayer`:

```kotlin
class M02_AudioIntentService : MediaSessionService() {

    private var player: ExoPlayer? = null
    private var mediaSession: MediaSession? = null

    override fun onCreate() {
        super.onCreate()
        val p = ExoPlayer.Builder(this).build().apply {
            val uri = Uri.parse("android.resource://$packageName/${R.raw.m02_audio1}")
            setMediaItem(MediaItem.fromUri(uri))
            prepare()
        }
        player = p
        mediaSession = MediaSession.Builder(this, p).build()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }
}
```

### Declaració al Manifest
Els serveis de Media3 requereixen l'acció de filtre d'intent `MediaSessionService`:

```xml
<service
    android:name=".M02_AudioIntentService"
    android:exported="false">
    <intent-filter>
        <action android:name="androidx.media3.session.MediaSessionService" />
    </intent-filter>
</service>
```

---
[M01](M01-Audio-MediaPlayer) | [Home](Home) | [M03](M03-ExecutorService)
