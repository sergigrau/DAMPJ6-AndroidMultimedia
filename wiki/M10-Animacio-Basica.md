# M10 – Basic Animation with ObjectAnimator

## Objective
Learn how to animate view properties (`x`, `y`, `alpha`, `scaleX`, `rotation`...) with `ObjectAnimator`, the core of Android's Property Animation System.

## Files Involved
| File | Description |
|------|-------------|
| `M10_AnimacioBasica.kt` | Main activity |
| `res/layout/m10_animacio_basica.xml` | Layout with Toolbar and FAB |

---

# M11 – Sequential Animation with AnimatorSet

## Objective
Combine multiple `ObjectAnimator` instances in sequence or in parallel using `AnimatorSet`, and create cyclic animations that repeat indefinitely.

## Files Involved
| File | Description |
|------|-------------|
| `M11_AnimacioRequadre.kt` | Main activity |
| `res/layout/m11_animacio_requadre.xml` | Layout with a Button |

---

## Key Concepts (M10 and M11)

### ObjectAnimator – Single Property Animation
`ObjectAnimator` animates a specific property of an object from a start value to an end value:

```kotlin
// Move the FAB vertically to the middle of the screen
val anim = ObjectAnimator.ofFloat(fab, "y", 0f, height / 2f)
anim.duration = 1000  // ms
anim.start()

// Fade the FAB out
val animAlpha = ObjectAnimator.ofFloat(fab, "alpha", 1f, 0f)
animAlpha.duration = 1000
animAlpha.start()
```

### Animatable Properties
| Property (String) | Description |
|-------------------|-------------|
| `"x"`, `"y"` | Absolute position |
| `"translationX"`, `"translationY"` | Relative displacement |
| `"alpha"` | Transparency (0.0 = transparent, 1.0 = opaque) |
| `"scaleX"`, `"scaleY"` | Horizontal / vertical scale |
| `"rotation"` | Rotation in degrees |
| `"rotationX"`, `"rotationY"` | 3D rotation |

### AnimatorSet – Combining Animators

```kotlin
val anim1 = ObjectAnimator.ofFloat(button, "x", 0f, (width - button.width).toFloat()).apply { duration = 1000 }
val anim2 = ObjectAnimator.ofFloat(button, "y", 0f, (height - button.height).toFloat()).apply { duration = 1000 }
val anim3 = ObjectAnimator.ofFloat(button, "x", (width - button.width).toFloat(), 0f).apply { duration = 1000 }
val anim4 = ObjectAnimator.ofFloat(button, "y", (height - button.height).toFloat(), 0f).apply { duration = 1000 }

val animSet = AnimatorSet()
animSet.playSequentially(anim1, anim2, anim3, anim4)
animSet.start()
```

**Combination modes:**
| Method | Behaviour |
|--------|-----------|
| `playSequentially(a1, a2, a3)` | One after another |
| `playTogether(a1, a2, a3)` | All at the same time |
| `play(a1).before(a2)` | a1 first, then a2 |
| `play(a1).after(a2)` | a2 first, then a1 |
| `play(a1).with(a2)` | Both at the same time |

### Infinite Loop with AnimatorListenerAdapter

```kotlin
animSet.addListener(object : AnimatorListenerAdapter() {
    override fun onAnimationEnd(a: Animator) {
        super.onAnimationEnd(a)
        animSet.start()  // Restart when finished -> infinite loop
    }
})
```

### WindowMetrics (API 30+) to Get Screen Dimensions

```kotlin
val metrics: WindowMetrics = windowManager.currentWindowMetrics
val bounds: Rect = metrics.bounds
val height = bounds.height()
val width  = bounds.width()
```

### Interpolators – Animation Speed
`ObjectAnimator` accepts `interpolators` to control the pace:

```kotlin
anim.interpolator = AccelerateDecelerateInterpolator() // Default
anim.interpolator = BounceInterpolator()               // Bounce effect
anim.interpolator = OvershootInterpolator()            // Overshoots end point
anim.interpolator = LinearInterpolator()               // Constant speed
```

## M10 vs M11 Comparison
| | M10 | M11 |
|---|---|---|
| Animators | 1 per property | 4 combined |
| Combination | Independent | `AnimatorSet.playSequentially` |
| Loop | No | Yes (listener) |
| Effects | Translation + Alpha | Rectangular translation |

---
[M09](M09-Propietats-Dinamiques) | [Home](Home) | [M12](M12-Camera-Intent)
