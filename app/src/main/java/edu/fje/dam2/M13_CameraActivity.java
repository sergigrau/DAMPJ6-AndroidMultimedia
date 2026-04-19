package edu.fje.dam2;

import android.os.Bundle;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;

import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Activity que permet l'accés directament al dispositiu de càmera
 *
 * @author sergi.grau@fje.edu
 * @version 5.0 27.01.2020
 */
public class M13_CameraActivity extends AppCompatActivity {
    private static final String TAG = "M11";
    private Button prendreFoto;
    private TextureView vistaTextura;
    private static final SparseIntArray ORIENTACIONS = new SparseIntArray();

    static {
        ORIENTACIONS.append(Surface.ROTATION_0, 90);
        ORIENTACIONS.append(Surface.ROTATION_90, 0);
        ORIENTACIONS.append(Surface.ROTATION_180, 270);
        ORIENTACIONS.append(Surface.ROTATION_270, 180);
    }

    private String cameraId;
    protected CameraDevice dispositiuCamera;
    protected CameraCaptureSession sessionsCamera;
    protected CaptureRequest captureRequest;
    protected CaptureRequest.Builder captureRequestBuilder;
    private Size imageDimension;
    private ImageReader imageReader;
    private File file;
    private static final int PETICIO_PERMIS_CAMARA = 200;
    private boolean mFlashSupported;
    private Handler filHandler;
    private HandlerThread fil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m12_camera);
        vistaTextura =  findViewById(R.id.texture);
        assert vistaTextura != null;
        vistaTextura.setSurfaceTextureListener(texturaListener);
        prendreFoto = findViewById(R.id.btn_takepicture);
        assert prendreFoto != null;
        prendreFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prendreFoto();
            }
        });
    }

    TextureView.SurfaceTextureListener texturaListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            obrirCamara();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        }
    };
    private final CameraDevice.StateCallback estatCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {
            Log.e(TAG, "càmara oberta");
            dispositiuCamera = camera;
            previsualitzacio();
        }

        @Override
        public void onDisconnected(CameraDevice camera) {
            dispositiuCamera.close();
        }

        @Override
        public void onError(CameraDevice camera, int error) {
            dispositiuCamera.close();
            dispositiuCamera = null;
        }
    };
    final CameraCaptureSession.CaptureCallback capturaCallbackListener = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
            super.onCaptureCompleted(session, request, result);
            Toast.makeText(M13_CameraActivity.this, "Desada:" + file, Toast.LENGTH_SHORT).show();
            previsualitzacio();
        }
    };

    protected void iniciFil() {
        fil = new HandlerThread("Càmara en background");
        fil.start();
        filHandler = new Handler(fil.getLooper());
    }

    protected void aturarFil() {
        fil.quitSafely();
        try {
            fil.join();
            fil = null;
            filHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected void prendreFoto() {
        if (null == dispositiuCamera) {
            Log.e(TAG, "dispositiuCamera es null");
            return;
        }
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            CameraCharacteristics caracteristiques = manager.getCameraCharacteristics(dispositiuCamera.getId());
            Size[] mides = null;
            if (caracteristiques != null) {
                mides = caracteristiques.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(ImageFormat.JPEG);
            }
            int ample = 640;
            int altura = 480;
            if (mides != null && 0 < mides.length) {
                ample = mides[0].getWidth();
                altura = mides[0].getHeight();
            }
            ImageReader reader = ImageReader.newInstance(ample, altura, ImageFormat.JPEG, 1);
            List<Surface> superficieSortida = new ArrayList<Surface>(2);
            superficieSortida.add(reader.getSurface());
            superficieSortida.add(new Surface(vistaTextura.getSurfaceTexture()));
            final CaptureRequest.Builder capturaBuilder = dispositiuCamera.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            capturaBuilder.addTarget(reader.getSurface());
            capturaBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);

            // getDisplay() substitueix getDefaultDisplay() (deprecated a API 30)
            int rotacio = getDisplay() != null ? getDisplay().getRotation() : Surface.ROTATION_0;
            capturaBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTACIONS.get(rotacio));
            // getExternalFilesDir() substitueix getExternalStorageDirectory() (deprecated a API 29)
            final File fitxer = new File(getExternalFilesDir(null), "pic.jpg");
            ImageReader.OnImageAvailableListener readerListener = new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader reader) {
                    Image imatge = null;
                    try {
                        imatge = reader.acquireLatestImage();
                        ByteBuffer buffer = imatge.getPlanes()[0].getBuffer();
                        byte[] bytes = new byte[buffer.capacity()];
                        buffer.get(bytes);
                        desar(bytes);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (imatge != null) {
                            imatge.close();
                        }
                    }
                }

                private void desar(byte[] bytes) throws IOException {
                    OutputStream output = null;
                    try {
                        output = new FileOutputStream(fitxer);
                        output.write(bytes);
                    } finally {
                        if (null != output) {
                            output.close();
                        }
                    }
                }
            };
            reader.setOnImageAvailableListener(readerListener, filHandler);
            final CameraCaptureSession.CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
                    super.onCaptureCompleted(session, request, result);
                    Toast.makeText(M13_CameraActivity.this, "Desat:" + fitxer, Toast.LENGTH_SHORT).show();
                    previsualitzacio();
                }
            };
            dispositiuCamera.createCaptureSession(superficieSortida, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession session) {
                    try {
                        session.capture(capturaBuilder.build(), captureListener, filHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession session) {
                }
            }, filHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    protected void previsualitzacio() {
        try {
            SurfaceTexture textura = vistaTextura.getSurfaceTexture();
            assert textura != null;
            textura.setDefaultBufferSize(imageDimension.getWidth(), imageDimension.getHeight());
            Surface surface = new Surface(textura);
            captureRequestBuilder = dispositiuCamera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(surface);
            dispositiuCamera.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    if (null == dispositiuCamera) {
                        return;
                    }
                    sessionsCamera = cameraCaptureSession;
                    updatePreview();
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    Toast.makeText(M13_CameraActivity.this, "canvi configuració", Toast.LENGTH_SHORT).show();
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void obrirCamara() {
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        Log.e(TAG, "camara oberta");
        try {
            cameraId = manager.getCameraIdList()[0];
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            assert map != null;
            imageDimension = map.getOutputSizes(SurfaceTexture.class)[0];
            // dóna permisos a la camera u deixa a l'usuari administrar-los
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(M13_CameraActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PETICIO_PERMIS_CAMARA);
                return;
            }
            manager.openCamera(cameraId, estatCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "obrirCamara");
    }

    protected void updatePreview() {
        if (null == dispositiuCamera) {
            Log.e(TAG, "error vista previa");
        }
        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        try {
            sessionsCamera.setRepeatingRequest(captureRequestBuilder.build(), null, filHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void tancarCamara() {
        if (null != dispositiuCamera) {
            dispositiuCamera.close();
            dispositiuCamera = null;
        }
        if (null != imageReader) {
            imageReader.close();
            imageReader = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PETICIO_PERMIS_CAMARA) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(M13_CameraActivity.this, "calen permisos", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
        iniciFil();
        if (vistaTextura.isAvailable()) {
            obrirCamara();
        } else {
            vistaTextura.setSurfaceTextureListener(texturaListener);
        }
    }

    @Override
    protected void onPause() {
        Log.e(TAG, "onPause");
        //tancarCamara();
        aturarFil();
        super.onPause();
    }
}