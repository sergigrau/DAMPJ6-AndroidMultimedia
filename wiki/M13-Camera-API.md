# M13 – Càmera Avançada amb Jetpack CameraX

## Objectiu
Aprendre a utilitzar **Jetpack CameraX** (`ProcessCameraProvider`, `PreviewView`, `ImageCapture`), la llibreria oficial de Jetpack que simplifica l'accés al maquinari de la càmera d'una forma 100% conscient del cicle de vida.

## Fitxers Involucrats
| Fitxer | Descripció |
|--------|------------|
| `M13_CameraActivity.kt` | Activitat principal basada en CameraX |
| `res/layout/m12_camera.xml` | Layout amb `androidx.camera.view.PreviewView` |

## Conceptes Clau

### Arquitectura de Jetpack CameraX
CameraX substitueix més de 200 línies de boilerplate de l'antiga Camera2 API per una configuració senzilla vinculada al `LifecycleOwner`:

```kotlin
private fun iniciarCameraX() {
    val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
    cameraProviderFuture.addListener({
        val cameraProvider = cameraProviderFuture.get()

        val preview = Preview.Builder().build().also {
            it.surfaceProvider = binding.previewView.surfaceProvider
        }

        imageCapture = ImageCapture.Builder().build()
        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
    }, ContextCompat.getMainExecutor(this))
}
```

### Captura d'Imatge amb ImageCapture
```kotlin
val photoFile = File(getExternalFilesDir(null), "camerax_pic.jpg")
val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

imageCapture.takePicture(
    outputOptions,
    ContextCompat.getMainExecutor(this),
    object : ImageCapture.OnImageSavedCallback {
        override fun onImageSaved(output: ImageCapture.OutputFileResults) {
            Toast.makeText(this@M13_CameraActivity, "Foto desada amb CameraX!", Toast.LENGTH_SHORT).show()
        }
        override fun onError(exc: ImageCaptureException) {}
    }
)
```

---
[M12](M12-Camera-Intent) | [Home](Home) | [M14](M14-Imatge-SD)
