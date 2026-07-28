# M06 – Vista personalitzada amb Canvas

## 🎯 Objectiu
Crear una `View` personalitzada que dibuixa amb `Canvas` sobreescrivint el mètode `onDraw()`, i aprendre a definir **atributs XML personalitzats** per configurar-la des del layout.

## 📄 Fitxers implicats
| Fitxer | Descripció |
|--------|-----------|
| `M06_CanvasPersonalitzat.kt` | Activity que usa la vista |
| `M06_VistaPropia.kt` | Classe `View` personalitzada |
| `res/layout/m06_canvas_personalitzat_principal.xml` | Layout amb la vista personalitzada |
| `res/values/attrs.xml` | Definició d'atributs personalitzats |

## 🔑 Conceptes clau

### Per què una View personalitzada?
A diferència de M05 (dibuix estàtic sobre Bitmap), una `View` personalitzada:
- Es **redibuja automàticament** en canviar la mida o l'orientació.
- Pot gestionar **events tàctils**.
- S'integra completament al **sistema de layouts** d'Android.
- Es pot reutilitzar en **múltiples layouts** XML.

### Estructura d'una View personalitzada

```kotlin
class M06_VistaPropia(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var color: Int = 0

    // 1. Llegir atributs del layout XML
    init {
        val a = context.theme.obtainStyledAttributes(
            attrs, R.styleable.M06_VistaPropia, 0, 0
        )
        try {
            xPos = a.getFloat(R.styleable.M06_VistaPropia_x, 0f)
            cadena = a.getString(R.styleable.M06_VistaPropia_cadena)
        } finally {
            a.recycle()  // IMPORTANT: sempre reciclar el TypedArray
        }
    }

    // 2. Sobreescriure onDraw per dibuixar
    override fun onDraw(canvas: Canvas) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = color
        canvas.drawRect(xPos, yPos, 450f, 600f, paint)
        paint.color = Color.BLACK
        paint.textSize = 60f
        canvas.drawText(cadena ?: "", 40f, 250f, paint)
    }

    // 3. Setters que poden forçar un redibuxat
    fun setColor(c: Int) {
        color = c
        invalidate()  // Força onDraw()
    }
}
```

### Definició d'atributs personalitzats (`res/values/attrs.xml`)

```xml
<resources>
    <declare-styleable name="M06_VistaPropia">
        <attr name="x" format="float" />
        <attr name="cadena" format="string" />
    </declare-styleable>
</resources>
```

### Ús en el layout XML

```xml
<edu.fje.dam2.M06_VistaPropia
    android:id="@+id/vista"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:x="20"
    app:cadena="Android" />
```

### Ús des de l'Activity

```kotlin
val vistaPropia: M06_VistaPropia = findViewById(R.id.vista)
vistaPropia.setColor(Color.RED)
```

### Cicle de vida d'una View
```
Constructor → onMeasure() → onLayout() → onDraw()
                                              ↑
                                        invalidate()
                                    (força el redibuxat)
```

### `invalidate()` vs `requestLayout()`
| Mètode | Quan usar |
|--------|-----------|
| `invalidate()` | Quan canvia el contingut visual (color, text...) |
| `requestLayout()` | Quan canvia la mida o posició de la vista |

## ⚙️ Bones pràctiques en `onDraw()`
- **No crear objectes** a `onDraw()`: s'executa moltes vegades per segon. Crear el `Paint` al constructor.
- **No fer operacions costoses** (I/O, xarxa) a `onDraw()`.
- Sempre cridar `a.recycle()` després d'usar un `TypedArray`.

---
[← M05](M05-Canvas-Basic) | [Inici](Home) | [M07 →](M07-Canvas-Path)

