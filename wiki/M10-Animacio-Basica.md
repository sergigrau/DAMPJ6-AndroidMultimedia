# M10 – Animació Bàsica amb ViewBinding i ObjectAnimator

## Objectiu
Aprendre a animar propietats de vistes (`x`, `y`, `alpha`) utilitzant `ObjectAnimator` d'AndroidX integrat amb **ViewBinding**.

## Fitxers Involucrats
| Fitxer | Descripció |
|--------|------------|
| `M10_AnimacioBasica.kt` | Activity principal amb ViewBinding |

## Conceptes Clau

```kotlin
val animY = ObjectAnimator.ofFloat(binding.fab, "y", 0f, height / 2f).apply { duration = 1000 }
val animAlpha = ObjectAnimator.ofFloat(binding.fab, "alpha", 1f, 0f).apply { duration = 1000 }
animY.start()
animAlpha.start()
```

---
[M09](M09-Propietats-Dinamiques) | [Home](Home) | [M11](M11-Animacio-Requadre)
