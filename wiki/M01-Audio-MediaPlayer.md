# M01 – Audio Playback with MediaPlayer

## Objective
Learn how to play an audio file from an Activity using `MediaPlayer` and manage the system audio focus to coexist correctly with other applications that use audio.

## Files Involved
| File | Description |
|------|-------------|
| `M01_AudioActivity.kt` | Main activity with playback controls |
| `res/raw/m02_audio1` | Audio file to play |
| `res/layout/m01_audio_principal.xml` | Layout with Toolbar, SeekBar and FloatingActionButton |
| `res/menu/m01_menu.xml` | Options menu |

## Key Concepts

### MediaPlayer
`MediaPlayer` is the main Android class for playing audio and video. In this exercise it is created from a `raw` resource:

```kotlin
mp = MediaPlayer.create(this, R.raw.m02_audio1)
```

**Main methods:**
| Method | Description |
|--------|-------------|
| `start()` | Starts or resumes playback |
| `pause()` | Pauses playback |
| `stop()` | Stops playback |
| `release()` | Releases system resources |
| `seekTo(ms)` | Jumps to the given position (in ms) |
| `isPlaying` | Returns `true` if currently playing |
| `duration` | Total duration in ms |

### AudioFocus
Audio focus management is necessary to respect system behaviour when other apps want to use audio (e.g. an incoming call):

```kotlin
val audioFocusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK)
    .setAudioAttributes(audioAttributes)
    .setOnAudioFocusChangeListener(mAudioFocusListener)
    .build()

val result = am.requestAudioFocus(audioFocusRequest)
```

**Focus events:**
| Constant | Recommended action |
|----------|--------------------|
| `AUDIOFOCUS_GAIN` | Resume playback at normal volume |
| `AUDIOFOCUS_LOSS` | Stop and release the MediaPlayer |
| `AUDIOFOCUS_LOSS_TRANSIENT` | Pause temporarily |
| `AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK` | Lower volume (`setVolume(0.5f, 0.5f)`) |

### SeekBar
Allows the user to jump to any point in the track:

```kotlin
barraProgress.max = mp!!.duration
barraProgress.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
    override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
        mp?.seekTo(barraProgress.progress)
    }
    // ...
})
```

## Flow Diagram

```
onCreate()
    |
    +-- Create MediaPlayer (R.raw.m02_audio1)
    +-- Request AudioFocus
    |       |
    |       +-- GRANTED -> mp.start()
    |       +-- FAILED  -> mp.stop()
    |
    +-- FAB click
            +-- isPlaying -> pause()
            +-- !isPlaying -> start()
```

## Required Permissions
None. The audio file is an internal application resource (`res/raw/`).

## Important Notes
- Always release the `MediaPlayer` when not in use (`release()`) to avoid memory leaks.
- `MediaPlayer.create()` prepares the player automatically (calls `prepare()` internally).
- `AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK` means the audio can coexist with other sounds at reduced volume.

---
[Home](Home) | [M02](M02-Audio-Service)
