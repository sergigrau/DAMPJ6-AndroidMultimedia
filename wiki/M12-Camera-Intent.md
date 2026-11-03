# M12 – Càmera mitjançant Intent

## Objectiu
Aprendre a capturar una foto utilitzant l'aplicació de càmera del sistema mitjançant un `Intent` (`ACTION_IMAGE_CAPTURE`), gestionant el resultat amb el modern `ActivityResultLauncher` (substitut del mètode obsolet `startActivityForResult`).

## Fitxers involucrats
| Fitxer | Descripció |
|--------|------------|
| `M12_CameraIntentActivity.kt` | Activitat principal |
| `res/layout/m13_camera_intent.xml` | Disseny amb Botó i ImageView |

## Conceptes clau

### ActivityResultLauncher (API moderna)
`startActivityForResult()` i `onActivityResult()` estan **obsolets** (deprecated). L'alternativa és `ActivityResultLauncher`:

```kotlin
// 1. Registrar el launcher ABANS d'onCreate (com a propietat de classe)
private val capturaImatgeLauncher: ActivityResultLauncher<Intent> =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            // La foto es retorna com un petit Bitmap a l'extra "data"
            val imageBitmap = result.data!!.extras?.getParcelable("data", Bitmap::class.java)
            imageView.setImageBitmap(imageBitmap)
        }
    }

// 2. Iniciar la càmera
private fun takePicture() {
    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    if (takePictureIntent.resolveActivity(packageManager) != null) {
        capturaImatgeLauncher.launch(takePictureIntent)
    }
}
```

### Gestió de permisos amb ActivityResultLauncher
```kotlin
private val requestCameraPermissionLauncher: ActivityResultLauncher<String> =
    registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) takePicture()
        else Toast.makeText(this, "Permís de càmera denegat", Toast.LENGTH_SHORT).show()
    }

// Comprovar i sol·licitar el permís
button.setOnClickListener {
    if (ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA)
        != PackageManager.PERMISSION_GRANTED) {
        requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        return@setOnClickListener
    }
    takePicture()
}
```

### Comparativa entre mètodes antics i nous
| Mètode antic (obsolet) | Mètode nou |
|------------------------|------------|
| `startActivityForResult(intent, code)` | `launcher.launch(intent)` |
| `onActivityResult(code, result, data)` | Callback de `registerForActivityResult` |
| `requestPermissions(array, code)` | `permissionLauncher.launch(permission)` |
| `onRequestPermissionsResult(...)` | Callback del launcher de permisos |

### Permisos requerits
```xml
<!-- AndroidManifest.xml -->
<uses-permission android:name="android.permission.CAMERA" />

<!-- A partir de l'API 33, WRITE_EXTERNAL_STORAGE no és necessari per a la càmera -->
```

### Limitació de la miniatura
`ACTION_IMAGE_CAPTURE` retorna una petita **miniatura** (no una foto a resolució completa) a l'extra `"data"`. Per desar la foto en alta resolució, cal proporcionar un `Uri` de destinació:

```kotlin
// Per a alta resolució (fora de l'abast d'aquest exercici)
val photoUri = FileProvider.getUriForFile(this, "edu.fje.dam2.provider", photoFile)
intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
```

## Diferència entre M12 i M13
| | M12 (Intent) | M13 (Camera2 API) |
|---|---|---|
| **Complexitat** | Sencera | Complexa |
| **Control** | Limitat | Total |
| **Resolució** | Miniatura | Màxima |
| **App de càmera** | Del sistema | Pròpia |
| **Recomanat per a** | Apps generals | Apps especialitzades |

---
[M11](M11-Animacio-Requadre) | [Home](Home) | [M13](M13-Camera-API)
