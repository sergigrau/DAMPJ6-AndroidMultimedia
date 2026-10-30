# M19 – XML Animation Descriptor

## Objective
Learn how to define animations in XML files (descriptors) instead of code, and load them at runtime with `AnimatorInflater`. This allows animations to be reused and maintained in a centralised way.

## Files Involved
| File | Description |
|------|-------------|
| `M19_AnimacioDescriptor.kt` | Main activity |
| `res/animator/animacio.xml` | XML animation descriptor |
| `res/layout/m11_animacio_requadre.xml` | Layout reused from M11 |

## Key Concepts

### XML Animation Descriptor
Animations are defined in `res/animator/` (not `res/anim/` which is for the old View Animation system):

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

**Main attributes:**
| Attribute | Description |
|-----------|-------------|
| `propertyName` | Name of the property to animate |
| `duration` | Duration in ms |
| `valueFrom` | Initial value (optional) |
| `valueTo` | Final value |
| `valueType` | `floatType`, `intType` |
| `startOffset` | Delay before starting |
| `repeatCount` | Number of repetitions (-1 = infinite) |
| `repeatMode` | `restart` or `reverse` |

**`<set>` ordering:**
| Value | Description |
|-------|-------------|
| `sequentially` | Animations run one after another |
| `together` | Animations run in parallel |

### Loading and Running the Animation

```kotlin
val set = AnimatorInflater.loadAnimator(this, R.animator.animacio) as AnimatorSet
set.setTarget(button)   // The object the animation is applied to
set.start()
```

### XML vs Code Comparison

```kotlin
// Via XML (M19)
val set = AnimatorInflater.loadAnimator(this, R.animator.animacio) as AnimatorSet
set.setTarget(button)
set.start()

// Via code (M10/M11)
val anim1 = ObjectAnimator.ofFloat(button, "x", 0f, 500f).apply { duration = 1000 }
val anim2 = ObjectAnimator.ofFloat(button, "y", 0f, 500f).apply { duration = 1000 }
AnimatorSet().apply { playSequentially(anim1, anim2); start() }
```

**Advantages of each approach:**
| | XML | Code |
|---|---|---|
| Reusability | High | Requires refactoring |
| Dynamic values | Difficult | Easy |
| Readability | Clear | Can be verbose |
| IDE preview | Yes | No |
| Logic conditions | No | Yes |

## Difference Between `res/anim/` and `res/animator/`
| Folder | System | Classes |
|--------|--------|---------|
| `res/anim/` | View Animation (old) | `AlphaAnimation`, `TranslateAnimation`... |
| `res/animator/` | Property Animation (modern) | `ObjectAnimator`, `AnimatorSet`... |

> Always use `res/animator/` for new animations.

---
[M18](M18-Firebase) | [Home](Home) | [M20](M20-Animacio-Sprite)
