# M13 – Accés directe a la càmera (Camera2 API)

## 🎯 Objectiu
Aprendre a accedir directament al hardware de la càmera usant la **Camera2 API**, que ofereix control total sobre la previsualització, captura, orientació i paràmetres de la càmera. És la API recomanada per aplicacions de càmera avançades.

## 📄 Fitxers implicats
| Fitxer | Descripció |
|--------|-----------|
| `M13_CameraActivity.kt` | Activity principal de la càmera |
| `res/layout/m12_camera.xml` | Layout amb TextureView i Button |

## 🔑 Conceptes clau

### Arquitectura Camera2 API

```
TextureView (previsualització)
    │
    ▼
CameraManager → obre CameraDevice
    │
    ▼
CameraDevice → crea CameraCaptureSession
    │
    ├─► TEMPLATE_PREVIEW → previsualització contínua
    └─► TEMPLATE_STILL_CAPTURE → captura d'imatge
```

### Flux d'obertura de la càmera

```kotlin
// 1. Obtenir el CameraManager
val manager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
val cameraId = manager.cameraIdList[0]  // Càmera posterior

// 2. Obtenir característiques (mides suportades)
val characteristics = manager.getCameraCharacteristics(cameraId)
val map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)!!
imageDimension = map.getOutputSizes(SurfaceTexture::class.java)[0]

// 3. Obrir la càmera
manager.openCamera(cameraId, estatCallback, null)
```

### CameraDevice.StateCallback
```kotlin
private val estatCallback = object : CameraDevice.StateCallback() {
    override fun onOpened(camera: CameraDevice) {
        dispositiuCamera = camera
        previsualitzacio()  // Iniciar preview quan la càmera és oberta
    }
    override fun onDisconnected(camera: CameraDevice) { camera.close() }
    override fun onError(camera: CameraDevice, error: Int) {
        camera.close()
        dispositiuCamera = null
    }
}
```

### TextureView i SurfaceTextureListener
`TextureView` permet mostrar la previsualització de la càmera com una textura integrada al layout:

```kotlin
vistaTextura.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
    override fun onSurfaceTextureAvailable(surface: SurfaceTexture, w: Int, h: Int) {
        obrirCamara()  // La textura està disponible, obrir la càmera
    }
    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean = false
    // ...
}
```

### HandlerThread – Fils de la càmera
La Camera2 API requereix un fil dedicat per als callbacks (no es pot usar el fil principal):

```kotlin
protected fun iniciFil() {
    fil = HandlerThread("Càmara en background").also { it.start() }
    filHandler = Handler(fil!!.looper)
}

protected fun aturarFil() {
    fil?.quitSafely()
    fil?.join()
    fil = null
    filHandler = null
}
```

### Previsualització contínua
```kotlin
protected fun previsualitzacio() {
    val textura = vistaTextura.surfaceTexture!!
    textura.setDefaultBufferSize(imageDimension!!.width, imageDimension!!.height)
    val surface = Surface(textura)

    captureRequestBuilder = dispositiuCamera!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
    captureRequestBuilder.addTarget(surface)

    dispositiuCamera!!.createCaptureSession(listOf(surface), object : CameraCaptureSession.StateCallback() {
        override fun onConfigured(session: CameraCaptureSession) {
            sessionsCamera = session
            captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO)
            sessionsCamera?.setRepeatingRequest(captureRequestBuilder.build(), null, filHandler)
        }
        override fun onConfigureFailed(session: CameraCaptureSession) {}
    }, null)
}
```

### Captura d'imatge

```kotlin
// Crear un ImageReader per rebre la imatge JPEG
val reader = ImageReader.newInstance(ample, altura, ImageFormat.JPEG, 1)

// Configurar el listener per processar la imatge
reader.setOnImageAvailableListener({ r ->
    r.acquireLatestImage()?.use { imatge ->
        val buffer = imatge.planes[0].buffer
        val bytes = ByteArray(buffer.capacity())
        buffer.get(bytes)
        FileOutputStream(fitxer).use { it.write(bytes) }
    }
}, filHandler)
```

### Gestió de l'orientació
```kotlin
private val ORIENTACIONS = SparseIntArray().apply {
    append(Surface.ROTATION_0,   90)
    append(Surface.ROTATION_90,  0)
    append(Surface.ROTATION_180, 270)
    append(Surface.ROTATION_270, 180)
}

val rotacio = display?.rotation ?: Surface.ROTATION_0
capturaBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTACIONS.get(rotacio))
```

### Cicle de vida i la càmera
```kotlin
override fun onResume() {
    super.onResume()
    iniciFil()
    if (vistaTextura.isAvailable) obrirCamara()
    else vistaTextura.surfaceTextureListener = texturaListener
}

override fun onPause() {
    aturarFil()  // Sempre aturar el fil en onPause
    super.onPause()
}
```

## ⚠️ Permisos necessaris
```xml
<uses-permission android:name="android.permission.CAMERA" />
<uses-feature android:name="android.hardware.camera2" android:required="true" />
```

---
[← M12](M12-Camera-Intent) | [Inici](Home) | [M14 →](M14-Imatge-SD)

