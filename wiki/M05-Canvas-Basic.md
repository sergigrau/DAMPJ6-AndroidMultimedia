# M05 – Canvas bàsic

## 🎯 Objectiu
Introduir la classe `Canvas` d'Android per dibuixar formes geomètriques bàsiques sobre un `Bitmap`, i mostrar el resultat en un `ImageView`. Aquest és el fonament per a tot el dibuix 2D personalitzat a Android.

## 📄 Fitxers implicats
| Fitxer | Descripció |
|--------|-----------|
| `M05_CanvasBasic.kt` | Activity principal |
| `res/layout/m05_canvas_basic_principal.xml` | Layout amb Toolbar i ImageView |

## 🔑 Conceptes clau

### La tríada del dibuix: Bitmap + Canvas + Paint

```
Bitmap  ← superfície de píxels (el "paper")
  │
Canvas  ← eines de dibuix (el "llapis")
  │
Paint   ← configuració visual (el "color/gruix")
```

### Creació i ús bàsic

```kotlin
// 1. Crear el Bitmap (100x100 píxels, color ARGB)
val bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)

// 2. Crear el Canvas associat al Bitmap
val canvas = Canvas(bitmap)

// 3. Crear el Paint amb configuració
val paint = Paint(Paint.ANTI_ALIAS_FLAG)
paint.color = Color.BLACK

// 4. Dibuixar formes
canvas.drawRect(10f, 10f, 50f, 50f, paint)
canvas.drawCircle(50f, 50f, 10f, paint)

// 5. Mostrar el Bitmap en un ImageView
imageView.setImageBitmap(bitmap)
```

### Mètodes principals de Canvas
| Mètode | Descripció |
|--------|-----------|
| `drawRect(l, t, r, b, paint)` | Dibuixa un rectangle |
| `drawCircle(cx, cy, r, paint)` | Dibuixa un cercle |
| `drawLine(x1, y1, x2, y2, paint)` | Dibuixa una línia |
| `drawText(text, x, y, paint)` | Dibuixa text |
| `drawPath(path, paint)` | Dibuixa un traç complex |
| `drawBitmap(bmp, x, y, paint)` | Dibuixa un altre Bitmap |
| `drawColor(color)` | Omple el canvas d'un color |

### Color a Android
```kotlin
// Colors predefinits
Color.BLACK, Color.RED, Color.WHITE...

// Color personalitzat ARGB (alpha, red, green, blue)
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
| Config | Bits per píxel | Ús |
|--------|---------------|-----|
| `ARGB_8888` | 32 bits | Qualitat màxima (recomanat) |
| `RGB_565` | 16 bits | Menys memòria, sense transparència |
| `ALPHA_8` | 8 bits | Només màscara d'alpha |

### Paint.ANTI_ALIAS_FLAG
Suavitza les vores de les formes per evitar l'efecte "escala de pols" (*aliasing*):

```kotlin
// Sense antialiasing: vores dentades
val paint = Paint()

// Amb antialiasing: vores suaus
val paint = Paint(Paint.ANTI_ALIAS_FLAG)
```

## ⚠️ Limitació d'aquest enfocament
Dibuixar sobre un `Bitmap` és estàtic. Si es vol un dibuix **dinàmic** (que canviï en resposta a l'usuari), cal crear una `View` personalitzada i sobreescriure `onDraw()` → Vegeu M06 i M07.

---
[← M04](M04-Notificacions-SnackBar) | [Inici](Home) | [M06 →](M06-Canvas-Personalitzat)

