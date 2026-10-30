# M07 – Freehand Drawing with Path and Paint

## Objective
Implement a freehand drawing application where the user can paint with their finger on the screen, applying visual effects such as emboss (`EmbossMaskFilter`) and blur (`BlurMaskFilter`). Introduces the `Path` class for drawing quadratic Bezier curves.

## Files Involved
| File | Description |
|------|-------------|
| `M07_CanvasPath.kt` | Main activity with effects menu |
| `M07_VistaPaint.kt` | Custom View that manages drawing |
| `M07_PathDit.kt` | Data class representing an individual stroke |
| `res/menu/m07_menu.xml` | Menu: Emboss / Blur / Clear |

## Key Concepts

### Path – Quadratic Bezier Curves
`Path` allows defining complex trajectories. Instead of straight lines, quadratic curves are used for smooth strokes:

```kotlin
// Start a new stroke
rutaActual = Path()
rutaActual.moveTo(x, y)          // Starting point

// While moving the finger, add a curve
rutaActual.quadTo(
    xPrev, yPrev,                 // Control point
    (x + xPrev) / 2,              // End point (midpoint)
    (y + yPrev) / 2
)

// When the finger is lifted
rutaActual.lineTo(xPrev, yPrev)
```

### Touch Event Handling

```kotlin
override fun onTouchEvent(event: MotionEvent): Boolean {
    val x = event.x
    val y = event.y
    when (event.action) {
        MotionEvent.ACTION_DOWN -> { touchStart(x, y); invalidate() }
        MotionEvent.ACTION_MOVE -> { touchMove(x, y); invalidate() }
        MotionEvent.ACTION_UP   -> { touchUp(); invalidate(); performClick() }
    }
    return true  // Indicates that we handled the event
}
```

### Data class M07_PathDit
Each stroke is an immutable object containing all the information needed to draw it:

```kotlin
data class M07_PathDit(
    val color: Int,
    val emboss: Boolean,
    val blur: Boolean,
    val strokeWidth: Int,
    val path: Path
)
```

### MaskFilter – Visual Effects
Filters are applied to the `Paint` to modify the appearance of the stroke:

**Emboss (EmbossMaskFilter):**
```kotlin
val embossFilter: MaskFilter = EmbossMaskFilter(
    floatArrayOf(1f, 1f, 1f),  // Light direction (x, y, z)
    0.4f,                       // Ambient light
    6f,                         // Specular
    3.5f                        // Emboss smoothness
)
paint.maskFilter = embossFilter
```

**Blur (BlurMaskFilter):**
```kotlin
val blurFilter: MaskFilter = BlurMaskFilter(
    5f,                         // Blur radius
    BlurMaskFilter.Blur.NORMAL  // Type: NORMAL, SOLID, INNER, OUTER
)
paint.maskFilter = blurFilter
```

### Double Buffer Pattern (Off-screen Bitmap)
To avoid flickering, drawing is done onto an auxiliary Bitmap and then copied to the main Canvas:

```kotlin
override fun onDraw(canvas: Canvas) {
    canvas.save()
    canvasBitmap?.drawColor(backgroundColour)       // Clear background

    for (fp in strokes) {
        paint.color       = fp.color
        paint.strokeWidth = fp.strokeWidth.toFloat()
        paint.maskFilter  = if (fp.emboss) embossFilter
                            else if (fp.blur) blurFilter
                            else null
        canvasBitmap?.drawPath(fp.path, paint)      // Draw to buffer
    }
    bitmap?.let { canvas.drawBitmap(it, 0f, 0f, bitmapPaint) } // Copy to Canvas
    canvas.restore()
}
```

### Touch Tolerance
To avoid overly jagged strokes from slight finger tremors:

```kotlin
private const val TOUCH_TOLERANCE = 4f

private fun touchMove(x: Float, y: Float) {
    val dx = Math.abs(x - xPrev)
    val dy = Math.abs(y - yPrev)
    if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
        // Add point to the path
    }
}
```

## Drawing Flow Summary

```
User places finger (ACTION_DOWN)
    -> touchStart() -> new Path -> new M07_PathDit added to the list

User moves finger (ACTION_MOVE)
    -> touchMove() -> quadTo() on the current Path -> invalidate()

User lifts finger (ACTION_UP)
    -> touchUp() -> lineTo() to close the stroke -> invalidate()

onDraw() -> iterates through all strokes -> draws all to Bitmap -> copies to Canvas
```

---
[M06](M06-Canvas-Personalitzat) | [Home](Home) | [M08](M08-Alarmes)
