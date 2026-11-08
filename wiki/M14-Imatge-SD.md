# M14 – Carregar Imatges de la Galeria amb PickVisualMedia Photo Picker

## Objectiu
Aprendre a seleccionar imatges de la galeria de manera segura i moderna utilitzant el contracte de Jetpack Activity Result **`ActivityResultContracts.PickVisualMedia`** (Android Photo Picker).

## Fitxers Involucrats
| Fitxer | Descripció |
|--------|------------|
| `M14_ImatgeDeSDActivity.kt` | Activity amb PickVisualMedia i ViewBinding |

## Conceptes Clau

### PickVisualMedia Photo Picker
Permet a l'usuari triar imatges de la galeria **sense necessitat de sol·licitar permisos d'emmagatzematge** (`READ_MEDIA_IMAGES` / `READ_EXTERNAL_STORAGE`):

```kotlin
private val pickVisualMediaLauncher =
    registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
        if (uri != null) {
            contentResolver.openInputStream(uri)?.use { inputStream ->
                val bitmap = BitmapFactory.decodeStream(inputStream)
                binding.imageView1.setImageBitmap(bitmap)
            }
        }
    }

// Llançament del Photo Picker
pickVisualMediaLauncher.launch(
    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
)
```

---
[M13](M13-Camera-API) | [Home](Home) | [M15](M15-OpenGL-ES-20)
