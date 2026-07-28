# M17 – Fragments

## 🎯 Objectiu
Aprendre a usar **Fragments** per crear interfícies modulars que s'adapten a diferents mides de pantalla (tauletes vs mòbils). Implementa el patró **llista-detall**: un Fragment mostra una llista de cursos i l'altre permet afegir-ne de nous.

## 📄 Fitxers implicats
| Fitxer | Descripció |
|--------|-----------|
| `M17_FragmentsActivity.kt` | Activity principal que gestiona els Fragments |
| `M17_LlistaFragmentActivity.kt` | Fragment amb ListView de cursos |
| `M17_DetallFragmentActivity.kt` | Fragment amb formulari per afegir cursos |
| `res/layout/m17_fragments.xml` | Layout principal amb dos contenidors |
| `res/layout/m17_fragment_llista.xml` | Layout del Fragment de llista |
| `res/layout/m17_fragment_detall.xml` | Layout del Fragment de detall |
| `res/menu/m17_fragments.xml` | Menú per canviar fragments |

## 🔑 Conceptes clau

### Què és un Fragment?
Un **Fragment** és un component reutilitzable amb la seva pròpia UI i cicle de vida, que sempre viu dins d'una Activity. Permet:
- Composar interfícies complexes combinant múltiples fragments.
- Reutilitzar el mateix fragment en diverses Activities.
- Adaptar la UI a tauletes (multi-panell) i mòbils (una panell).

### Estructura del Fragment

```kotlin
class M17_LlistaFragmentActivity : Fragment() {

    private lateinit var llista: ListView
    private lateinit var itemsAdapter: ArrayAdapter<String>

    // Equivalent a onCreate() per a Fragments
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.m17_fragment_llista, container, false)
        llista = view.findViewById(R.id.llista)
        itemsAdapter = ArrayAdapter(
            view.context,
            android.R.layout.simple_list_item_1,
            M17_FragmentsActivity.CURSOS
        )
        llista.adapter = itemsAdapter
        return view
    }

    fun afegirNom(nom: String) {
        itemsAdapter.notifyDataSetChanged()
    }
}
```

### Comunicació Fragment → Activity (Interface Callback)
El patró recomanat per comunicar un Fragment amb la seva Activity és a través d'una **interfície**:

```kotlin
// 1. Definir la interfície al Fragment
class M17_DetallFragmentActivity : Fragment() {
    interface OnNomAfegitListener {
        fun onNomAfegit(nom: String)
    }

    private var callback: OnNomAfegitListener? = null

    fun setOnNomAfegitListener(callback: OnNomAfegitListener) {
        this.callback = callback
    }

    // 2. Cridar la interfície quan passi l'event
    botoEntrada.setOnClickListener {
        callback?.onNomAfegit(textEntrada.text.toString())
    }
}

// 3. Implementar la interfície a l'Activity
class M17_FragmentsActivity : AppCompatActivity(),
    M17_DetallFragmentActivity.OnNomAfegitListener {

    override fun onNomAfegit(nom: String) {
        CURSOS.add(nom)
        val llistaFragment = fragmentManager
            .findFragmentById(R.id.contenidorFragmentLlista) as? M17_LlistaFragmentActivity
        llistaFragment?.afegirNom(nom)
    }
}
```

### FragmentManager i transaccions

```kotlin
val fragmentManager = fragmentManager  // Referència al gestor
val transaction = fragmentManager.beginTransaction()

// Substituir un fragment en un contenidor
transaction.replace(R.id.contenidorFragmentLlista, M17_LlistaFragmentActivity())

// Opcionalment afegir a la pila de retrocés
transaction.addToBackStack(null)

// Confirmar la transacció
transaction.commit()
```

**Operacions de transacció:**
| Operació | Descripció |
|----------|-----------|
| `add(container, fragment)` | Afegeix un fragment sense eliminar l'anterior |
| `replace(container, fragment)` | Substitueix el fragment actual |
| `remove(fragment)` | Elimina un fragment |
| `hide(fragment)` / `show(fragment)` | Amaga/mostra sense destruir |

### Fragment intern (classe anidada)

```kotlin
// Fragment definit com a classe anidada a l'Activity
class FragmentInterna : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.m17_fragment_detall2, container, false)
}
```

### Canvis d'orientació
L'exercici respon al canvi d'orientació modificant el layout dinàmicament:

```kotlin
override fun onConfigurationChanged(novaConfiguracio: Configuration) {
    super.onConfigurationChanged(novaConfiguracio)
    when (novaConfiguracio.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            ll.orientation = LinearLayout.HORIZONTAL
        }
        Configuration.ORIENTATION_PORTRAIT -> {
            ll.orientation = LinearLayout.VERTICAL
        }
    }
}
```

Per activar-ho, cal afegir al Manifest:
```xml
<activity android:name=".M17_FragmentsActivity"
          android:configChanges="orientation|screenSize" />
```

## 🔄 Flux de comunicació

```
[M17_DetallFragmentActivity]
    Usuari prem "Afegir"
        │
        ▼
    callback.onNomAfegit(nom)
        │
        ▼
[M17_FragmentsActivity] (implementa OnNomAfegitListener)
    onNomAfegit(nom)
        │
        ├─► CURSOS.add(nom)
        └─► llistaFragment.afegirNom(nom)
                │
                ▼
        [M17_LlistaFragmentActivity]
            itemsAdapter.notifyDataSetChanged()
```

---
[← M16](M16-Lectura-XML) | [Inici](Home) | [M18 →](M18-Firebase)

