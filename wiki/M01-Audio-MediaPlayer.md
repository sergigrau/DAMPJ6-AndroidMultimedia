# M01 – Reproducció d'Àudio amb Jetpack Media3 ExoPlayer

## Objectiu
Aprendre a reproduir un fitxer d'àudio des d'una Activitat utilitzant **Jetpack Media3 ExoPlayer**, gestionant automàticament el focus d'àudio del sistema i la interfície amb **ViewBinding**.

## Fitxers Involucrats
| Fitxer | Descripció |
|--------|------------|
| `M01_AudioActivity.kt` | Activity principal amb controls de reproducció Media3 |
| `res/raw/m02_audio1` | Fitxer d'àudio a reproduir |
| `res/layout/m01_audio_principal.xml` | Layout amb Toolbar, SeekBar i FloatingActionButton |

## Conceptes Clau

### Jetpack Media3 ExoPlayer
**Jetpack Media3** és la suite recomanada oficialment per Google per a reproducció multimèdia. Substitueix el vell `MediaPlayer` per un motor modern, extensible i robust:

```kotlin
val audioAttributes = AudioAttributes.Builder()
    .setUsage(C.USAGE_MEDIA)
    .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
    .build()

player = ExoPlayer.Builder(this)
    .setAudioAttributes(audioAttributes, true) // true = gestió automàtica d'AudioFocus
    .build().apply {
        val uri = Uri.parse("android.resource://$packageName/${R.raw.m02_audio1}")
        setMediaItem(MediaItem.fromUri(uri))
        prepare()
    }
```

### Gestió Automàtica d'AudioFocus
A diferència de `MediaPlayer`, passar `true` a `setAudioAttributes(audioAttributes, handleAudioFocus = true)` fa que Media3 gestioni automàticament les pauses, reduccions de volum (ducking) i meandres del focus d'àudio quan hi ha trucades o altres apps reproduint so.

### Escoltar Esdeveniments de Reproducció (Player.Listener)
```kotlin
player?.addListener(object : Player.Listener {
    override fun onPlaybackStateChanged(state: Int) {
        if (state == Player.STATE_READY) {
            binding.barraProgress.max = duration.toInt()
        }
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        val iconRes = if (isPlaying) android.R.drawable.ic_media_pause else android.R.drawable.ic_media_play
        binding.fab.setImageResource(iconRes)
    }
})
```

---
[Home](Home) | [M02](M02-Audio-Service)
