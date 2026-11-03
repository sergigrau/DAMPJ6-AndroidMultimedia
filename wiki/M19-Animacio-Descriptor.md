# M19 – Descriptor d'animació XML

## Objectiu
Aprendre a definir animacions en fitxers XML (descriptors) en lloc de codi, i carregar-los en temps d'execució amb `AnimatorInflater`. Això permet reutilitzar i mantenir les animacions de forma centralitzada.

## Fitxers involucrats
| Fitxer | Descripció |
|--------|------------|
| `M19_AnimacioDescriptor.kt` | Activitat principal |
| `res/animator/animacio.xml` | Descriptor d'animació XML |
| `res/layout/m11_animacio_requadre.xml` | Disseny reutilitzat de M11 |

## Conceptes clau

### Descriptor d'animació XML
Les animacions es defineixen a `res/animator/` (no a `res/anim/`, que és per al sistema antic de View Animation):

```xml
<!-- res/animator/animacio.xml -->
<set xmlns:android="http://schemas.android.com/apk/res/android"
    android:ordering="sequentially">

    <objectAnimator
        android:propertyName="x"
        android:duration="1000"
        android:valueTo="500"
        android:valueType="floatType" />

    <objectAnimator
        android:propertyName="y"
        android:duration="1000"
        android:valueTo="500"
        android:valueType="floatType" />

</set>
```

**Atributs principals:**
| Atribut | Descripció |
|---------|------------|
| `propertyName` | Nom de la propietat a animar |
| `duration` | Durada en ms |
| `valueFrom` | Valor inicial (opcional) |
| `valueTo` | Valor final |
| `valueType` | `floatType`, `intType` |
| `startOffset` | Retard abans de començar |
| `repeatCount` | Nombre de repeticions (-1 = infinit) |
| `repeatMode` | `restart` o `reverse` |

**Ordre de `<set>`:**
| Valor | Descripció |
|-------|------------|
| `sequentially` | Les animacions s'executen una rere l'altra |
| `together` | Les animacions s'executen en paral·lel |

### Carregar i executar l'animació

```kotlin
val set = AnimatorInflater.loadAnimator(this, R.animator.animacio) as AnimatorSet
set.setTarget(button)   // L'objecte al qual s'aplica l'animació
set.start()
```

### Comparativa entre XML i Codi

```kotlin
// Mitjançant XML (M19)
val set = AnimatorInflater.loadAnimator(this, R.animator.animacio) as AnimatorSet
set.setTarget(button)
set.start()

// Mitjançant codi (M10/M11)
val anim1 = ObjectAnimator.ofFloat(button, "x", 0f, 500f).apply { duration = 1000 }
val anim2 = ObjectAnimator.ofFloat(button, "y", 0f, 500f).apply { duration = 1000 }
AnimatorSet().apply { playSequentially(anim1, anim2); start() }
```

**Avantatges de cada enfocament:**
| | XML | Codi |
|---|---|---|
| Reutilització | Alta | Requereix refactorització |
| Valors dinàmics | Difícil | Fàcil |
| Llegibilitat | Clara | Pot ser verbosa |
| Vista prèvia IDE | Sí | No |
| Condicions lògiques | No | Sí |

## Diferència entre `res/anim/` i `res/animator/`
| Carpeta | Sistema | Classes |
|---------|---------|---------|
| `res/anim/` | View Animation (antic) | `AlphaAnimation`, `TranslateAnimation`... |
| `res/animator/` | Property Animation (modern) | `ObjectAnimator`, `AnimatorSet`... |

> Utilitzar sempre `res/animator/` per a animacions noves.

---
[M18](M18-Firebase) | [Home](Home) | [M20](M20-Animacio-Sprite)
