package edu.fje.dam2

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.hardware.camera2.params.StreamConfigurationMap
import android.media.Image
import android.media.ImageReader
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.util.Size
import android.util.SparseIntArray
import android.view.Surface
import android.view.TextureView
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.ByteBuffer

/**
 * Activity que permet l'accés directament al dispositiu de càmera.
 *
 * @author sergi.grau@fje.edu
 * @version 6.0 10.04.2026
 */
class M13_CameraActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "M11"
        private const val PETICIO_PERMIS_CAMARA = 200
        private val ORIENTACIONS = SparseIntArray().apply {
            append(Surface.ROTATION_0, 90)
            append(Surface.ROTATION_90, 0)
            append(Surface.ROTATION_180, 270)
            append(Surface.ROTATION_270, 180)
        }
    }

    private lateinit var vistaTextura: TextureView
    protected var dispositiuCamera: CameraDevice? = null
    protected var sessionsCamera: CameraCaptureSession? = null
    protected lateinit var captureRequestBuilder: CaptureRequest.Builder
    private var imageDimension: Size? = null
    private var filHandler: Handler? = null
    private var fil: HandlerThread? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.m12_camera)
        vistaTextura = findViewById(R.id.texture)
        vistaTextura.surfaceTextureListener = texturaListener
        val prendreFoto: Button = findViewById(R.id.btn_takepicture)
        prendreFoto.setOnClickListener { prendreFoto() }
    }

    private val texturaListener = object : TextureView.SurfaceTextureListener {
        override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
            obrirCamara()
        }
        override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {}
        override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean = false
        override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {}
    }

    private val estatCallback = object : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice) {
            Log.e(TAG, "càmara oberta")
            dispositiuCamera = camera
            previsualitzacio()
        }
        override fun onDisconnected(camera: CameraDevice) { dispositiuCamera?.close() }
        override fun onError(camera: CameraDevice, error: Int) {
            dispositiuCamera?.close()
            dispositiuCamera = null
        }
    }

    val capturaCallbackListener = object : CameraCaptureSession.CaptureCallback() {
        override fun onCaptureCompleted(session: CameraCaptureSession, request: CaptureRequest, result: TotalCaptureResult) {
            super.onCaptureCompleted(session, request, result)
            Toast.makeText(this@M13_CameraActivity, "Desada", Toast.LENGTH_SHORT).show()
            previsualitzacio()
        }
    }

    protected fun iniciFil() {
        fil = HandlerThread("Càmara en background").also { it.start() }
        filHandler = Handler(fil!!.looper)
    }

    protected fun aturarFil() {
        fil?.quitSafely()
        try {
            fil?.join()
            fil = null
            filHandler = null
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    protected fun prendreFoto() {
        val camera = dispositiuCamera ?: run { Log.e(TAG, "dispositiuCamera es null"); return }
        val manager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            val caracteristiques = manager.getCameraCharacteristics(camera.id)
            val mides = caracteristiques.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                ?.getOutputSizes(ImageFormat.JPEG)
            val ample = mides?.getOrNull(0)?.width ?: 640
            val altura = mides?.getOrNull(0)?.height ?: 480

            val reader = ImageReader.newInstance(ample, altura, ImageFormat.JPEG, 1)
            val superficieSortida = listOf(reader.surface, Surface(vistaTextura.surfaceTexture))
            val capturaBuilder = camera.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
            capturaBuilder.addTarget(reader.surface)
            capturaBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO)

            val rotacio = display?.rotation ?: Surface.ROTATION_0
            capturaBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTACIONS.get(rotacio))

            val fitxer = File(getExternalFilesDir(null), "pic.jpg")
            reader.setOnImageAvailableListener({ r ->
                r.acquireLatestImage()?.use { imatge ->
                    val buffer: ByteBuffer = imatge.planes[0].buffer
                    val bytes = ByteArray(buffer.capacity())
                    buffer.get(bytes)
                    try { FileOutputStream(fitxer).use { it.write(bytes) } } catch (e: IOException) { e.printStackTrace() }
                }
            }, filHandler)

            @Suppress("DEPRECATION")
            camera.createCaptureSession(superficieSortida, object : CameraCaptureSession.StateCallback() {
                override fun onConfigured(session: CameraCaptureSession) {
                    try {
                        session.capture(capturaBuilder.build(), object : CameraCaptureSession.CaptureCallback() {
                            override fun onCaptureCompleted(s: CameraCaptureSession, req: CaptureRequest, result: TotalCaptureResult) {
                                super.onCaptureCompleted(s, req, result)
                                Toast.makeText(this@M13_CameraActivity, "Desat: $fitxer", Toast.LENGTH_SHORT).show()
                                previsualitzacio()
                            }
                        }, filHandler)
                    } catch (e: CameraAccessException) { e.printStackTrace() }
                }
                override fun onConfigureFailed(session: CameraCaptureSession) {}
            }, filHandler)
        } catch (e: CameraAccessException) { e.printStackTrace() }
    }

    protected fun previsualitzacio() {
        try {
            val textura = vistaTextura.surfaceTexture ?: return
            textura.setDefaultBufferSize(imageDimension!!.width, imageDimension!!.height)
            val surface = Surface(textura)
            captureRequestBuilder = dispositiuCamera!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            captureRequestBuilder.addTarget(surface)
            @Suppress("DEPRECATION")
            dispositiuCamera!!.createCaptureSession(listOf(surface), object : CameraCaptureSession.StateCallback() {
                override fun onConfigured(cameraCaptureSession: CameraCaptureSession) {
                    if (dispositiuCamera == null) return
                    sessionsCamera = cameraCaptureSession
                    updatePreview()
                }
                override fun onConfigureFailed(cameraCaptureSession: CameraCaptureSession) {
                    Toast.makeText(this@M13_CameraActivity, "canvi configuració", Toast.LENGTH_SHORT).show()
                }
            }, null)
        } catch (e: CameraAccessException) { e.printStackTrace() }
    }

    private fun obrirCamara() {
        val manager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        Log.e(TAG, "camara oberta")
        try {
            val cameraId = manager.cameraIdList[0]
            val characteristics = manager.getCameraCharacteristics(cameraId)
            val map: StreamConfigurationMap = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)!!
            imageDimension = map.getOutputSizes(SurfaceTexture::class.java)[0]
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), PETICIO_PERMIS_CAMARA)
                return
            }
            manager.openCamera(cameraId, estatCallback, null)
        } catch (e: CameraAccessException) { e.printStackTrace() }
        Log.e(TAG, "obrirCamara")
    }

    protected fun updatePreview() {
        if (dispositiuCamera == null) Log.e(TAG, "error vista previa")
        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO)
        try {
            sessionsCamera?.setRepeatingRequest(captureRequestBuilder.build(), null, filHandler)
        } catch (e: CameraAccessException) { e.printStackTrace() }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PETICIO_PERMIS_CAMARA && grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_DENIED
        ) {
            Toast.makeText(this, "calen permisos", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        Log.e(TAG, "onResume")
        iniciFil()
        if (vistaTextura.isAvailable) obrirCamara()
        else vistaTextura.surfaceTextureListener = texturaListener
    }

    override fun onPause() {
        Log.e(TAG, "onPause")
        aturarFil()
        super.onPause()
    }
}

