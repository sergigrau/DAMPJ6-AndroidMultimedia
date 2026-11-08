# M19 – Animador XML amb ViewBinding

## Objectiu
Carregar i executar animacions XML declaratives definides a `res/animator/` utilitzant `AnimatorInflater` i **ViewBinding** d'AndroidX.

## Fitxers Involucrats
| Fitxer | Descripció |
|--------|------------|
| `M19_AnimacioDescriptor.kt` | Activity principal amb ViewBinding |
| `res/animator/animacio.xml` | Animació XML |

## Conceptes Clau

```kotlin
val set = AnimatorInflater.loadAnimator(this, R.animator.animacio) as AnimatorSet
set.setTarget(binding.boto)
set.start()
```

---
[M18](M18-Firebase) | [Home](Home) | [M20](M20-Animacio-Sprite)
