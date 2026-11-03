# M01 – Reproducció d'Àudio amb MediaPlayer

## Objectiu
Aprendre a reproduir un fitxer d'àudio des d'una Activity utilitzant `MediaPlayer` i gestionar el focus d'àudio del sistema per coexistir correctament amb altres aplicacions que utilitzin àudio.

## Fitxers Involucrats
| Fitxer | Descripció |
|--------|------------|
| `M01_AudioActivity.kt` | Activity principal amb controls de reproducció |
| `res/raw/m02_audio1` | Fitxer d'àudio a reproduir |
| `res/layout/m01_audio_principal.xml` | Layout amb Toolbar, SeekBar i FloatingActionButton |
| `res/menu/m01_menu.xml` | Menú d'opcions |

## Conceptes Clau

### MediaPlayer
`MediaPlayer` és la classe principal d'Android per reproduir àudio i vídeo. En aquest exercici es crea a partir d'un recurs `raw`:

```kotlin
mp = MediaPlayer.create(this, R.raw.m02_audio1)
```

**Mètodes principals:**
| Mètode | Descripció |
|--------|------------|
| `start()` | Inicia o reprèn la reproducció |
| `pause()` | Pausa la reproducció |
| `stop()` | Atura la reproducció |
| `release()` | Allibera els recursos del sistema |
| `seekTo(ms)` | Salta a la posició indicada (en ms) |
| `isPlaying` | Retorna `true` si s'està reproduint |
| `duration` | Durada total en ms |

### AudioFocus
La gestió del focus d'àudio és necessària per respectar el comportament del sistema quan altres aplicacions volen utilitzar l'àudio (p. ex. una trucada entrant):

```kotlin
val audioFocusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK)
    .setAudioAttributes(audioAttributes)
    .setOnAudioFocusChangeListener(mAudioFocusListener)
    .build()

val result = am.requestAudioFocus(audioFocusRequest)
```

**Esdeveniments de focus:**
| Constant | Acció recomanada |
|----------|------------------|
| `AUDIOFOCUS_GAIN` | Reprendre la reproducció a volum normal |
| `AUDIOFOCUS_LOSS` | Aturar i alliberar el MediaPlayer |
| `AUDIOFOCUS_LOSS_TRANSIENT` | Pausar temporalment |
| `AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK` | Baixar el volum (`setVolume(0.5f, 0.5f)`) |

### SeekBar
Permet a l'usuari saltar a qualsevol punt de la pista:

```kotlin
barraProgress.max = mp!!.duration
barraProgress.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
    override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
        mp?.seekTo(barraProgress.progress)
    }
    // ...
})
```

## Diagrama de Flux

```
onCreate()
    |
    +-- Crea MediaPlayer (R.raw.m02_audio1)
    +-- Sol·licita AudioFocus
    |       |
    |       +-- CONCEDIT -> mp.start()
    |       +-- FALLADA  -> mp.stop()
    |
    +-- Clic al FAB
            +-- isPlaying -> pause()
            +-- !isPlaying -> start()
```

## Permisos Requerits
Cap. El fitxer d'àudio és un recurs intern de l'aplicació (`res/raw/`).

## Notes Importants
- Allibereu sempre el `MediaPlayer` quan no s'utilitzi (`release()`) per evitar fuites de memòria.
- `MediaPlayer.create()` prepara el reproductor automàticament (crida `prepare()` internament).
- `AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK` significa que l'àudio pot coexistir amb altres sons a volum reduït.

---
[Home](Home) | [M02](M02-Audio-Service)
