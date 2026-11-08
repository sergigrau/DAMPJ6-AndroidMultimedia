# M20 – Animació Sprite amb ViewBinding i DefaultLifecycleObserver

## Objectiu
Crear animacions per sprites (fotograma a fotograma) amb `AnimationDrawable`, **ViewBinding** i control automàtic del cicle de vida amb **`DefaultLifecycleObserver`**.

## Fitxers Involucrats
| Fitxer | Descripció |
|--------|------------|
| `M20_AnimacioSprite.kt` | Activity que implementa `DefaultLifecycleObserver` |
| `res/drawable/sprite.xml` | Descriptor `animation-list` |

## Conceptes Clau

### Gestió automàtica del cicle de vida de l'animació
L'animació s'inicia a `onStart()` i s'atura a `onStop()` automàticament mitjançant `DefaultLifecycleObserver`:

```kotlin
class M20_AnimacioSprite : AppCompatActivity(), DefaultLifecycleObserver {

    private lateinit var binding: M20AnimacioSpriteBinding
    private lateinit var animationDrawable: AnimationDrawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super<AppCompatActivity>.onCreate(savedInstanceState)
        binding = M20AnimacioSpriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.spriteImatge.setBackgroundResource(R.drawable.sprite)
        animationDrawable = binding.spriteImatge.background as AnimationDrawable

        lifecycle.addObserver(this)
    }

    override fun onStart(owner: LifecycleOwner) {
        super<DefaultLifecycleObserver>.onStart(owner)
        animationDrawable.start()
    }

    override fun onStop(owner: LifecycleOwner) {
        super<DefaultLifecycleObserver>.onStop(owner)
        animationDrawable.stop()
    }
}
```

---
[M19](M19-Animacio-Descriptor) | [Home](Home) | [M21](M21-Servei-Suma)
