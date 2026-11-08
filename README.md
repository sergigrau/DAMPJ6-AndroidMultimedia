# DAM2.PJ6 – Android Multimedia i Serveis (Migrat a Jetpack)

> **Llenguatge:** Kotlin 2.2.10 · **SDK:** Android 16 (API 36) · **minSdk:** API 33 · **Jetpack Suite**

Col·lecció d'exercicis pràctics de desenvolupament d'aplicacions Android per al mòdul de Projecte del cicle formatiu DAM2, totalment migrats a la suite de llibreries i arquitectures modernes d'**Android Jetpack**.

## Documentació completa → [Wiki del projecte](wiki)

| Mòdul | Descripció | Component Jetpack / Concepte |
|-------|-----------|------------------------------|
| [M01](wiki/M01-Audio-MediaPlayer.md) | Reproducció d'àudio | **Jetpack Media3 ExoPlayer**, `AudioAttributes`, ViewBinding |
| [M02](wiki/M02-Audio-Service.md) | Àudio en segon pla | **Jetpack Media3 MediaSessionService**, `MediaSession` |
| [M03](wiki/M03-ExecutorService.md) | Tasques en segon pla | **Kotlin Coroutines**, `lifecycleScope`, `Dispatchers.IO` |
| [M04](wiki/M04-Notificacions-SnackBar.md) | Notificacions | `NotificationCompat`, `NotificationManagerCompat`, Material3 |
| [M05](wiki/M05-Canvas-Basic.md) | Canvas bàsic | `androidx.core.graphics.createBitmap`, ViewBinding |
| [M06](wiki/M06-Canvas-Personalitzat.md) | Vista personalitzada | `Custom View`, `withStyledAttributes` (Core KTX) |
| [M07](wiki/M07-Canvas-Path.md) | Dibuix lliure | `Path`, `MaskFilter`, ViewBinding |
| [M08](wiki/M08-Alarmes.md) | Alarmes i tasques | **Jetpack WorkManager**, `CoroutineWorker` |
| [M09](wiki/M09-Propietats-Dinamiques.md) | Vistes dinàmiques | **Jetpack ConstraintLayout**, `ConstraintSet` dinàmic |
| [M10](wiki/M10-Animacio-Basica.md) | Animació bàsica | `ObjectAnimator`, AndroidX ViewBinding |
| [M11](wiki/M11-Animacio-Requadre.md) | Animació seqüencial | `AnimatorSet`, AndroidX ViewBinding |
| [M12](wiki/M12-Camera-Intent.md) | Càmera per intent | **Activity Result API** (`TakePicturePreview`) |
| [M13](wiki/M13-Camera-API.md) | Càmera avançada | **Jetpack CameraX** (`ProcessCameraProvider`, `PreviewView`, `ImageCapture`) |
| [M14](wiki/M14-Imatge-SD.md) | Imatge de la galeria | **Activity Result API** (`PickVisualMedia` Photo Picker) |
| [M15](wiki/M15-OpenGL-ES-20.md) | OpenGL ES 2.0 | `GLSurfaceView`, **DefaultLifecycleObserver** |
| [M16](wiki/M16-Lectura-XML.md) | Lectura XML | **Coroutines**, **RecyclerView**, `ListAdapter`, `DiffUtil` |
| [M17](wiki/M17-Fragments.md) | Fragments | **Jetpack Fragment KTX** (`supportFragmentManager.commit`) |
| [M18](wiki/M18-Firebase.md) | Firebase Realtime DB | **Arquitectura MVVM**, `ViewModel`, `StateFlow` |
| [M19](wiki/M19-Animacio-Descriptor.md) | Animació XML | `AnimatorInflater`, `AnimatorSet`, ViewBinding |
| [M20](wiki/M20-Animacio-Sprite.md) | Sprite animation | `AnimationDrawable`, **DefaultLifecycleObserver** |
| [M21](wiki/M21-Servei-Suma.md) | Servei de càlcul | **Jetpack WorkManager**, `CoroutineWorker`, `WorkInfo` LiveData |

## Publicar la wiki a GitHub

```bash
./publish-wiki.sh https://github.com/USUARI/DAMPJ6-AndroidMultimedia.git
```

> Primer cal habilitar la Wiki al repositori: **Settings → Features → Wikis ✓**

---
**Autor:** sergi.grau@fje.edu · **Versió:** 7.0 · **Curs:** 2025-2026
