# M05 – Basic Canvas

## Objective
Introduce the Android `Canvas` class to draw basic geometric shapes onto a `Bitmap` and display the result in an `ImageView`. This is the foundation for all custom 2D drawing in Android.

## Files Involved
| File | Description |
|------|-------------|
| `M05_CanvasBasic.kt` | Main activity |
| `res/layout/m05_canvas_basic_principal.xml` | Layout with Toolbar and ImageView |

## Key Concepts

### The Drawing Triad: Bitmap + Canvas + Paint

```
Bitmap  <- pixel surface (the "paper")
  |
Canvas  <- drawing tools (the "pencil")
  |
Paint   <- visual configuration (the "colour/thickness")
```

### Basic Creation and Usage

```kotlin
// 1. Create the Bitmap (100x100 pixels, ARGB colour)
val bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)

// 2. Create the Canvas associated with the Bitmap
val canvas = Canvas(bitmap)

// 3. Create the Paint with configuration
val paint = Paint(Paint.ANTI_ALIAS_FLAG)
paint.color = Color.BLACK

// 4. Draw shapes
canvas.drawRect(10f, 10f, 50f, 50f, paint)
canvas.drawCircle(50f, 50f, 10f, paint)

// 5. Display the Bitmap in an ImageView
imageView.setImageBitmap(bitmap)
```

### Main Canvas Methods
| Method | Description |
|--------|-------------|
| `drawRect(l, t, r, b, paint)` | Draws a rectangle |
| `drawCircle(cx, cy, r, paint)` | Draws a circle |
| `drawLine(x1, y1, x2, y2, paint)` | Draws a line |
| `drawText(text, x, y, paint)` | Draws text |
| `drawPath(path, paint)` | Draws a complex path |
| `drawBitmap(bmp, x, y, paint)` | Draws another Bitmap |
| `drawColor(color)` | Fills the canvas with a colour |

### Colour in Android
```kotlin
// Predefined colours
Color.BLACK, Color.RED, Color.WHITE...

// Custom ARGB colour (alpha, red, green, blue)
Color.argb(255, 128, 0, 255)  // Opaque violet

// Random colour
Color.argb(
    (Math.random() * 255).toInt(),
    (Math.random() * 255).toInt(),
    (Math.random() * 255).toInt(),
    (Math.random() * 255).toInt()
)
```

### Bitmap Configuration
| Config | Bits per pixel | Use |
|--------|---------------|-----|
| `ARGB_8888` | 32 bits | Maximum quality (recommended) |
| `RGB_565` | 16 bits | Less memory, no transparency |
| `ALPHA_8` | 8 bits | Alpha mask only |

### Paint.ANTI_ALIAS_FLAG
Smooths the edges of shapes to avoid the "staircase" effect (aliasing):

```kotlin
// Without antialiasing: jagged edges
val paint = Paint()

// With antialiasing: smooth edges
val paint = Paint(Paint.ANTI_ALIAS_FLAG)
```

## Limitation of This Approach
Drawing onto a `Bitmap` is static. For **dynamic** drawing (changing in response to the user), a custom `View` with an overridden `onDraw()` method is needed. See M06 and M07.

---
[M04](M04-Notificacions-SnackBar) | [Home](Home) | [M06](M06-Canvas-Personalitzat)
