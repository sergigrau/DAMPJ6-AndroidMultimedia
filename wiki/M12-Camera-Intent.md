# M12 – Càmera per Intent amb Activity Result TakePicturePreview

## Objectiu
Aprendre a capturar fotografies ràpides des de la càmera del sistema utilitzant el contracte de Jetpack Activity Result **`ActivityResultContracts.TakePicturePreview`**.

## Fitxers Involucrats
| Fitxer | Descripció |
|--------|------------|
| `M12_CameraIntentActivity.kt` | Activity que utilitza `TakePicturePreview` |

## Conceptes Clau

### TakePicturePreview Contract
Substitueix la creació manual de l'Intent i `startActivityForResult`:

```kotlin
private val takePictureLauncher =
    registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap: Bitmap? ->
        if (bitmap != null) {
            binding.imageView1.setImageBitmap(bitmap)
        }
    }

// Execució
takePictureLauncher.launch(null)
```

---
[M11](M11-Animacio-Requadre) | [Home](Home) | [M13](M13-Camera-API)
