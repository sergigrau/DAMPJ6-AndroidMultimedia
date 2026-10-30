# M09 – Dynamic View Properties

## Objective
Learn how to create and configure views (`Button`, `LinearLayout`, etc.) entirely programmatically at runtime, without relying on an XML layout. This enables building dynamic interfaces based on data or conditions.

## Files Involved
| File | Description |
|------|-------------|
| `M09_Propietats_dinamiques.kt` | Main activity |
| `res/layout/m09_propietats_dinamiques.xml` | Base layout with a `LinearLayout` container |

## Key Concepts

### Programmatic View Creation

```kotlin
// Create a button dynamically
val button = Button(this)
button.text = "Sergi Grau"

// Create a LinearLayout dynamically
val ll = LinearLayout(this)
ll.orientation = LinearLayout.VERTICAL
```

### LayoutParams – Defining Dimensions and Margins
`LayoutParams` defines how a view should be placed inside its parent container:

```kotlin
val lp = LinearLayout.LayoutParams(
    LinearLayout.LayoutParams.MATCH_PARENT,  // width
    LinearLayout.LayoutParams.WRAP_CONTENT   // height
)

// Margins in pixels (convert from dp to px)
val margin = (16 * resources.displayMetrics.density).toInt()
lp.setMargins(margin, margin, margin, margin)

ll.layoutParams = lp
```

### `ViewGroup.LayoutParams` Hierarchy
```
ViewGroup.LayoutParams
    +-- LinearLayout.LayoutParams  (adds gravity, weight)
    +-- RelativeLayout.LayoutParams (adds relative rules)
    +-- ConstraintLayout.LayoutParams
    +-- FrameLayout.LayoutParams
```

> Always use the `LayoutParams` type of the **parent**, not of the child view.

### dp to px Conversion
Margins and dimensions are always specified in **dp** but Android methods expect **pixels**:

```kotlin
fun dpToPx(dp: Int): Int {
    return (dp * resources.displayMetrics.density).toInt()
}
```

### Adding Views to a Layout

```kotlin
val container: LinearLayout = findViewById(R.id.contingut)

// Add the LinearLayout to the existing container
container.addView(ll)

// Add the button to the new LinearLayout with its own LayoutParams
val btnLp = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
ll.addView(button, btnLp)
```

### Event Handling on Dynamic Views
Listeners can be added programmatically, just as with XML views:

```kotlin
button.setOnClickListener {
    Toast.makeText(this, "Button clicked: Sergi Grau", Toast.LENGTH_SHORT).show()
}
```

## XML vs Programmatic Comparison

| Aspect | XML | Programmatic |
|--------|-----|--------------|
| Readability | High | Medium |
| Performance | Similar | Similar |
| Dynamic | Static | Dynamic |
| Refactoring | Easy | Can be complex |
| Recommended for | Fixed layouts | Lists, builders, conditional views |

## Practical Use Cases
- Generating dynamic forms based on server data.
- Creating cards (`CardView`) for each item in a list.
- Adding/removing rows from a table in real time.
- Creating adaptive layouts for tablets/phones.

---
[M08](M08-Alarmes) | [Home](Home) | [M10](M10-Animacio-Basica)
