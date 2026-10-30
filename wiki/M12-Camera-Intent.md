# M12 – Camera via Intent

## Objective
Learn how to capture a photo using the system camera application through an `Intent` (`ACTION_IMAGE_CAPTURE`), handling the result with the modern `ActivityResultLauncher` (replacement for the deprecated `startActivityForResult`).

## Files Involved
| File | Description |
|------|-------------|
| `M12_CameraIntentActivity.kt` | Main activity |
| `res/layout/m13_camera_intent.xml` | Layout with Button and ImageView |

## Key Concepts

### ActivityResultLauncher (Modern API)
`startActivityForResult()` and `onActivityResult()` were **deprecated**. The alternative is `ActivityResultLauncher`:

```kotlin
// 1. Register the launcher BEFORE onCreate (as a class property)
private val capturaImatgeLauncher: ActivityResultLauncher<Intent> =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            // The photo is returned as a small Bitmap in the "data" extra
            val imageBitmap = result.data!!.extras?.getParcelable("data", Bitmap::class.java)
            imageView.setImageBitmap(imageBitmap)
        }
    }

// 2. Launch the camera
private fun takePicture() {
    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    if (takePictureIntent.resolveActivity(packageManager) != null) {
        capturaImatgeLauncher.launch(takePictureIntent)
    }
}
```

### Permission Handling with ActivityResultLauncher
```kotlin
private val requestCameraPermissionLauncher: ActivityResultLauncher<String> =
    registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) takePicture()
        else Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
    }

// Check and request permission
button.setOnClickListener {
    if (ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA)
        != PackageManager.PERMISSION_GRANTED) {
        requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        return@setOnClickListener
    }
    takePicture()
}
```

### Old vs New Methods Comparison
| Old method (deprecated) | New method |
|-------------------------|-----------|
| `startActivityForResult(intent, code)` | `launcher.launch(intent)` |
| `onActivityResult(code, result, data)` | Callback of `registerForActivityResult` |
| `requestPermissions(array, code)` | `permissionLauncher.launch(permission)` |
| `onRequestPermissionsResult(...)` | Callback of the permission launcher |

### Required Permissions
```xml
<!-- AndroidManifest.xml -->
<uses-permission android:name="android.permission.CAMERA" />

<!-- From API 33, WRITE_EXTERNAL_STORAGE is not needed for the camera -->
```

### Thumbnail Limitation
`ACTION_IMAGE_CAPTURE` returns a small **thumbnail** (not a full-resolution photo) in the `"data"` extra. To save the photo in high resolution, a destination `Uri` must be provided:

```kotlin
// For high resolution (outside the scope of this exercise)
val photoUri = FileProvider.getUriForFile(this, "edu.fje.dam2.provider", photoFile)
intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
```

## Difference Between M12 and M13
| | M12 (Intent) | M13 (Camera2 API) |
|---|---|---|
| **Complexity** | Simple | Complex |
| **Control** | Limited | Full |
| **Resolution** | Thumbnail | Maximum |
| **Camera app** | System | Own |
| **Recommended for** | General apps | Specialised apps |

---
[M11](M11-Animacio-Requadre) | [Home](Home) | [M13](M13-Camera-API)
