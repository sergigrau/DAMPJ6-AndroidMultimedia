# M07 – Dibuix a Mà Alçada amb Path i ViewBinding

## Objectiu
Implementar una aplicació de dibuix a mà alçada on l'usuari pugui pintar amb el dit utilitzant `Path`, `MaskFilter` i la integració de **ViewBinding** de Jetpack.

## Fitxers Involucrats
| Fitxer | Descripció |
|--------|------------|
| `M07_CanvasPath.kt` | Activity principal amb ViewBinding |
| `M07_VistaPaint.kt` | Vista personalitzada de dibuix |
| `M07_PathDit.kt` | Data class immutable del traç |

## Conceptes Clau

### Integració amb ViewBinding
Accés segur i directe a la vista de pintura (`binding.contingut.paintView`):

```kotlin
vistaPaint = binding.contingut.paintView
val bounds: Rect = windowManager.currentWindowMetrics.bounds
val metrics = DisplayMetrics().apply {
    widthPixels = bounds.width()
    heightPixels = bounds.height()
}
vistaPaint.init(metrics)
```

---
[M06](M06-Canvas-Personalitzat) | [Home](Home) | [M08](M08-Alarmes)
