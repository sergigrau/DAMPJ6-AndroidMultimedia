# M04 – Notificacions i SnackBar

## Objectiu
Aprendre a mostrar missatges informatius a l'usuari amb `Snackbar` (una notificació no intrusiva a la part inferior de la pantalla) i `Toast`, i entendre quan és apropiat cadascun.

## Fitxers Involucrats
| Fitxer | Descripció |
|--------|------------|
| `M04_Notificacions_SnackBar.kt` | Activity principal |
| `res/layout/m04_notificacions_snack_bar.xml` | Layout amb CoordinatorLayout i FAB |

## Conceptes Clau

### Snackbar
`Snackbar` és un component de Material Design que apareix a la part inferior de la pantalla durant un temps limitat. A diferència del `Toast`, pot incloure una **acció interactiva**:

```kotlin
Snackbar.make(view, "S'està reproduint...", Snackbar.LENGTH_LONG)
    .setAction("Veure") {
        Toast.makeText(applicationContext, "Acció de veure més", Toast.LENGTH_LONG).show()
    }
    .show()
```

**Durades disponibles:**
| Constant | Durada |
|----------|--------|
| `LENGTH_SHORT` | ~2 segons |
| `LENGTH_LONG` | ~3.5 segons |
| `LENGTH_INDEFINITE` | Fins que l'usuari o el codi el tanquin |

### Toast
Un missatge emergent simple sense cap interacció:

```kotlin
Toast.makeText(context, "Text del missatge", Toast.LENGTH_SHORT).show()
```

### Comparació Snackbar vs Toast
| Característica | Snackbar | Toast |
|----------------|----------|-------|
| Posició | Inferior (sobre la IU) | Centre/inferior |
| Acció | Sí | No |
| Tancable | Sí (lliscant) | No |
| Context necessari | View | Context |
| Material Design | Sí | No |
| Recomanat per | Feedback d'accions | Missatges simples |

### CoordinatorLayout
Perquè el `Snackbar` es mostri correctament i el FAB llisqui cap amunt quan aparegui, el layout arrel ha de ser un `CoordinatorLayout`:

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
Un botó flotant circular que dispara una acció principal:

```kotlin
val fab: FloatingActionButton = findViewById(R.id.fab)
fab.setOnClickListener { view ->
    Snackbar.make(view, "Missatge", Snackbar.LENGTH_LONG).show()
}
```

## Quan Utilitzar cada Element
- **Snackbar:** Confirmació d'accions (fitxer desat, correu enviat, element esborrat amb opció de desfer).
- **Toast:** Missatges informatius ràpids que no requereixen resposta de l'usuari.
- **Dialog/AlertDialog:** Quan es requereix una confirmació explícita de l'usuari.
- **Notificació:** Per a avisos que han de persistir fora de l'aplicació.

---
[M03](M03-ExecutorService) | [Home](Home) | [M05](M05-Canvas-Basic)
