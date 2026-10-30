# M06 – Custom View with Canvas

## Objective
Create a custom `View` that draws with `Canvas` by overriding the `onDraw()` method, and learn how to define **custom XML attributes** to configure it from the layout.

## Files Involved
| File | Description |
|------|-------------|
| `M06_CanvasPersonalitzat.kt` | Activity that uses the custom view |
| `M06_VistaPropia.kt` | Custom `View` class |
| `res/layout/m06_canvas_personalitzat_principal.xml` | Layout with the custom view |
| `res/values/attrs.xml` | Custom attribute definitions |

## Key Concepts

### Why a Custom View?
Unlike M05 (static drawing onto a Bitmap), a custom `View`:
- **Redraws automatically** when size or orientation changes.
- Can handle **touch events**.
- Integrates fully into Android's **layout system**.
- Can be reused in **multiple XML layouts**.

### Structure of a Custom View

```kotlin
class M06_VistaPropia(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var color: Int = 0
    // Paint preallocated outside onDraw() to avoid allocations during drawing
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    // 1. Read attributes from the XML layout
    init {
        val a = context.theme.obtainStyledAttributes(
            attrs, R.styleable.M06_VistaPropia, 0, 0
        )
        try {
            xPos = a.getFloat(R.styleable.M06_VistaPropia_x, 0f)
            cadena = a.getString(R.styleable.M06_VistaPropia_cadena)
        } finally {
            a.recycle()  // IMPORTANT: always recycle the TypedArray
        }
    }

    // 2. Override onDraw to draw
    override fun onDraw(canvas: Canvas) {
        paint.color = color
        canvas.drawRect(xPos, yPos, 450f, 600f, paint)
        paint.color = Color.BLACK
        paint.textSize = 60f
        canvas.drawText(cadena ?: "", 40f, 250f, paint)
    }

    // 3. Setters that can trigger a redraw
    fun setColor(c: Int) {
        color = c
        invalidate()  // Forces onDraw()
    }
}
```

### Defining Custom Attributes (`res/values/attrs.xml`)

```xml
<resources>
    <declare-styleable name="M06_VistaPropia">
        <attr name="x" format="float" />
        <attr name="cadena" format="string" />
    </declare-styleable>
</resources>
```

### Using the View in XML Layout

```xml
<edu.fje.dam2.M06_VistaPropia
    android:id="@+id/vista"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:x="20"
    app:cadena="Android" />
```

### Using the View from an Activity

```kotlin
val vistaPropia: M06_VistaPropia = findViewById(R.id.vista)
vistaPropia.setColor(Color.RED)
```

### View Lifecycle
```
Constructor -> onMeasure() -> onLayout() -> onDraw()
                                               ^
                                         invalidate()
                                     (forces a redraw)
```

### `invalidate()` vs `requestLayout()`
| Method | When to use |
|--------|-------------|
| `invalidate()` | When the visual content changes (colour, text...) |
| `requestLayout()` | When the size or position of the view changes |

## Best Practices in `onDraw()`
- **Do not create objects** inside `onDraw()`: it is called many times per second. Create the `Paint` in the constructor.
- **Do not perform expensive operations** (I/O, network) inside `onDraw()`.
- Always call `a.recycle()` after using a `TypedArray`.

---
[M05](M05-Canvas-Basic) | [Home](Home) | [M07](M07-Canvas-Path)
