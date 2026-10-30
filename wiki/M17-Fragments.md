# M17 – Fragments

## Objective
Learn how to use **Fragments** to create modular interfaces that adapt to different screen sizes (tablets vs phones). Implements the **list-detail** pattern: one Fragment shows a list of courses and the other allows adding new ones.

## Files Involved
| File | Description |
|------|-------------|
| `M17_FragmentsActivity.kt` | Main activity that manages the Fragments |
| `M17_LlistaFragmentActivity.kt` | Fragment with a ListView of courses |
| `M17_DetallFragmentActivity.kt` | Fragment with a form to add courses |
| `res/layout/m17_fragments.xml` | Main layout with two containers |
| `res/layout/m17_fragment_llista.xml` | List Fragment layout |
| `res/layout/m17_fragment_detall.xml` | Detail Fragment layout |
| `res/menu/m17_fragments.xml` | Menu to switch fragments |

## Key Concepts

### What is a Fragment?
A **Fragment** is a reusable component with its own UI and lifecycle that always lives inside an Activity. It enables:
- Composing complex interfaces by combining multiple fragments.
- Reusing the same fragment in multiple Activities.
- Adapting the UI to tablets (multi-pane) and phones (single-pane).

### Fragment Structure

```kotlin
class M17_LlistaFragmentActivity : Fragment() {

    private lateinit var listView: ListView
    private lateinit var itemsAdapter: ArrayAdapter<String>

    // Equivalent to onCreate() for Fragments
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

### Fragment to Activity Communication (Interface Callback)
The recommended pattern for a Fragment to communicate with its Activity is through an **interface**:

```kotlin
// 1. Define the interface in the Fragment
class M17_DetallFragmentActivity : Fragment() {
    interface OnNameAddedListener {
        fun onNameAdded(name: String)
    }

    private var callback: OnNameAddedListener? = null

    fun setOnNameAddedListener(callback: OnNameAddedListener) {
        this.callback = callback
    }

    // 2. Call the interface when the event occurs
    button.setOnClickListener {
        callback?.onNameAdded(editText.text.toString())
    }
}

// 3. Implement the interface in the Activity
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

### FragmentManager and Transactions

```kotlin
val transaction = fragmentManager.beginTransaction()

// Replace a fragment in a container
transaction.replace(R.id.contenidorFragmentLlista, M17_LlistaFragmentActivity())

// Optionally add to the back stack
transaction.addToBackStack(null)

// Commit the transaction
transaction.commit()
```

**Transaction operations:**
| Operation | Description |
|-----------|-------------|
| `add(container, fragment)` | Adds a fragment without removing the previous one |
| `replace(container, fragment)` | Replaces the current fragment |
| `remove(fragment)` | Removes a fragment |
| `hide(fragment)` / `show(fragment)` | Hides/shows without destroying |

### Inner Fragment (Nested Class)

```kotlin
// Fragment defined as a nested class inside the Activity
class FragmentInterna : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.m17_fragment_detall2, container, false)
}
```

### Orientation Changes
The exercise responds to orientation changes by dynamically modifying the layout:

```kotlin
override fun onConfigurationChanged(newConfig: Configuration) {
    super.onConfigurationChanged(newConfig)
    when (newConfig.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> ll.orientation = LinearLayout.HORIZONTAL
        Configuration.ORIENTATION_PORTRAIT  -> ll.orientation = LinearLayout.VERTICAL
        else -> { /* ORIENTATION_SQUARE / UNDEFINED: no action */ }
    }
}
```

To enable this, add to the Manifest:
```xml
<activity android:name=".M17_FragmentsActivity"
          android:configChanges="orientation|screenSize" />
```

## Communication Flow

```
[M17_DetallFragmentActivity]
    User presses "Add"
        |
        v
    callback.onNomAfegit(name)
        |
        v
[M17_FragmentsActivity] (implements OnNomAfegitListener)
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
