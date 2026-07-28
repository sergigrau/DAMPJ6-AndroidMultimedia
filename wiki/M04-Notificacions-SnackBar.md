# M04 – Notificacions i SnackBar

## 🎯 Objectiu
Aprendre a mostrar missatges informatius a l'usuari amb `Snackbar` (notificació no intrusiva a la part inferior de la pantalla) i `Toast`, i entendre quan és adequat usar cadascun.

## 📄 Fitxers implicats
| Fitxer | Descripció |
|--------|-----------|
| `M04_Notificacions_SnackBar.kt` | Activity principal |
| `res/layout/m04_notificacions_snack_bar.xml` | Layout amb CoordinatorLayout i FAB |

## 🔑 Conceptes clau

### Snackbar
El `Snackbar` és un component de Material Design que apareix a la part inferior de la pantalla durant un temps limitat. A diferència del `Toast`, pot incloure una **acció interactiva**:

```kotlin
Snackbar.make(view, "Reproduïnt...", Snackbar.LENGTH_LONG)
    .setAction("Veure") {
        Toast.makeText(applicationContext, "Acció veure més", Toast.LENGTH_LONG).show()
    }
    .show()
```

**Durades disponibles:**
| Constant | Durada |
|----------|--------|
| `LENGTH_SHORT` | ~2 segons |
| `LENGTH_LONG` | ~3.5 segons |
| `LENGTH_INDEFINITE` | Fins que l'usuari o el codi la tanquen |

### Toast
Missatge emergent simple, sense interacció possible:

```kotlin
Toast.makeText(context, "Text del missatge", Toast.LENGTH_SHORT).show()
```

### Comparativa Snackbar vs Toast
| Característica | Snackbar | Toast |
|----------------|----------|-------|
| Posició | Part inferior (sobre la UI) | Centre/baix |
| Acció | ✅ Sí | ❌ No |
| Es pot descartar | ✅ Lliscant | ❌ No |
| Context necessari | View | Context |
| Material Design | ✅ | ❌ |
| Recomanat per | Missatges d'acció | Missatges simples |

### CoordinatorLayout
Per a que el `Snackbar` es mostri correctament i que el FAB pugui pujar quan apareix, cal que el layout arrel sigui un `CoordinatorLayout`:

```xml
<androidx.coordinatorlayout.widget.CoordinatorLayout
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <com.google.android.material.floatingactionbutton.FloatingActionButton
        android:id="@+id/fab"
        ... />

</androidx.coordinatorlayout.widget.CoordinatorLayout>
```

### FloatingActionButton (FAB)
Botó circular flotant que fa una acció principal:

```kotlin
val fab: FloatingActionButton = findViewById(R.id.fab)
fab.setOnClickListener { view ->
    Snackbar.make(view, "Missatge", Snackbar.LENGTH_LONG).show()
}
```

## 📌 Quan usar cada element
- **Snackbar:** Confirmació d'una acció (fitxer desat, email enviat, element eliminat amb opció de desfer).
- **Toast:** Missatges informatius ràpids sense resposta de l'usuari.
- **Dialog/AlertDialog:** Quan cal confirmació explícita de l'usuari.
- **Notification:** Per notificacions que han de persistir fora de l'app.

---
[← M03](M03-ExecutorService) | [Inici](Home) | [M05 →](M05-Canvas-Basic)

