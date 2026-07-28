# M19 – Animació per descriptor XML

## 🎯 Objectiu
Aprendre a definir animacions en fitxers XML (descriptors) en comptes de codi, i carregar-les en temps d'execució amb `AnimatorInflater`. Permet reutilitzar i mantenir les animacions de manera centralitzada.

## 📄 Fitxers implicats
| Fitxer | Descripció |
|--------|-----------|
| `M19_AnimacioDescriptor.kt` | Activity principal |
| `res/animator/animacio.xml` | Descriptor XML de l'animació |
| `res/layout/m11_animacio_requadre.xml` | Layout reutilitzat de M11 |

## 🔑 Conceptes clau

### Descriptor XML d'animació
Les animacions es defineixen a `res/animator/` (no `res/anim/` que és per a la View Animation antiga):

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
|---------|-----------|
| `propertyName` | Nom de la propietat a animar |
| `duration` | Durada en ms |
| `valueFrom` | Valor inicial (opcional) |
| `valueTo` | Valor final |
| `valueType` | `floatType`, `intType` |
| `startOffset` | Retard abans d'iniciar |
| `repeatCount` | Nombre de repeticions (-1 = infinit) |
| `repeatMode` | `restart` o `reverse` |

**Ordenació del `<set>`:**
| Valor | Descripció |
|-------|-----------|
| `sequentially` | Les animacions s'executen una darrere l'altra |
| `together` | Les animacions s'executen en paral·lel |

### Carregar i executar l'animació

```kotlin
val set = AnimatorInflater.loadAnimator(this, R.animator.animacio) as AnimatorSet
set.setTarget(boto)   // L'objecte sobre el qual s'aplica l'animació
set.start()
```

### Comparativa: XML vs Codi

```kotlin
// ✅ Via XML (M19)
val set = AnimatorInflater.loadAnimator(this, R.animator.animacio) as AnimatorSet
set.setTarget(boto)
set.start()

// ✅ Via codi (M10/M11)
val anim1 = ObjectAnimator.ofFloat(boto, "x", 0f, 500f).apply { duration = 1000 }
val anim2 = ObjectAnimator.ofFloat(boto, "y", 0f, 500f).apply { duration = 1000 }
AnimatorSet().apply { playSequentially(anim1, anim2); start() }
```

**Avantatges de cada enfocament:**
| | XML | Codi |
|---|---|---|
| Reutilització | ✅ Alta | ⚠️ Requereix refactorització |
| Valors dinàmics | ❌ Difícil | ✅ Fàcil |
| Llegibilitat | ✅ Clara | ⚠️ Pot ser verbós |
| Preview a l'IDE | ✅ Sí | ❌ No |
| Condicions lògiques | ❌ | ✅ |

## 📁 Diferència entre `res/anim/` i `res/animator/`
| Carpeta | Sistema | Classes |
|---------|---------|---------|
| `res/anim/` | View Animation (antic) | `AlphaAnimation`, `TranslateAnimation`... |
| `res/animator/` | Property Animation (modern) | `ObjectAnimator`, `AnimatorSet`... |

> Sempre usar `res/animator/` per a noves animacions.

---
[← M18](M18-Firebase) | [Inici](Home) | [M20 →](M20-Animacio-Sprite)

