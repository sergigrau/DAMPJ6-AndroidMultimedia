# M06 – Vista Personalitzada amb Core KTX withStyledAttributes

## Objectiu
Crear una `View` personalitzada que dibuixi amb `Canvas` i extregui atributs XML mitjançant l'extensió **`withStyledAttributes`** d'AndroidX Core KTX.

## Fitxers Involucrats
| Fitxer | Descripció |
|--------|------------|
| `M06_VistaPropia.kt` | Custom View amb KTX |
| `M06_CanvasPersonalitzat.kt` | Activity principal amb ViewBinding |

## Conceptes Clau

### withStyledAttributes (Core KTX)
Reemplaça el verbós patró tradicional `obtainStyledAttributes` / `try-finally` / `recycle()` per una sola tancadura neta i segura:

```kotlin
init {
    context.withStyledAttributes(attrs, R.styleable.M06_VistaPropia) {
        xPos = getFloat(R.styleable.M06_VistaPropia_x, 0f)
        cadena = getString(R.styleable.M06_VistaPropia_cadena)
    }
}
```

---
[M05](M05-Canvas-Basic) | [Home](Home) | [M07](M07-Canvas-Path)
