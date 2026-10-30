# DAM2.PJ6 – Android Multimedia and Services

Welcome to the wiki of the **DAMPJ6-AndroidMultimedia** project, a collection of practical Android development exercises in **Kotlin** for the DAM2 Project module.

---

## Table of Contents

| Module | Title | Key Concepts |
|--------|-------|--------------|
| [M01](M01-Audio-MediaPlayer) | Audio Playback | `MediaPlayer`, `AudioFocus` |
| [M02](M02-Audio-Service) | Background Audio | `Service`, `Intent` |
| [M03](M03-ExecutorService) | Background Tasks | `ExecutorService`, `ObjectAnimator` |
| [M04](M04-Notificacions-SnackBar) | SnackBar Notifications | `Snackbar`, `Toast` |
| [M05](M05-Canvas-Basic) | Basic Canvas | `Canvas`, `Bitmap`, `Paint` |
| [M06](M06-Canvas-Personalitzat) | Custom View | `View`, `AttributeSet`, `onDraw` |
| [M07](M07-Canvas-Path) | Freehand Drawing | `Path`, `MaskFilter`, `EmbossMaskFilter` |
| [M08](M08-Alarmes) | Alarms | `AlarmManager`, `BroadcastReceiver` |
| [M09](M09-Propietats-Dinamiques) | Dynamic Views | `LinearLayout`, `LayoutParams` |
| [M10](M10-Animacio-Basica) | Basic Animation | `ObjectAnimator` |
| [M11](M11-Animacio-Requadre) | Sequential Animation | `AnimatorSet` |
| [M12](M12-Camera-Intent) | Camera via Intent | `ActivityResultLauncher`, `MediaStore` |
| [M13](M13-Camera-API) | Camera2 API | `CameraDevice`, `CameraCaptureSession` |
| [M14](M14-Imatge-SD) | Image from Gallery | `ContentResolver`, `BitmapFactory` |
| [M15](M15-OpenGL-ES-20) | OpenGL ES 2.0 | `GLSurfaceView`, `GLES20`, shaders |
| [M16](M16-Lectura-XML) | XML Parsing | `XmlPullParser`, `AssetManager` |
| [M17](M17-Fragments) | Fragments | `Fragment`, `FragmentManager` |
| [M18](M18-Firebase) | Firebase Realtime DB | `FirebaseDatabase`, `ValueEventListener` |
| [M19](M19-Animacio-Descriptor) | XML Animator | `AnimatorInflater`, `AnimatorSet` |
| [M20](M20-Animacio-Sprite) | Sprite Animation | `AnimationDrawable` |
| [M21](M21-Servei-Suma) | Calculation Service | `Service`, `BroadcastReceiver` |

---

## Project Technologies

- **Language:** Kotlin 2.1.0
- **Minimum SDK:** Android 13 (API 33)
- **Target SDK:** Android 16 (API 36)
- **Build tool:** Android Gradle Plugin 9.1.1
- **Database:** Firebase Realtime Database 22.0.1
- **UI:** AppCompat, Material Design 3

---

## Project Structure

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
│   ├── M18_Firebase_Artistes.kt
│   ├── M19_AnimacioDescriptor.kt
│   ├── M20_AnimacioSprite.kt
│   ├── M21_SumaService.kt
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

## How to Run the Project

1. Clone the repository.
2. Open it with **Android Studio Meerkat** or later.
3. Connect a physical device or create an AVD with **API 33+**.
4. Place the `google-services.json` file in the `app/` folder for Firebase exercises (M18).
5. Run `Run > Run 'app'`.

---

> **Author:** sergi.grau@fje.edu | **Version:** 6.0 | **Course:** 2025-2026
