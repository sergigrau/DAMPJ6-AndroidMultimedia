# M09 – Vistes Dinàmiques amb Jetpack ConstraintLayout i ConstraintSet

## Objectiu
Aprendre a crear vistes programàticament i definir les seves restriccions visuals en temps d'execució utilitzant **Jetpack ConstraintLayout** i **ConstraintSet**.

## Fitxers Involucrats
| Fitxer | Descripció |
|--------|------------|
| `M09_Propietats_dinamiques.kt` | Activity principal amb ConstraintSet dinàmic |

## Conceptes Clau

### ConstraintSet Dinàmic
`ConstraintSet` permet programar qualsevol connexió de disseny de manera dinàmica:

```kotlin
val constraintLayout = ConstraintLayout(this)
val boto = Button(this).apply { id = View.generateViewId() }
constraintLayout.addView(boto)

val constraintSet = ConstraintSet()
constraintSet.clone(constraintLayout)
constraintSet.connect(boto.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 64)
constraintSet.connect(boto.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 32)
constraintSet.applyTo(constraintLayout)
```

---
[M08](M08-Alarmes) | [Home](Home) | [M10](M10-Animacio-Basica)
