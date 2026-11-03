# M05 – Canvas Bàsic

## Objectiu
Introduir la classe `Canvas` d'Android per dibuixar figures geomètriques bàsiques sobre un `Bitmap` i mostrar el resultat en un `ImageView`. Aquesta és la base de tot el dibuix 2D personalitzat a Android.

## Fitxers Involucrats
| Fitxer | Descripció |
|--------|------------|
| `M05_CanvasBasic.kt` | Activity principal |
| `res/layout/m05_canvas_basic_principal.xml` | Layout amb Toolbar i ImageView |

## Conceptes Clau

### La Tríada de Dibuix: Bitmap + Canvas + Paint

```
Bitmap  <- superfície de píxels (el "paper")
  |
Canvas  <- eines de dibuix (el "llapis")
  |
Paint   <- configuració visual (el "color/gruix")
```

### Creació i Ús Bàsic

```kotlin
// 1. Crea el Bitmap (100x100 píxels, color ARGB)
val bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)

// 2. Crea el Canvas associat al Bitmap
val canvas = Canvas(bitmap)

// 3. Crea el Paint amb configuració
val paint = Paint(Paint.ANTI_ALIAS_FLAG)
paint.color = Color.BLACK

// 4. Dibuixa figures
canvas.drawRect(10f, 10f, 50f, 50f, paint)
canvas.drawCircle(50f, 50f, 10f, paint)

// 5. Mostra el Bitmap en un ImageView
imageView.setImageBitmap(bitmap)
```

### Mètodes Principals de Canvas
| Mètode | Descripció |
|--------|------------|
| `drawRect(l, t, r, b, paint)` | Dibuixa un rectangle |
| `drawCircle(cx, cy, r, paint)` | Dibuixa un cercle |
| `drawLine(x1, y1, x2, y2, paint)` | Dibuixa una línia |
| `drawText(text, x, y, paint)` | Dibuixa text |
| `drawPath(path, paint)` | Dibuixa un camí complex |
| `drawBitmap(bmp, x, y, paint)` | Dibuixa un altre Bitmap |
| `drawColor(color)` | Omple el canvas amb un color |

### Color a Android
```kotlin
// Colors predefinits
Color.BLACK, Color.RED, Color.WHITE...

// Color ARGB personalitzat (alfa, vermell, verd, blau)
Color.argb(255, 128, 0, 255)  // Violeta opac

// Color aleatori
Color.argb(
    (Math.random() * 255).toInt(),
    (Math.random() * 255).toInt(),
    (Math.random() * 255).toInt(),
    (Math.random() * 255).toInt()
)
```

### Configuració de Bitmap
| Configuració | Bits per píxel | Ús |
|--------------|----------------|----|
| `ARGB_8888` | 32 bits | Màxima qualitat (recomanat) |
| `RGB_565` | 16 bits | Menys memòria, sense transparència |
| `ALPHA_8` | 8 bits | Només màscara alfa |

### Paint.ANTI_ALIAS_FLAG
Suavitza les vores de les figures per evitar l'efecte "escala" (aliasing):

```kotlin
// Sense antialiasing: vores serrades
val paint = Paint()

// Amb antialiasing: vores suaus
val paint = Paint(Paint.ANTI_ALIAS_FLAG)
```

## Limitació d'Aquest Enfocament
Dibuixar sobre un `Bitmap` és estàtic. Per a un dibuix **dinàmic** (que canviï en resposta a l'usuari), cal una `View` personalitzada amb el mètode `onDraw()` sobreescrit. Vegeu M06 i M07.

---
[M04](M04-Notificacions-SnackBar) | [Home](Home) | [M06](M06-Canvas-Personalitzat)
