# M13 – Accés directe a la càmera (Camera2 API)

## Objectiu
Aprendre a accedir directament al maquinari de la càmera mitjançant la **Camera2 API**, que proporciona control total sobre la vista prèvia, la captura, l'orientació i els paràmetres de la càmera. És l'API recomanada per a aplicacions de càmera avançades.

## Fitxers involucrats
| Fitxer | Descripció |
|--------|------------|
| `M13_CameraActivity.kt` | Activitat principal de la càmera |
| `res/layout/m12_camera.xml` | Disseny amb TextureView i Botó |

## Conceptes clau

### Arquitectura de la Camera2 API

```
TextureView (vista prèvia)
    |
    v
CameraManager -> obre CameraDevice
    |
    v
CameraDevice -> crea CameraCaptureSession
    |
    +-- TEMPLATE_PREVIEW        -> vista prèvia contínua
    +-- TEMPLATE_STILL_CAPTURE  -> captura d'imatge
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
manager.openCamera(cameraId, stateCallback, null)
```

### CameraDevice.StateCallback
```kotlin
private val stateCallback = object : CameraDevice.StateCallback() {
    override fun onOpened(camera: CameraDevice) {
        cameraDevice = camera
        startPreview()  // Iniciar la vista prèvia quan la càmera estigui oberta
    }
    override fun onDisconnected(camera: CameraDevice) { camera.close() }
    override fun onError(camera: CameraDevice, error: Int) {
        camera.close()
        cameraDevice = null
    }
}
```

### TextureView i SurfaceTextureListener
`TextureView` mostra la vista prèvia de la càmera com una textura integrada al disseny:

```kotlin
textureView.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
    override fun onSurfaceTextureAvailable(surface: SurfaceTexture, w: Int, h: Int) {
        openCamera()  // La textura està disponible, obrir la càmera
    }
    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean = false
    // ...
}
```

### HandlerThread – Hils de la càmera
La Camera2 API requereix un fil dedicat per a les crides de retorn (no es pot utilitzar el fil principal):

```kotlin
protected fun startThread() {
    backgroundThread = HandlerThread("Camera background").also { it.start() }
    backgroundHandler = Handler(backgroundThread!!.looper)
}

protected fun stopThread() {
    backgroundThread?.quitSafely()
    backgroundThread?.join()
    backgroundThread = null
    backgroundHandler = null
}
```

### Vista prèvia contínua
```kotlin
protected fun startPreview() {
    val texture = textureView.surfaceTexture!!
    texture.setDefaultBufferSize(imageDimension!!.width, imageDimension!!.height)
    val surface = Surface(texture)

    captureRequestBuilder = cameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
    captureRequestBuilder.addTarget(surface)

    cameraDevice!!.createCaptureSession(listOf(surface), object : CameraCaptureSession.StateCallback() {
        override fun onConfigured(session: CameraCaptureSession) {
            cameraSession = session
            captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO)
            cameraSession?.setRepeatingRequest(captureRequestBuilder.build(), null, backgroundHandler)
        }
        override fun onConfigureFailed(session: CameraCaptureSession) {}
    }, null)
}
```

### Captura d'imatge

```kotlin
// Crear un ImageReader per rebre la imatge JPEG
val reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1)

// Configurar el listener per processar la imatge
reader.setOnImageAvailableListener({ r ->
    r.acquireLatestImage()?.use { image ->
        val buffer = image.planes[0].buffer
        val bytes = ByteArray(buffer.capacity())
        buffer.get(bytes)
        FileOutputStream(outputFile).use { it.write(bytes) }
    }
}, backgroundHandler)
```

### Gestió de l'orientació
```kotlin
private val ORIENTATIONS = SparseIntArray().apply {
    append(Surface.ROTATION_0,   90)
    append(Surface.ROTATION_90,  0)
    append(Surface.ROTATION_180, 270)
    append(Surface.ROTATION_270, 180)
}

val rotation = display?.rotation ?: Surface.ROTATION_0
captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation))
```

### Cicle de vida i càmera
```kotlin
override fun onResume() {
    super.onResume()
    startThread()
    if (textureView.isAvailable) openCamera()
    else textureView.surfaceTextureListener = textureListener
}

override fun onPause() {
    stopThread()  // Aturar sempre el fil a onPause
    super.onPause()
}
```

## Permisos requerits
```xml
<uses-permission android:name="android.permission.CAMERA" />
<uses-feature android:name="android.hardware.camera2" android:required="true" />
```

---
[M12](M12-Camera-Intent) | [Home](Home) | [M14](M14-Imatge-SD)
