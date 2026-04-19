package edu.fje.dam2;

import android.os.Bundle;

import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

/**
 * Activitat que mostra el funcionament de Canvas,
 * fa ús de Paint i Path
 * Disposa d'un menú per a aplicar diferents efectes al dibuix
 * Per a detectar els canvis afegeix   android:configChanges="orientation|screenSize" a
 * l'activity en el manifest
 * @author sergi.grau@fje.edu
 * @version 6.0 27.01.2026
 */
public class M07_CanvasPath extends AppCompatActivity {

    private M07_VistaPaint vistaPaint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m07_canvas_principal);

        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        vistaPaint = findViewById(R.id.paintView);
        // getCurrentWindowMetrics() substitueix getDefaultDisplay().getMetrics() (deprecated API 30)
        Rect bounds = getWindowManager().getCurrentWindowMetrics().getBounds();
        DisplayMetrics metrics = new DisplayMetrics();
        metrics.widthPixels = bounds.width();
        metrics.heightPixels = bounds.height();
        vistaPaint.init(metrics);
        if (vistaPaint != null) {
            vistaPaint.init(metrics);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.m07_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.emboss) {
            if (vistaPaint != null) vistaPaint.posaRelleu();
            return true;
        } else if (id == R.id.blur) {
            if (vistaPaint != null) vistaPaint.posaDifumina();
            return true;
        } else if (id == R.id.clear) {
            if (vistaPaint != null) vistaPaint.neteja();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
