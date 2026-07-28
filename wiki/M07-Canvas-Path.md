# M07 – Dibuix lliure amb Path i Paint

## 🎯 Objectiu
Implementar una aplicació de dibuix lliure on l'usuari pot pintar amb el dit sobre la pantalla, aplicant efectes visuals com relleu (`EmbossMaskFilter`) i difuminat (`BlurMaskFilter`). Introdueix la classe `Path` per traçar corbes Bézier.

## 📄 Fitxers implicats
| Fitxer | Descripció |
|--------|-----------|
| `M07_CanvasPath.kt` | Activity principal amb menú d'efectes |
| `M07_VistaPaint.kt` | View personalitzada que gestiona el dibuix |
| `M07_PathDit.kt` | Data class que representa un traç individual |
| `res/menu/m07_menu.xml` | Menú: Relleu / Difuminat / Neteja |

## 🔑 Conceptes clau

### Path – Corbes de Bézier quadràtiques
`Path` permet definir trajectòries complexes. En lloc de línies rectes, s'usen corbes quadràtiques per a traços suaus:

```kotlin
// Iniciar un nou traç
rutaActual = Path()
rutaActual.moveTo(x, y)          // Punt inicial

// En moure el dit, afegir corba
rutaActual.quadTo(
    xAnterior, yAnterior,         // Punt de control
    (x + xAnterior) / 2,          // Punt final (punt mig)
    (y + yAnterior) / 2
)

// En aixecar el dit
rutaActual.lineTo(xAnterior, yAnterior)
```

### Gestió d'events tàctils

```kotlin
override fun onTouchEvent(event: MotionEvent): Boolean {
    val x = event.x
    val y = event.y
    when (event.action) {
        MotionEvent.ACTION_DOWN -> { touchStart(x, y); invalidate() }
        MotionEvent.ACTION_MOVE -> { touchMove(x, y); invalidate() }
        MotionEvent.ACTION_UP   -> { touchUp();       invalidate() }
    }
    return true  // Indica que hem gestionat l'event
}
```

### Data class M07_PathDit
Cada traç és un objecte immutable amb tota la informació necessària per dibuixar-lo:

```kotlin
data class M07_PathDit(
    val color: Int,
    val relleu: Boolean,
    val difumina: Boolean,
    val ampladaTrac: Int,
    val ruta: Path
)
```

### MaskFilter – Efectes visuals
Els filtres s'apliquen al `Paint` per modificar l'aparença del traç:

**Relleu (EmbossMaskFilter):**
```kotlin
val filtroRelleu: MaskFilter = EmbossMaskFilter(
    floatArrayOf(1f, 1f, 1f),  // Direcció de la llum (x, y, z)
    0.4f,                       // Llum ambient
    6f,                         // Especularitat
    3.5f                        // Suavitat del relleu
)
paint.maskFilter = filtroRelleu
```

**Difuminat (BlurMaskFilter):**
```kotlin
val filtroDifumina: MaskFilter = BlurMaskFilter(
    5f,                         // Radi del difuminat
    BlurMaskFilter.Blur.NORMAL  // Tipus: NORMAL, SOLID, INNER, OUTER
)
paint.maskFilter = filtroDifumina
```

### Patró de doble buffer (Bitmap off-screen)
Per evitar parpelleig, es dibuixa sobre un Bitmap auxiliar i després es copia al Canvas principal:

```kotlin
override fun onDraw(canvas: Canvas) {
    canvas.save()
    canvasBitmap?.drawColor(colorFons)           // Esborrar fons

    for (fp in tracos) {
        pintura.color       = fp.color
        pintura.strokeWidth = fp.ampladaTrac.toFloat()
        pintura.maskFilter  = if (fp.relleu) filtroRelleu
                              else if (fp.difumina) filtroDifumina
                              else null
        canvasBitmap?.drawPath(fp.ruta, pintura) // Dibuixar al buffer
    }
    bitmap?.let { canvas.drawBitmap(it, 0f, 0f, pinturaBitmap) } // Copiar al Canvas
    canvas.restore()
}
```

### Tolerància del tàctil
Per evitar traços massa zigzagants amb petits tremolors del dit:

```kotlin
private const val TOLERANCIA_TACTE = 4f

private fun touchMove(x: Float, y: Float) {
    val dx = Math.abs(x - xAnterior)
    val dy = Math.abs(y - yAnterior)
    if (dx >= TOLERANCIA_TACTE || dy >= TOLERANCIA_TACTE) {
        // Afegir punt al path
    }
}
```

## 📋 Resum del flux de dibuix

```
Usuari posa el dit (ACTION_DOWN)
    → touchStart() → nou Path → nou M07_PathDit afegit a la llista

Usuari mou el dit (ACTION_MOVE)
    → touchMove() → quadTo() al Path actual → invalidate()

Usuari aixeca el dit (ACTION_UP)
    → touchUp() → lineTo() per tancar el traç → invalidate()

onDraw() → recorre tota la llista de traços → dibuixa tot al Bitmap → copia al Canvas
```

---
[← M06](M06-Canvas-Personalitzat) | [Inici](Home) | [M08 →](M08-Alarmes)

