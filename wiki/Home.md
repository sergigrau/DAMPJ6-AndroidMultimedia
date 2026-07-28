# DAM2.PJ6 – Android Multimedia i Serveis 📱

Benvingut/da a la wiki del projecte **DAMPJ6-AndroidMultimedia**, una col·lecció d'exercicis pràctics de desenvolupament d'aplicacions Android en **Kotlin** per al mòdul de Projecte del cicle formatiu DAM2.

---

## 📚 Índex d'exercicis

| Mòdul | Títol | Conceptes clau |
|-------|-------|----------------|
| [M01](M01-Audio-MediaPlayer) | Reproducció d'àudio | `MediaPlayer`, `AudioFocus` |
| [M02](M02-Audio-Service) | Àudio en segon pla | `Service`, `Intent` |
| [M03](M03-ExecutorService) | Tasques en segon pla | `ExecutorService`, `ObjectAnimator` |
| [M04](M04-Notificacions-SnackBar) | Notificacions SnackBar | `Snackbar`, `Toast` |
| [M05](M05-Canvas-Basic) | Canvas bàsic | `Canvas`, `Bitmap`, `Paint` |
| [M06](M06-Canvas-Personalitzat) | Vista personalitzada | `View`, `AttributeSet`, `onDraw` |
| [M07](M07-Canvas-Path) | Dibuix lliure | `Path`, `MaskFilter`, `EmbossMaskFilter` |
| [M08](M08-Alarmes) | Alarmes | `AlarmManager`, `BroadcastReceiver` |
| [M09](M09-Propietats-Dinamiques) | Vistes dinàmiques | `LinearLayout`, `LayoutParams` |
| [M10](M10-Animacio-Basica) | Animació bàsica | `ObjectAnimator` |
| [M11](M11-Animacio-Requadre) | Animació seqüencial | `AnimatorSet` |
| [M12](M12-Camera-Intent) | Càmera per intent | `ActivityResultLauncher`, `MediaStore` |
| [M13](M13-Camera-API) | Càmera Camera2 API | `CameraDevice`, `CameraCaptureSession` |
| [M14](M14-Imatge-SD) | Imatge de la galeria | `ContentResolver`, `BitmapFactory` |
| [M15](M15-OpenGL-ES-20) | OpenGL ES 2.0 | `GLSurfaceView`, `GLES20`, shaders |
| [M16](M16-Lectura-XML) | Anàlisi XML | `XmlPullParser`, `AssetManager` |
| [M17](M17-Fragments) | Fragments | `Fragment`, `FragmentManager` |
| [M18](M18-Firebase) | Firebase Realtime DB | `FirebaseDatabase`, `ValueEventListener` |
| [M19](M19-Animacio-Descriptor) | Animació per descriptor | `AnimatorInflater`, `AnimatorSet` |
| [M20](M20-Animacio-Sprite) | Animació Sprite | `AnimationDrawable` |
| [M21](M21-Servei-Suma) | Servei de càlcul | `Service`, `BroadcastReceiver` |

---

## 🛠️ Tecnologies del projecte

- **Llenguatge:** Kotlin 2.1.0
- **SDK mínim:** Android 13 (API 33)
- **SDK objectiu:** Android 16 (API 36)
- **Build tool:** Android Gradle Plugin 9.1.1
- **Base de dades:** Firebase Realtime Database 22.0.1
- **UI:** AppCompat, Material Design 3

---

## 🏗️ Estructura del projecte

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

## 🚀 Com executar el projecte

1. Clona el repositori
2. Obre'l amb **Android Studio Meerkat** o superior
3. Connecta un dispositiu físic o crea un AVD amb **API 33+**
4. Configura el fitxer `google-services.json` per als exercicis de Firebase (M18)
5. Executa `Run > Run 'app'`

---

> **Autor:** sergi.grau@fje.edu | **Versió:** 6.0 | **Curs:** 2025-2026

