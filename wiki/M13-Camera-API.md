# M13 – Direct Camera Access (Camera2 API)

## Objective
Learn how to access the camera hardware directly using the **Camera2 API**, which provides full control over the preview, capture, orientation and camera parameters. It is the recommended API for advanced camera applications.

## Files Involved
| File | Description |
|------|-------------|
| `M13_CameraActivity.kt` | Main camera activity |
| `res/layout/m12_camera.xml` | Layout with TextureView and Button |

## Key Concepts

### Camera2 API Architecture

```
TextureView (preview)
    |
    v
CameraManager -> opens CameraDevice
    |
    v
CameraDevice -> creates CameraCaptureSession
    |
    +-- TEMPLATE_PREVIEW        -> continuous preview
    +-- TEMPLATE_STILL_CAPTURE  -> image capture
```

### Camera Opening Flow

```kotlin
// 1. Get the CameraManager
val manager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
val cameraId = manager.cameraIdList[0]  // Rear camera

// 2. Get characteristics (supported sizes)
val characteristics = manager.getCameraCharacteristics(cameraId)
val map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)!!
imageDimension = map.getOutputSizes(SurfaceTexture::class.java)[0]

// 3. Open the camera
manager.openCamera(cameraId, stateCallback, null)
```

### CameraDevice.StateCallback
```kotlin
private val stateCallback = object : CameraDevice.StateCallback() {
    override fun onOpened(camera: CameraDevice) {
        cameraDevice = camera
        startPreview()  // Start preview when camera is open
    }
    override fun onDisconnected(camera: CameraDevice) { camera.close() }
    override fun onError(camera: CameraDevice, error: Int) {
        camera.close()
        cameraDevice = null
    }
}
```

### TextureView and SurfaceTextureListener
`TextureView` shows the camera preview as a texture integrated into the layout:

```kotlin
textureView.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
    override fun onSurfaceTextureAvailable(surface: SurfaceTexture, w: Int, h: Int) {
        openCamera()  // Texture is available, open the camera
    }
    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean = false
    // ...
}
```

### HandlerThread – Camera Threads
The Camera2 API requires a dedicated thread for callbacks (the main thread cannot be used):

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

### Continuous Preview
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

### Image Capture

```kotlin
// Create an ImageReader to receive the JPEG image
val reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1)

// Set the listener to process the image
reader.setOnImageAvailableListener({ r ->
    r.acquireLatestImage()?.use { image ->
        val buffer = image.planes[0].buffer
        val bytes = ByteArray(buffer.capacity())
        buffer.get(bytes)
        FileOutputStream(outputFile).use { it.write(bytes) }
    }
}, backgroundHandler)
```

### Orientation Handling
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

### Lifecycle and Camera
```kotlin
override fun onResume() {
    super.onResume()
    startThread()
    if (textureView.isAvailable) openCamera()
    else textureView.surfaceTextureListener = textureListener
}

override fun onPause() {
    stopThread()  // Always stop the thread in onPause
    super.onPause()
}
```

## Required Permissions
```xml
<uses-permission android:name="android.permission.CAMERA" />
<uses-feature android:name="android.hardware.camera2" android:required="true" />
```

---
[M12](M12-Camera-Intent) | [Home](Home) | [M14](M14-Imatge-SD)
