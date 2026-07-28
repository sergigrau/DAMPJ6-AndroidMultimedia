# M20 – Animació Sprite (fotograma a fotograma)

## 🎯 Objectiu
Aprendre a crear animacions de tipus **sprite** (frame-by-frame) a Android usant `AnimationDrawable`: una seqüència d'imatges estàtiques que es mostren ràpidament per crear la il·lusió de moviment.

## 📄 Fitxers implicats
| Fitxer | Descripció |
|--------|-----------|
| `M20_AnimacioSprite.kt` | Activity principal |
| `res/drawable/sprite.xml` | Descriptor de l'animació sprite |
| `res/drawable/sprite_*.png` | Fotogrames individuals de l'animació |
| `res/layout/m20_animacio_sprite.xml` | Layout amb ImageView |

## 🔑 Conceptes clau

### AnimationDrawable – Animació fotograma a fotograma
`AnimationDrawable` és un `Drawable` especial que conté una seqüència de `Drawable` que es mostren en ordre:

```kotlin
val imatge: ImageView = findViewById(R.id.spriteImatge)

// 1. Assignar el recurs de l'animació com a background
imatge.setBackgroundResource(R.drawable.sprite)

// 2. Obtenir l'AnimationDrawable
val a = imatge.background as AnimationDrawable

// 3. Iniciar l'animació
a.start()
```

### Descriptor XML del Sprite (`res/drawable/sprite.xml`)

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

**Atributs:**
| Atribut | Descripció |
|---------|-----------|
| `android:oneshot="false"` | Loop infinit |
| `android:oneshot="true"` | Es reprodueix una sola vegada |
| `android:duration` | Durada de cada fotograma en ms |

### Mètodes de AnimationDrawable
| Mètode | Descripció |
|--------|-----------|
| `start()` | Inicia o reprèn l'animació |
| `stop()` | Atura l'animació al fotograma actual |
| `isRunning` | `true` si s'està reproduint |
| `numberOfFrames` | Nombre total de fotogrames |
| `getFrame(index)` | Obté el Drawable d'un fotograma |
| `getDuration(index)` | Durada del fotograma indicat |

### Control per tàctil
```kotlin
override fun onTouchEvent(event: MotionEvent): Boolean {
    if (event.action == MotionEvent.ACTION_DOWN) {
        a.stop()    // Atura l'animació en tocar la pantalla
        return true
    }
    return super.onTouchEvent(event)
}
```

### ⚠️ Problema conegut: `start()` a `onCreate()`
`AnimationDrawable.start()` **no funciona** si es crida directament a `onCreate()` perquè el `ImageView` encara no s'ha dibuixat. La solució és cridar-la a `onWindowFocusChanged()` o en un `post()`:

```kotlin
// Solució recomanada
imatge.post { a.start() }

// Alternativa
override fun onWindowFocusChanged(hasFocus: Boolean) {
    super.onWindowFocusChanged(hasFocus)
    if (hasFocus) a.start()
}
```

> En aquest projecte funciona perquè s'inicialitza en `onCreate()` i el sistema pot optimitzar l'ordre d'execució, però per major robustesa es recomana el `post {}`.

## 📊 Comparativa d'animacions a Android

| Tipus | Classe | Adequat per |
|-------|--------|------------|
| **Frame-by-frame** | `AnimationDrawable` | Sprites, personatges 2D |
| **Property Animation** | `ObjectAnimator` | Moviment, transparència, escala |
| **View Animation** | `AlphaAnimation`... | Animacions simples (deprecated) |
| **Transition** | `TransitionManager` | Canvis de layout |
| **Lottie** | (llibreria) | Animacions vectorials complexes |

---
[← M19](M19-Animacio-Descriptor) | [Inici](Home) | [M21 →](M21-Servei-Suma)

