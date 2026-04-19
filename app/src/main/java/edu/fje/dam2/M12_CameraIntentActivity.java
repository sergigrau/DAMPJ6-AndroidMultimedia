package edu.fje.dam2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

/**
 * Activity que permet l'accés a l'aplicació estàndard de la càmera de fotos
 * i mostra la imatge capturada en un ImageView. A API 33 no cal WRITE_EXTERNAL_STORAGE per a la càmera, només el permís de CAMERA.
 * L'ús de ActivityResultLauncher substitueix el mètode startActivityForResult, que està deprecated a les versions més recents d'Android.
 * L'Activity comprova si té el permís de càmera abans de llançar la intenció de captura d'imatge. Si no té el permís, el sol·licita a l'usuari.
 * Quan l'usuari captura una foto i torna a l'aplicació, el resultat es maneja a través de l'ActivityResultLauncher, que actualitza l'ImageView amb la imatge capturada.
 *
 * @author sergi.grau@fje.edu
 * @version 6.0 (API 33)
 */
public class M12_CameraIntentActivity extends AppCompatActivity {

    private ImageView imatge;

    private final ActivityResultLauncher<Intent> capturaImatgeLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bundle extras = result.getData().getExtras();
                    Bitmap imageBitmap = null;
                    if (extras != null) {
                        imageBitmap = extras.getParcelable("data", Bitmap.class);
                    }
                    imatge.setImageBitmap(imageBitmap);
                }
            });

    private final ActivityResultLauncher<String> requestCameraPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permís concedit, fem la foto
                    fesFoto();
                } else {
                    Toast.makeText(this, "Permís de càmera denegat", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m13_camera_intent);

        Button boto = findViewById(R.id.botoFoto);
        imatge = findViewById(R.id.imageView1);

        boto.setOnClickListener(view -> {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA);
                return;
            }
            fesFoto();
        });
    }

    @SuppressLint("QueryPermissionsNeeded")
    private void fesFoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            capturaImatgeLauncher.launch(takePictureIntent);
        }
    }
}