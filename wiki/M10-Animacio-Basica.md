# M10 – Animació bàsica amb ObjectAnimator

## 🎯 Objectiu
Aprendre a animar propietats de vistes (`x`, `y`, `alpha`, `scaleX`, `rotation`...) amb `ObjectAnimator`, el mecanisme d'animació de propietats d'Android (Property Animation System).

## 📄 Fitxers implicats
| Fitxer | Descripció |
|--------|-----------|
| `M10_AnimacioBasica.kt` | Activity principal |
| `res/layout/m10_animacio_basica.xml` | Layout amb Toolbar i FAB |

---

# M11 – Animació seqüencial amb AnimatorSet

## 🎯 Objectiu
Combinar múltiples `ObjectAnimator` en seqüència o en paral·lel amb `AnimatorSet`, i crear animacions cícliques que es repeteixen indefinidament.

## 📄 Fitxers implicats
| Fitxer | Descripció |
|--------|-----------|
| `M11_AnimacioRequadre.kt` | Activity principal |
| `res/layout/m11_animacio_requadre.xml` | Layout amb un Button |

---

## 🔑 Conceptes clau comuns

### ObjectAnimator – Animació d'una propietat
`ObjectAnimator` anima una propietat específica d'un objecte de `valor_inici` a `valor_final`:

```kotlin
// Mou el FAB verticalment fins a la meitat de la pantalla
val anim = ObjectAnimator.ofFloat(fab, "y", 0f, alcada / 2f)
anim.duration = 1000  // ms
anim.start()

// Desvaneix el FAB
val animAlpha = ObjectAnimator.ofFloat(fab, "alpha", 1f, 0f)
animAlpha.duration = 1000
animAlpha.start()
```

### Propietats animables
| Propietat (String) | Descripció |
|--------------------|-----------|
| `"x"`, `"y"` | Posició absoluta |
| `"translationX"`, `"translationY"` | Desplaçament relatiu |
| `"alpha"` | Transparència (0.0 = transparent, 1.0 = opac) |
| `"scaleX"`, `"scaleY"` | Escala horitzontal / vertical |
| `"rotation"` | Rotació en graus |
| `"rotationX"`, `"rotationY"` | Rotació en 3D |

### AnimatorSet – Combinació d'animadors

```kotlin
val animacio1 = ObjectAnimator.ofFloat(boto, "x", 0f, (amplada - boto.width).toFloat()).apply { duration = 1000 }
val animacio2 = ObjectAnimator.ofFloat(boto, "y", 0f, (alcada - boto.height).toFloat()).apply { duration = 1000 }
val animacio3 = ObjectAnimator.ofFloat(boto, "x", (amplada - boto.width).toFloat(), 0f).apply { duration = 1000 }
val animacio4 = ObjectAnimator.ofFloat(boto, "y", (alcada - boto.height).toFloat(), 0f).apply { duration = 1000 }

val animacio = AnimatorSet()
animacio.playSequentially(animacio1, animacio2, animacio3, animacio4)
animacio.start()
```

**Modes de combinació:**
| Mètode | Comportament |
|--------|-------------|
| `playSequentially(a1, a2, a3)` | Una darrere l'altra |
| `playTogether(a1, a2, a3)` | Totes alhora |
| `play(a1).before(a2)` | a1 primer, a2 després |
| `play(a1).after(a2)` | a2 primer, a1 després |
| `play(a1).with(a2)` | Les dues alhora |

### Animació en bucle amb AnimatorListenerAdapter

```kotlin
animacio.addListener(object : AnimatorListenerAdapter() {
    override fun onAnimationEnd(a: Animator) {
        super.onAnimationEnd(a)
        animacio.start()  // Reinicia en acabar → bucle infinit
    }
})
```

### WindowMetrics (API 30+) per obtenir dimensions de pantalla

```kotlin
val metrics: WindowMetrics = windowManager.currentWindowMetrics
val bounds: Rect = metrics.bounds
val alcada = bounds.height()
val amplada = bounds.width()
```

### Interpolators – Velocitat de l'animació
`ObjectAnimator` admet `interpolators` per controlar el ritme:

```kotlin
anim.interpolator = AccelerateDecelerateInterpolator() // Per defecte
anim.interpolator = BounceInterpolator()               // Efecte rebot
anim.interpolator = OvershootInterpolator()            // Passa del punt final
anim.interpolator = LinearInterpolator()               // Velocitat constant
```

## 📋 Comparativa M10 vs M11
| | M10 | M11 |
|---|---|---|
| Animadors | 1 per propietat | 4 combinats |
| Combinació | Independent | `AnimatorSet.playSequentially` |
| Bucle | No | Sí (listener) |
| Efectes | Translació + Alpha | Translació rectangular |

---
[← M09](M09-Propietats-Dinamiques) | [Inici](Home) | [M12 →](M12-Camera-Intent)

