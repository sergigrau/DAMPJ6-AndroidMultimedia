# M20 – Animació per Sprites (fotograma a fotograma)

## Objectiu
Aprendre a crear animacions de tipus **sprite** (fotograma a fotograma) a Android utilitzant `AnimationDrawable`: una seqüència d'imatges estàtiques que es mostren ràpidament per crear la il·lusió de moviment.

## Fitxers involucrats
| Fitxer | Descripció |
|--------|------------|
| `M20_AnimacioSprite.kt` | Activitat principal |
| `res/drawable/sprite.xml` | Descriptor d'animació per sprites |
| `res/drawable/sprite_*.png` | Fotogrames individuals de l'animació |
| `res/layout/m20_animacio_sprite.xml` | Disseny amb ImageView |

## Conceptes clau

### AnimationDrawable – Animació fotograma a fotograma
`AnimationDrawable` és un `Drawable` especial que conté una seqüència d'elements `Drawable` mostrats en ordre:

```kotlin
val imageView: ImageView = findViewById(R.id.spriteImatge)

// 1. Assignar el recurs d'animació com a fons
imageView.setBackgroundResource(R.drawable.sprite)

// 2. Obtenir l'AnimationDrawable
val anim = imageView.background as AnimationDrawable

// 3. Iniciar l'animació
anim.start()
```

### Descriptor XML de Sprites (`res/drawable/sprite.xml`)

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
|---------|------------|
| `android:oneshot="false"` | Bucle infinit |
| `android:oneshot="true"` | Es reprodueix només una vegada |
| `android:duration` | Durada de cada fotograma en ms |

### Mètodes d'AnimationDrawable
| Mètode | Descripció |
|--------|------------|
| `start()` | Inicia o reprèn l'animació |
| `stop()` | Atura l'animació al fotograma actual |
| `isRunning` | `true` si s'està reproduint |
| `numberOfFrames` | Nombre total de fotogrames |
| `getFrame(index)` | Obté el Drawable per a un fotograma donat |
| `getDuration(index)` | Durada del fotograma donat |

### Control per tacte
```kotlin
override fun onTouchEvent(event: MotionEvent): Boolean {
    if (event.action == MotionEvent.ACTION_DOWN) {
        anim.stop()    // Aturar l'animació quan es toca la pantalla
        return true
    }
    return super.onTouchEvent(event)
}
```

### Problema conegut: `start()` a `onCreate()`
`AnimationDrawable.start()` **no funciona** si es crida directament a `onCreate()` perquè l'`ImageView` encara no s'ha dibuixat. La solució és cridar-lo a `onWindowFocusChanged()` o en un `post()`:

```kotlin
// Solució recomanada
imageView.post { anim.start() }

// Alternativa
override fun onWindowFocusChanged(hasFocus: Boolean) {
    super.onWindowFocusChanged(hasFocus)
    if (hasFocus) anim.start()
}
```

## Comparativa d'animacions a Android

| Tipus | Classe | Recomanat per a |
|-------|--------|-----------------|
| **Fotograma a fotograma** | `AnimationDrawable` | Sprites, personatges 2D |
| **Property Animation** | `ObjectAnimator` | Moviment, transparència, escala |
| **View Animation** | `AlphaAnimation`... | Animacions simples (obsolet) |
| **Transició** | `TransitionManager` | Canvis de disseny |
| **Lottie** | (llibreria) | Animacions vectorials complexes |

---
[M19](M19-Animacio-Descriptor) | [Home](Home) | [M21](M21-Servei-Suma)
