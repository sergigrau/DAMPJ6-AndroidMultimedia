# M09 – Propietats de Vistes Dinàmiques

## Objectiu
Aprendre a crear i configurar vistes (`Button`, `LinearLayout`, etc.) completament per codi en temps d'execució, sense dependre d'un layout XML. Això permet construir interfícies dinàmiques basades en dades o condicions.

## Fitxers Involucrats
| Fitxer | Descripció |
|--------|------------|
| `M09_Propietats_dinamiques.kt` | Activity principal |
| `res/layout/m09_propietats_dinamiques.xml` | Layout base amb un contenidor `LinearLayout` |

## Conceptes Clau

### Creació de Vistes per Codi

```kotlin
// Crea un botó dinàmicament
val button = Button(this)
button.text = "Sergi Grau"

// Crea un LinearLayout dinàmicament
val ll = LinearLayout(this)
ll.orientation = LinearLayout.VERTICAL
```

### LayoutParams – Definició de Mides i Marges
`LayoutParams` defineix com s'ha de col·locar una vista dins del seu contenidor pare:

```kotlin
val lp = LinearLayout.LayoutParams(
    LinearLayout.LayoutParams.MATCH_PARENT,  // amplada
    LinearLayout.LayoutParams.WRAP_CONTENT   // alçada
)

// Marges en píxels (convertir de dp a px)
val margin = (16 * resources.displayMetrics.density).toInt()
lp.setMargins(margin, margin, margin, margin)

ll.layoutParams = lp
```

### Jerarquia de `ViewGroup.LayoutParams`
```
ViewGroup.LayoutParams
    +-- LinearLayout.LayoutParams  (afegeix gravetat, pes)
    +-- RelativeLayout.LayoutParams (afegeix regles relatives)
    +-- ConstraintLayout.LayoutParams
    +-- FrameLayout.LayoutParams
```

> Utilitzeu sempre el tipus de `LayoutParams` del **pare**, no de la vista fill.

### Conversió de dp a px
Els marges i dimensions s'especifiquen sempre en **dp** però els mètodes d'Android esperen **píxels**:

```kotlin
fun dpToPx(dp: Int): Int {
    return (dp * resources.displayMetrics.density).toInt()
}
```

### Afegir Vistes a un Layout

```kotlin
val container: LinearLayout = findViewById(R.id.contingut)

// Afegeix el LinearLayout al contenidor existent
container.addView(ll)

// Afegeix el botó al nou LinearLayout amb els seus propis LayoutParams
val btnLp = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
ll.addView(button, btnLp)
```

### Gestió d'Esdeveniments en Vistes Dinàmiques
Es poden afegir listeners per codi, exactament igual que amb les vistes XML:

```kotlin
button.setOnClickListener {
    Toast.makeText(this, "Botó clicat: Sergi Grau", Toast.LENGTH_SHORT).show()
}
```

## Comparació XML vs Codi

| Aspecte | XML | Codi |
|---------|-----|------|
| Llegibilitat | Alta | Mitjana |
| Rendiment | Similar | Similar |
| Dinamisme | Estàtic | Dinàmic |
| Refactorització | Fàcil | Pot ser complex |
| Recomanat per | Layouts fixos | Llistes, constructors, vistes condicionals |

## Casos d'Ús Pràctics
- Generar formularis dinàmics basats en dades del servidor.
- Crear targetes (`CardView`) per a cada element d'una llista.
- Afegir/eliminar files d'una taula en temps real.
- Crear layouts adaptatius per a tauletes/mòbils.

---
[M08](M08-Alarmes) | [Home](Home) | [M10](M10-Animacio-Basica)
