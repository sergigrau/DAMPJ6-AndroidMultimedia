# M10 – Animació bàsica amb ObjectAnimator

## Objectiu
Aprendre a animar propietats de vistes (`x`, `y`, `alpha`, `scaleX`, `rotation`...) amb `ObjectAnimator`, el nucli del sistema d'animació de propietats d'Android.

## Fitxers involucrats
| Fitxer | Descripció |
|--------|------------|
| `M10_AnimacioBasica.kt` | Activitat principal |
| `res/layout/m10_animacio_basica.xml` | Disseny amb Toolbar i FAB |

---

# M11 – Animació seqüencial amb AnimatorSet

## Objectiu
Combinar múltiples instàncies d'`ObjectAnimator` en seqüència o en paral·lel mitjançant `AnimatorSet`, i crear animacions cícliques que es repeteixen indefinidament.

## Fitxers involucrats
| Fitxer | Descripció |
|--------|------------|
| `M11_AnimacioRequadre.kt` | Activitat principal |
| `res/layout/m11_animacio_requadre.xml` | Disseny amb un Botó |

---

## Conceptes clau (M10 i M11)

### ObjectAnimator – Animació d'una sola propietat
`ObjectAnimator` anima una propietat específica d'un objecte des d'un valor inicial fins a un de final:

```kotlin
// Moure el FAB verticalment fins al mig de la pantalla
val anim = ObjectAnimator.ofFloat(fab, "y", 0f, height / 2f)
anim.duration = 1000  // ms
anim.start()

// Desfermat (fade out) del FAB
val animAlpha = ObjectAnimator.ofFloat(fab, "alpha", 1f, 0f)
animAlpha.duration = 1000
animAlpha.start()
```

### Propietats animables
| Propietat (String) | Descripció |
|-------------------|------------|
| `"x"`, `"y"` | Posició absoluta |
| `"translationX"`, `"translationY"` | Desplaçament relatiu |
| `"alpha"` | Transparència (0.0 = transparent, 1.0 = opac) |
| `"scaleX"`, `"scaleY"` | Escala horitzontal / vertical |
| `"rotation"` | Rotació en graus |
| `"rotationX"`, `"rotationY"` | Rotació 3D |

### AnimatorSet – Combinació d'animadors

```kotlin
val anim1 = ObjectAnimator.ofFloat(button, "x", 0f, (width - button.width).toFloat()).apply { duration = 1000 }
val anim2 = ObjectAnimator.ofFloat(button, "y", 0f, (height - button.height).toFloat()).apply { duration = 1000 }
val anim3 = ObjectAnimator.ofFloat(button, "x", (width - button.width).toFloat(), 0f).apply { duration = 1000 }
val anim4 = ObjectAnimator.ofFloat(button, "y", (height - button.height).toFloat(), 0f).apply { duration = 1000 }

val animSet = AnimatorSet()
animSet.playSequentially(anim1, anim2, anim3, anim4)
animSet.start()
```

**Modes de combinació:**
| Mètode | Comportament |
|--------|--------------|
| `playSequentially(a1, a2, a3)` | Un rere l'altre |
| `playTogether(a1, a2, a3)` | Tots al mateix temps |
| `play(a1).before(a2)` | a1 primer, després a2 |
| `play(a1).after(a2)` | a2 primer, després a1 |
| `play(a1).with(a2)` | Tots dos al mateix temps |

### Bucle infinit amb AnimatorListenerAdapter

```kotlin
animSet.addListener(object : AnimatorListenerAdapter() {
    override fun onAnimationEnd(a: Animator) {
        super.onAnimationEnd(a)
        animSet.start()  // Reiniciar quan acabi -> bucle infinit
    }
})
```

### WindowMetrics (API 30+) per obtenir dimensions de la pantalla

```kotlin
val metrics: WindowMetrics = windowManager.currentWindowMetrics
val bounds: Rect = metrics.bounds
val height = bounds.height()
val width  = bounds.width()
```

### Interpoladors – Velocitat de l'animació
`ObjectAnimator` accepta `interpolators` per controlar el ritme:

```kotlin
anim.interpolator = AccelerateDecelerateInterpolator() // Per defecte
anim.interpolator = BounceInterpolator()               // Efecte de rebot
anim.interpolator = OvershootInterpolator()            // Sobrepassa el punt final
anim.interpolator = LinearInterpolator()               // Velocitat constant
```

## Comparativa M10 vs M11
| | M10 | M11 |
|---|---|---|
| Animadors | 1 per propietat | 4 combinats |
| Combinació | Independent | `AnimatorSet.playSequentially` |
| Bucle | No | Sí (listener) |
| Efectes | Traslació + Alpha | Traslació rectangular |

---
[M09](M09-Propietats-Dinamiques) | [Home](Home) | [M12](M12-Camera-Intent)
