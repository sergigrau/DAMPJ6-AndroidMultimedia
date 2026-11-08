# DAM2.PJ6 – Android Multimèdia i Serveis (Migrat a Jetpack)

Benvinguts a la wiki del projecte **DAMPJ6-AndroidMultimedia**, una col·lecció d'exercicis pràctics de desenvolupament Android en **Kotlin** per al mòdul de Projecte de DAM2, totalment migrats a les biblioteques i patrons d'arquitectura de **Jetpack**.

---

## Taula de Continguts

| Mòdul | Títol | Component Jetpack / Concepte |
|-------|-------|------------------------------|
| [M01](M01-Audio-MediaPlayer) | Reproducció d'Àudio | **Jetpack Media3 ExoPlayer**, `AudioAttributes`, ViewBinding |
| [M02](M02-Audio-Service) | Àudio en Segon Pla | **Jetpack Media3 MediaSessionService**, `MediaSession` |
| [M03](M03-ExecutorService) | Tasques en Segon Pla | **Kotlin Coroutines**, `lifecycleScope`, `Dispatchers.IO` |
| [M04](M04-Notificacions-SnackBar) | Notificacions | `NotificationCompat`, `NotificationManagerCompat`, Material3 |
| [M05](M05-Canvas-Basic) | Canvas Bàsic | `createBitmap` (Core KTX), ViewBinding |
| [M06](M06-Canvas-Personalitzat) | Vista Personalitzada | `Custom View`, `withStyledAttributes` |
| [M07](M07-Canvas-Path) | Dibuix a Mà Alçada | `Path`, `MaskFilter`, ViewBinding |
| [M08](M08-Alarmes) | Alarmes i Tasques | **Jetpack WorkManager**, `CoroutineWorker` |
| [M09](M09-Propietats-Dinamiques) | Vistes Dinàmiques | **Jetpack ConstraintLayout**, `ConstraintSet` dinàmic |
| [M10](M10-Animacio-Basica) | Animació Bàsica | `ObjectAnimator`, ViewBinding |
| [M11](M11-Animacio-Requadre) | Animació Seqüencial | `AnimatorSet`, ViewBinding |
| [M12](M12-Camera-Intent) | Càmera mitjançant Intent | **Activity Result API** (`TakePicturePreview`) |
| [M13](M13-Camera-API) | Càmera Avançada | **Jetpack CameraX** (`ProcessCameraProvider`, `PreviewView`, `ImageCapture`) |
| [M14](M14-Imatge-SD) | Imatge de la Galeria | **Activity Result API** (`PickVisualMedia` Photo Picker) |
| [M15](M15-OpenGL-ES-20) | OpenGL ES 2.0 | `GLSurfaceView`, **DefaultLifecycleObserver** |
| [M16](M16-Lectura-XML) | Lectura XML | **Coroutines**, **RecyclerView**, `ListAdapter`, `DiffUtil` |
| [M17](M17-Fragments) | Fragments | **Jetpack Fragment KTX** (`supportFragmentManager.commit`) |
| [M18](M18-Firebase) | Firebase Realtime DB | **Arquitectura MVVM**, `ViewModel`, `StateFlow` |
| [M19](M19-Animacio-Descriptor) | Animador XML | `AnimatorInflater`, `AnimatorSet`, ViewBinding |
| [M20](M20-Animacio-Sprite) | Animació Sprite | `AnimationDrawable`, **DefaultLifecycleObserver** |
| [M21](M21-Servei-Suma) | Servei de Càlcul | **Jetpack WorkManager**, `CoroutineWorker`, `WorkInfo` LiveData |

---

## Tecnologies del Projecte

- **Llenguatge:** Kotlin 2.2.10
- **SDK mínim:** Android 13 (API 33)
- **SDK objectiu:** Android 16 (API 36)
- **Eina de construcció:** Android Gradle Plugin 9.5.0
- **Jetpack Libraries:**
  - **Media3 ExoPlayer & Session:** 1.5.1
  - **CameraX:** 1.4.1
  - **WorkManager:** 2.10.0
  - **Lifecycle & ViewModel:** 2.8.7
  - **RecyclerView:** 1.4.0
  - **ViewBinding:** Habilitat
- **Base de dades:** Firebase Realtime Database (BoM 33.9.0)
- **IU:** AppCompat, Material Design 3, ConstraintLayout

---

## Estructura del Projecte

```
app/src/main/
├── java/edu/fje/dam2/
│   ├── M01_AudioActivity.kt
│   ├── M02_AudioIntentService.kt
│   ├── M02_AudioServiceActivity.kt
│   ├── M03_ExecutorServiceActivity.kt
│   ├── M04_Notificacions_SnackBar.kt
│   ├── M05_CanvasBasic.kt
│   ├── M06_CanvasPersonalitzat.kt
│   ├── M06_VistaPropia.kt
│   ├── M07_CanvasPath.kt
│   ├── M07_PathDit.kt
│   ├── M07_VistaPaint.kt
│   ├── M08_AlarmaActivity.kt
│   ├── M08_AlarmaReceiver.kt
│   ├── M08_AlarmaWorker.kt
│   ├── M09_Propietats_dinamiques.kt
│   ├── M10_AnimacioBasica.kt
│   ├── M11_AnimacioRequadre.kt
│   ├── M12_CameraIntentActivity.kt
│   ├── M13_CameraActivity.kt
│   ├── M14_ImatgeDeSDActivity.kt
│   ├── M15_OpenGLES20Activity.kt
│   ├── M15_OpenGLES20GLSurfaceView.kt
│   ├── M15_OpenGLES20Renderer.kt
│   ├── M15_OpenGLES20Triangle.kt
│   ├── M15_OpenGLES20Quadrat.kt
│   ├── M16_LecturaXMLActivity.kt
│   ├── M16_LecturaXMLUtility.kt
│   ├── M17_FragmentsActivity.kt
│   ├── M17_LlistaFragmentActivity.kt
│   ├── M17_DetallFragmentActivity.kt
│   ├── M18_Artista.kt
│   ├── M18_Llista.kt
│   ├── M18_ArtistesViewModel.kt
│   ├── M18_Firebase_Artistes.kt
│   ├── M19_AnimacioDescriptor.kt
│   ├── M20_AnimacioSprite.kt
│   ├── M21_SumaWorker.kt
│   └── M21_SumaServeiActivity.kt
├── assets/
│   └── m16_curs.xml
└── res/
    ├── layout/
    ├── drawable/
    ├── animator/
    ├── menu/
    └── raw/
```

---

## Com Executar el Projecte

1. Cloneu el repositori.
2. Obriu-lo amb **Android Studio Ladybug** o superior.
3. Connecteu un dispositiu físic o creeu un AVD amb **API 33+**.
4. Col·loqueu el fitxer `google-services.json` a la carpeta `app/` per als exercicis de Firebase (M18).
5. Executeu `Run > Run 'app'`.

---

> **Autor:** sergi.grau@fje.edu | **Versió:** 7.0 | **Curs:** 2025-2026
