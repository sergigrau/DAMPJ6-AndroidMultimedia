# M06 – Vista Personalitzada amb Canvas

## Objectiu
Crear una `View` personalitzada que dibuixi amb `Canvas` sobreescrivint el mètode `onDraw()`, i aprendre a definir **atributs XML personalitzats** per configurar-la des del layout.

## Fitxers Involucrats
| Fitxer | Descripció |
|--------|------------|
| `M06_CanvasPersonalitzat.kt` | Activity que utilitza la vista personalitzada |
| `M06_VistaPropia.kt` | Classe `View` personalitzada |
| `res/layout/m06_canvas_personalitzat_principal.xml` | Layout amb la vista personalitzada |
| `res/values/attrs.xml` | Definicions d'atributs personalitzats |

## Conceptes Clau

### Per què una Vista Personalitzada?
A diferència de M05 (dibuix estàtic sobre un Bitmap), una vista personalitzada:
- **Es redibuixa automàticament** quan canvia la mida o l'orientació.
- Pot gestionar **esdeveniments tàctils**.
- S'integra plenament en el **sistema de layouts** d'Android.
- Es pot reutilitzar en **múltiples layouts XML**.

### Estructura d'una Vista Personalitzada

```kotlin
class M06_VistaPropia(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var color: Int = 0
    // Paint preassignat fora de onDraw() per evitar creacions durant el dibuix
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    // 1. Llegeix els atributs del layout XML
    init {
        val a = context.theme.obtainStyledAttributes(
            attrs, R.styleable.M06_VistaPropia, 0, 0
        )
        try {
            xPos = a.getFloat(R.styleable.M06_VistaPropia_x, 0f)
            cadena = a.getString(R.styleable.M06_VistaPropia_cadena)
        } finally {
            a.recycle()  // IMPORTANT: reciclar sempre el TypedArray
        }
    }

    // 2. Sobreescriu onDraw per dibuixar
    override fun onDraw(canvas: Canvas) {
        paint.color = color
        canvas.drawRect(xPos, yPos, 450f, 600f, paint)
        paint.color = Color.BLACK
        paint.textSize = 60f
        canvas.drawText(cadena ?: "", 40f, 250f, paint)
    }

    // 3. Setters que poden disparar un redibuix
    fun setColor(c: Int) {
        color = c
        invalidate()  // Força onDraw()
    }
}
```

### Definició d'Atributs Personalitzats (`res/values/attrs.xml`)

```xml
<resources>
    <declare-styleable name="M06_VistaPropia">
        <attr name="x" format="float" />
        <attr name="cadena" format="string" />
    </declare-styleable>
</resources>
```

### Ús de la Vista al Layout XML

```xml
<edu.fje.dam2.M06_VistaPropia
    android:id="@+id/vista"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:x="20"
    app:cadena="Android" />
```

### Ús de la Vista des d'una Activity

```kotlin
val vistaPropia: M06_VistaPropia = findViewById(R.id.vista)
vistaPropia.setColor(Color.RED)
```

### Cicle de Vida de la View
```
Constructor -> onMeasure() -> onLayout() -> onDraw()
                                               ^
                                         invalidate()
                                     (força un redibuix)
```

### `invalidate()` vs `requestLayout()`
| Mètode | Quan utilitzar-lo |
|--------|-------------------|
| `invalidate()` | Quan el contingut visual canvia (color, text...) |
| `requestLayout()` | Quan la mida o la posició de la vista canvien |

## Bones Pràctiques a `onDraw()`
- **No creïs objectes** dins de `onDraw()`: es crida moltes vegades per segon. Crea el `Paint` al constructor.
- **No realitzis operacions pesades** (I/O, xarxa) dins de `onDraw()`.
- Crida sempre `a.recycle()` després d'utilitzar un `TypedArray`.

---
[M05](M05-Canvas-Basic) | [Home](Home) | [M07](M07-Canvas-Path)
