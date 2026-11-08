# M15 – OpenGL ES 2.0 amb DefaultLifecycleObserver

## Objectiu
Aprendre a integrar una vista de gràfics 3D **OpenGL ES 2.0** (`GLSurfaceView`) gestionant automàticament les pauses i represes del renderitzat mitjançant el **`DefaultLifecycleObserver`** de Jetpack Lifecycle.

## Fitxers Involucrats
| Fitxer | Descripció |
|--------|------------|
| `M15_OpenGLES20Activity.kt` | Activity que implementa `DefaultLifecycleObserver` |
| `M15_OpenGLES20GLSurfaceView.kt` | Vista GLSurfaceView |
| `M15_OpenGLES20Renderer.kt` | Renderitzador OpenGL ES 2.0 |

## Conceptes Clau

### DefaultLifecycleObserver
L'Activitat implementa `DefaultLifecycleObserver` per pausar i reprendre `GLSurfaceView` sense acoblar la lògica manualment a `onPause()` o `onResume()`:

```kotlin
class M15_OpenGLES20Activity : AppCompatActivity(), DefaultLifecycleObserver {

    private lateinit var mGLView: GLSurfaceView

    override fun onCreate(savedInstanceState: Bundle?) {
        super<AppCompatActivity>.onCreate(savedInstanceState)
        mGLView = M15_OpenGLES20GLSurfaceView(this)
        setContentView(mGLView)

        lifecycle.addObserver(this)
    }

    override fun onResume(owner: LifecycleOwner) {
        super<DefaultLifecycleObserver>.onResume(owner)
        mGLView.onResume()
    }

    override fun onPause(owner: LifecycleOwner) {
        super<DefaultLifecycleObserver>.onPause(owner)
        mGLView.onPause()
    }
}
```

---
[M14](M14-Imatge-SD) | [Home](Home) | [M16](M16-Lectura-XML)
