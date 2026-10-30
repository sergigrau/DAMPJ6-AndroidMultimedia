# M20 – Sprite Animation (Frame by Frame)

## Objective
Learn how to create **sprite** (frame-by-frame) animations in Android using `AnimationDrawable`: a sequence of static images displayed quickly to create the illusion of movement.

## Files Involved
| File | Description |
|------|-------------|
| `M20_AnimacioSprite.kt` | Main activity |
| `res/drawable/sprite.xml` | Sprite animation descriptor |
| `res/drawable/sprite_*.png` | Individual animation frames |
| `res/layout/m20_animacio_sprite.xml` | Layout with ImageView |

## Key Concepts

### AnimationDrawable – Frame-by-Frame Animation
`AnimationDrawable` is a special `Drawable` that contains a sequence of `Drawable` items displayed in order:

```kotlin
val imageView: ImageView = findViewById(R.id.spriteImatge)

// 1. Set the animation resource as the background
imageView.setBackgroundResource(R.drawable.sprite)

// 2. Get the AnimationDrawable
val anim = imageView.background as AnimationDrawable

// 3. Start the animation
anim.start()
```

### Sprite XML Descriptor (`res/drawable/sprite.xml`)

```xml
<animation-list xmlns:android="http://schemas.android.com/apk/res/android"
    android:oneshot="false">

    <item android:drawable="@drawable/sprite_01" android:duration="100" />
    <item android:drawable="@drawable/sprite_02" android:duration="100" />
    <item android:drawable="@drawable/sprite_03" android:duration="100" />
    <item android:drawable="@drawable/sprite_04" android:duration="100" />
    <!-- ... -->

</animation-list>
```

**Attributes:**
| Attribute | Description |
|-----------|-------------|
| `android:oneshot="false"` | Infinite loop |
| `android:oneshot="true"` | Plays only once |
| `android:duration` | Duration of each frame in ms |

### AnimationDrawable Methods
| Method | Description |
|--------|-------------|
| `start()` | Starts or resumes the animation |
| `stop()` | Stops the animation at the current frame |
| `isRunning` | `true` if currently playing |
| `numberOfFrames` | Total number of frames |
| `getFrame(index)` | Gets the Drawable for a given frame |
| `getDuration(index)` | Duration of the given frame |

### Touch Control
```kotlin
override fun onTouchEvent(event: MotionEvent): Boolean {
    if (event.action == MotionEvent.ACTION_DOWN) {
        anim.stop()    // Stop the animation when the screen is touched
        return true
    }
    return super.onTouchEvent(event)
}
```

### Known Issue: `start()` in `onCreate()`
`AnimationDrawable.start()` **does not work** if called directly in `onCreate()` because the `ImageView` has not yet been drawn. The solution is to call it in `onWindowFocusChanged()` or in a `post()`:

```kotlin
// Recommended solution
imageView.post { anim.start() }

// Alternative
override fun onWindowFocusChanged(hasFocus: Boolean) {
    super.onWindowFocusChanged(hasFocus)
    if (hasFocus) anim.start()
}
```

## Android Animation Comparison

| Type | Class | Suitable for |
|------|-------|--------------|
| **Frame-by-frame** | `AnimationDrawable` | Sprites, 2D characters |
| **Property Animation** | `ObjectAnimator` | Movement, transparency, scale |
| **View Animation** | `AlphaAnimation`... | Simple animations (deprecated) |
| **Transition** | `TransitionManager` | Layout changes |
| **Lottie** | (library) | Complex vector animations |

---
[M19](M19-Animacio-Descriptor) | [Home](Home) | [M21](M21-Servei-Suma)
