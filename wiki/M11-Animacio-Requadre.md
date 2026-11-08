# M11 – Animació Seqüencial amb AnimatorSet i ViewBinding

## Objectiu
Combinar múltiples instàncies d'`ObjectAnimator` en seqüència utilitzant `AnimatorSet` i **ViewBinding** per crear animacions cícliques.

## Fitxers Involucrats
| Fitxer | Descripció |
|--------|------------|
| `M11_AnimacioRequadre.kt` | Activity principal |

## Conceptes Clau

```kotlin
val animSet = AnimatorSet()
animSet.playSequentially(anim1, anim2, anim3, anim4)
animSet.start()
```

---
[M10](M10-Animacio-Basica) | [Home](Home) | [M12](M12-Camera-Intent)
