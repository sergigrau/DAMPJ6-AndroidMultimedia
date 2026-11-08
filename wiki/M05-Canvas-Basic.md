# M05 – Canvas Bàsic amb Jetpack Core Graphics KTX

## Objectiu
Utilitzar la classe `Canvas` d'Android conjuntament amb les utilitats **Graphics KTX** d'AndroidX Core (`createBitmap`) i **ViewBinding** per a dibuixar figures 2D sobre un `Bitmap`.

## Fitxers Involucrats
| Fitxer | Descripció |
|--------|------------|
| `M05_CanvasBasic.kt` | Activity principal amb ViewBinding |

## Conceptes Clau

### Extensions Graphics KTX (`createBitmap`)
AndroidX Core proporciona la funció d'extensió `createBitmap` que simplifica la creació de Bitmaps:

```kotlin
val bitmap = createBitmap(100, 100)
val canvas = Canvas(bitmap)
val paint = Paint(Paint.ANTI_ALIAS_FLAG)
paint.color = Color.BLACK
canvas.drawRect(10f, 10f, 90f, 90f, paint)

binding.contingut.imatge.setImageBitmap(bitmap)
```

---
[M04](M04-Notificacions-SnackBar) | [Home](Home) | [M06](M06-Canvas-Personalitzat)
