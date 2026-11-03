# M07 – Dibuix a Mà Alçada amb Path i Paint

## Objectiu
Implementar una aplicació de dibuix a mà alçada on l'usuari pugui pintar amb el dit per la pantalla, aplicant efectes visuals com relleu (`EmbossMaskFilter`) i difuminat (`BlurMaskFilter`). Introdueix la classe `Path` per dibuixar corbes de Bézier quadràtiques.

## Fitxers Involucrats
| Fitxer | Descripció |
|--------|------------|
| `M07_CanvasPath.kt` | Activity principal amb menú d'efectes |
| `M07_VistaPaint.kt` | Vista personalitzada que gestiona el dibuix |
| `M07_PathDit.kt` | Data class que representa un traç individual |
| `res/menu/m07_menu.xml` | Menú: Relleu / Difuminat / Netejar |

## Conceptes Clau

### Path – Corbes de Bézier quadràtiques
`Path` permet definir trajectòries complexes. En lloc de línies rectes, s'utilitzen corbes quadràtiques per aconseguir traços suaus:

```kotlin
// Inicia un nou traç
rutaActual = Path()
rutaActual.moveTo(x, y)          // Punt inicial

// Mentre es mou el dit, afegeix una corba
rutaActual.quadTo(
    xPrev, yPrev,                 // Punt de control
    (x + xPrev) / 2,              // Punt final (punt mitjà)
    (y + yPrev) / 2
)

// Quan s'aixeca el dit
rutaActual.lineTo(xPrev, yPrev)
```

### Gestió d'Esdeveniments Tàctils

```kotlin
override fun onTouchEvent(event: MotionEvent): Boolean {
    val x = event.x
    val y = event.y
    when (event.action) {
        MotionEvent.ACTION_DOWN -> { touchStart(x, y); invalidate() }
        MotionEvent.ACTION_MOVE -> { touchMove(x, y); invalidate() }
        MotionEvent.ACTION_UP   -> { touchUp(); invalidate(); performClick() }
    }
    return true  // Indica que hem gestionat l'esdeveniment
}
```

### Data class M07_PathDit
Cada traç és un objecte immutable que conté tota la informació necessària per dibuixar-lo:

```kotlin
data class M07_PathDit(
    val color: Int,
    val emboss: Boolean,
    val blur: Boolean,
    val strokeWidth: Int,
    val path: Path
)
```

### MaskFilter – Efectes Visuals
Els filtres s'apliquen al `Paint` per modificar l'aspecte del traç:

**Relleu (EmbossMaskFilter):**
```kotlin
val embossFilter: MaskFilter = EmbossMaskFilter(
    floatArrayOf(1f, 1f, 1f),  // Direcció de la llum (x, y, z)
    0.4f,                       // Llum ambient
    6f,                         // Especular
    3.5f                        // Suavitat del relleu
)
paint.maskFilter = embossFilter
```

**Difuminat (BlurMaskFilter):**
```kotlin
val blurFilter: MaskFilter = BlurMaskFilter(
    5f,                         // Radi del difuminat
    BlurMaskFilter.Blur.NORMAL  // Tipus: NORMAL, SOLID, INNER, OUTER
)
paint.maskFilter = blurFilter
```

### Patró de Doble Buffer (Bitmap fora de pantalla)
Per evitar parpellejos (flickering), el dibuix es fa sobre un Bitmap auxiliar i després es copia al Canvas principal:

```kotlin
override fun onDraw(canvas: Canvas) {
    canvas.save()
    canvasBitmap?.drawColor(backgroundColour)       // Neteja el fons

    for (fp in strokes) {
        paint.color       = fp.color
        paint.strokeWidth = fp.strokeWidth.toFloat()
        paint.maskFilter  = if (fp.emboss) embossFilter
                            else if (fp.blur) blurFilter
                            else null
        canvasBitmap?.drawPath(fp.path, paint)      // Dibuixa al buffer
    }
    bitmap?.let { canvas.drawBitmap(it, 0f, 0f, bitmapPaint) } // Copia al Canvas
    canvas.restore()
}
```

### Tolerància Tàctil
Per evitar traços massa serrats per lleus tremolors del dit:

```kotlin
private const val TOUCH_TOLERANCE = 4f

private fun touchMove(x: Float, y: Float) {
    val dx = Math.abs(x - xPrev)
    val dy = Math.abs(y - yPrev)
    if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
        // Afegeix punt al camí
    }
}
```

## Resum del Flux de Dibuix

```
L'usuari col·loca el dit (ACTION_DOWN)
    -> touchStart() -> nou Path -> nou M07_PathDit afegit a la llista

L'usuari mou el dit (ACTION_MOVE)
    -> touchMove() -> quadTo() al Path actual -> invalidate()

L'usuari aixeca el dit (ACTION_UP)
    -> touchUp() -> lineTo() per tancar el traç -> invalidate()

onDraw() -> recorre tots els traços -> dibuixa tot al Bitmap -> copia al Canvas
```

---
[M06](M06-Canvas-Personalitzat) | [Home](Home) | [M08](M08-Alarmes)
