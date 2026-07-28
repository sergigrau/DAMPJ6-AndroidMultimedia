# M12 – Càmera per Intent

## 🎯 Objectiu
Aprendre a capturar una foto usant l'aplicació de càmera del sistema a través d'un `Intent` (`ACTION_IMAGE_CAPTURE`), gestionant el resultat amb el modern `ActivityResultLauncher` (substitut del deprecated `startActivityForResult`).

## 📄 Fitxers implicats
| Fitxer | Descripció |
|--------|-----------|
| `M12_CameraIntentActivity.kt` | Activity principal |
| `res/layout/m13_camera_intent.xml` | Layout amb Button i ImageView |

## 🔑 Conceptes clau

### ActivityResultLauncher (API moderna)
`startActivityForResult()` i `onActivityResult()` van ser **deprecats**. L'alternativa és `ActivityResultLauncher`:

```kotlin
// 1. Registrar el launcher ABANS d'onCreate (com a propietat de classe)
private val capturaImatgeLauncher: ActivityResultLauncher<Intent> =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            // La foto retorna com a Bitmap petit al extra "data"
            val imageBitmap = result.data!!.extras?.getParcelable("data", Bitmap::class.java)
            imatge.setImageBitmap(imageBitmap)
        }
    }

// 2. Llançar la càmera
private fun fesFoto() {
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
        if (isGranted) fesFoto()
        else Toast.makeText(this, "Permís de càmera denegat", Toast.LENGTH_SHORT).show()
    }

// Comprovar i sol·licitar permís
boto.setOnClickListener {
    if (ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA)
        != PackageManager.PERMISSION_GRANTED) {
        requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        return@setOnClickListener
    }
    fesFoto()
}
```

### Comparativa mètodes antics vs nous
| Mètode antic (deprecated) | Mètode nou |
|--------------------------|-----------|
| `startActivityForResult(intent, code)` | `launcher.launch(intent)` |
| `onActivityResult(code, result, data)` | Callback del `registerForActivityResult` |
| `requestPermissions(array, code)` | `permissionLauncher.launch(permission)` |
| `onRequestPermissionsResult(...)` | Callback del launcher de permisos |

### Permisos necessaris
```xml
<!-- AndroidManifest.xml -->
<uses-permission android:name="android.permission.CAMERA" />

<!-- A partir d'API 33, no cal WRITE_EXTERNAL_STORAGE per a la càmera -->
```

### Limitació del thumbnail
`ACTION_IMAGE_CAPTURE` retorna un **thumbnail** petit (no la foto en resolució completa) al extra `"data"`. Per guardar la foto en alta resolució, cal proporcionar un `Uri` de destí:

```kotlin
// Per alta resolució (fora de l'abast d'aquest exercici)
val photoUri = FileProvider.getUriForFile(this, "edu.fje.dam2.provider", photoFile)
intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
```

## 🆚 Diferència entre M12 i M13
| | M12 (Intent) | M13 (Camera2 API) |
|---|---|---|
| **Complexitat** | ✅ Simple | ⚠️ Complex |
| **Control** | ❌ Limitat | ✅ Total |
| **Resolució** | Thumbnail | Màxima |
| **App de càmera** | Sistema | Pròpia |
| **Ús recomanat** | Apps generals | Apps especialitzades |

---
[← M11](M11-Animacio-Requadre) | [Inici](Home) | [M13 →](M13-Camera-API)

