# M17 – Fragments

## Objectiu
Aprendre a utilitzar **Fragments** per crear interfícies modulars que s'adapten a diferents mides de pantalla (tauletes vs telèfons). Implementa el patró **llista-detall**: un Fragment mostra una llista de cursos i l'altre permet afegir-ne de nous.

## Fitxers involucrats
| Fitxer | Descripció |
|--------|------------|
| `M17_FragmentsActivity.kt` | Activitat principal que gestiona els Fragments |
| `M17_LlistaFragmentActivity.kt` | Fragment amb un ListView de cursos |
| `M17_DetallFragmentActivity.kt` | Fragment amb un formulari per afegir cursos |
| `res/layout/m17_fragments.xml` | Disseny principal amb dos contenidors |
| `res/layout/m17_fragment_llista.xml` | Disseny del Fragment de llista |
| `res/layout/m17_fragment_detall.xml` | Disseny del Fragment de detall |
| `res/menu/m17_fragments.xml` | Menú per canviar de fragment |

## Conceptes clau

### Què és un Fragment?
Un **Fragment** és un component reutilitzable amb la seva pròpia UI i cicle de vida que sempre viu dins d'una Activitat. Permet:
- Compondre interfícies complexes combinant múltiples fragments.
- Reutilitzar el mateix fragment en múltiples Activitats.
- Adaptar la UI a tauletes (multi-panell) i telèfons (un sol panell).

### Estructura d'un Fragment

```kotlin
class M17_LlistaFragmentActivity : Fragment() {

    private lateinit var listView: ListView
    private lateinit var itemsAdapter: ArrayAdapter<String>

    // Equivalent a onCreate() per als Fragments
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.m17_fragment_llista, container, false)
        listView = view.findViewById(R.id.llista)
        itemsAdapter = ArrayAdapter(
            view.context,
            android.R.layout.simple_list_item_1,
            M17_FragmentsActivity.CURSOS
        )
        listView.adapter = itemsAdapter
        return view
    }

    fun addName(name: String) {
        itemsAdapter.notifyDataSetChanged()
    }
}
```

### Comunicació de Fragment a Activitat (Callback d'interfície)
El patró recomanat perquè un Fragment es comuniqui amb la seva Activitat és mitjançant una **interfície**:

```kotlin
// 1. Definir la interfície al Fragment
class M17_DetallFragmentActivity : Fragment() {
    interface OnNameAddedListener {
        fun onNameAdded(name: String)
    }

    private var callback: OnNameAddedListener? = null

    fun setOnNameAddedListener(callback: OnNameAddedListener) {
        this.callback = callback
    }

    // 2. Cridar la interfície quan es produeix l'esdeveniment
    button.setOnClickListener {
        callback?.onNameAdded(editText.text.toString())
    }
}

// 3. Implementar la interfície a l'Activitat
class M17_FragmentsActivity : AppCompatActivity(),
    M17_DetallFragmentActivity.OnNomAfegitListener {

    override fun onNomAfegit(name: String) {
        CURSOS.add(name)
        val listFragment = fragmentManager
            .findFragmentById(R.id.contenidorFragmentLlista) as? M17_LlistaFragmentActivity
        listFragment?.afegirNom(name)
    }
}
```

### FragmentManager i Transaccions

```kotlin
val transaction = fragmentManager.beginTransaction()

// Reemplaçar un fragment en un contenidor
transaction.replace(R.id.contenidorFragmentLlista, M17_LlistaFragmentActivity())

// Opcionalment afegir a la pila d'enrere (back stack)
transaction.addToBackStack(null)

// Confirmar la transacció
transaction.commit()
```

**Operacions de transacció:**
| Operació | Descripció |
|----------|------------|
| `add(container, fragment)` | Afegeix un fragment sense treure l'anterior |
| `replace(container, fragment)` | Reemplaça el fragment actual |
| `remove(fragment)` | Elimina un fragment |
| `hide(fragment)` / `show(fragment)` | Amaga/mostra sense destruir |

### Fragment intern (Classe anidada)

```kotlin
// Fragment definit com una classe anidada dins de l'Activitat
class FragmentInterna : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.m17_fragment_detall2, container, false)
}
```

### Canvis d'orientació
L'exercici respon als canvis d'orientació modificant dinàmicament el disseny:

```kotlin
override fun onConfigurationChanged(newConfig: Configuration) {
    super.onConfigurationChanged(newConfig)
    when (newConfig.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> ll.orientation = LinearLayout.HORIZONTAL
        Configuration.ORIENTATION_PORTRAIT  -> ll.orientation = LinearLayout.VERTICAL
        else -> { /* ORIENTATION_SQUARE / UNDEFINED: sense acció */ }
    }
}
```

Per habilitar-ho, cal afegir al Manifest:
```xml
<activity android:name=".M17_FragmentsActivity"
          android:configChanges="orientation|screenSize" />
```

## Flux de comunicació

```
[M17_DetallFragmentActivity]
    L'usuari prem "Afegir"
        |
        v
    callback.onNomAfegit(name)
        |
        v
[M17_FragmentsActivity] (implementa OnNomAfegitListener)
    onNomAfegit(name)
        |
        +-- CURSOS.add(name)
        +-- listFragment.addName(name)
                |
                v
        [M17_LlistaFragmentActivity]
            itemsAdapter.notifyDataSetChanged()
```

---
[M16](M16-Lectura-XML) | [Home](Home) | [M18](M18-Firebase)
