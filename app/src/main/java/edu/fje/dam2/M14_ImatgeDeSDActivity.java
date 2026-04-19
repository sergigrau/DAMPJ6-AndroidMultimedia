package edu.fje.dam2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;

/**
 * Activity que permet l'accés directament al dispositiu de càmera
 * per seleccionar una imatge de la galeria d'imatges del dispositiu.
 * L'ús de ContentResolver amb Uri és la forma recomanada a API 33 en lloc de rutes de fitxer (deprecated).
 * L'ús de ActivityResultLauncher substitueix startActivityForResult (deprecated).
 * L'ús de MediaStore.Images.Media.EXTERNAL_CONTENT_URI és la forma recomanada per accedir a les imatges externes.
 * L'ús de InputStream per llegir la imatge és la forma recomanada en lloc de rutes de fitxer (deprecated).
 *
 * @version 6.0 (API 33) 19.04.2026
 * @author sergi.grau@fje.edu
 */
public class M14_ImatgeDeSDActivity extends AppCompatActivity {

    private ImageView imageView;

    // ActivityResultLauncher substitueix startActivityForResult (deprecated)
    private final ActivityResultLauncher<Intent> seleccioImatgeLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri seleccio = result.getData().getData();
                    try {
                        // Usar ContentResolver amb Uri en lloc de ruta de fitxer (deprecated)
                        InputStream inputStream = null;
                        if (seleccio != null) {
                            inputStream = getContentResolver().openInputStream(seleccio);
                        }
                        Bitmap imatge = BitmapFactory.decodeStream(inputStream);
                        imageView.setImageBitmap(imatge);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m14_imatges_sd);

        imageView = (ImageView) findViewById(R.id.imageView1);

        Intent i = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        seleccioImatgeLauncher.launch(i);
    }
}