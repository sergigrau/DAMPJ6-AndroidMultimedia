# M09 – Propietats dinàmiques de vistes

## 🎯 Objectiu
Aprendre a crear i configurar vistes (`Button`, `LinearLayout`, etc.) de manera completament programàtica en temps d'execució, sense dependre del layout XML. Permet crear interfícies dinàmiques basades en dades o condicions.

## 📄 Fitxers implicats
| Fitxer | Descripció |
|--------|-----------|
| `M09_Propietats_dinamiques.kt` | Activity principal |
| `res/layout/m09_propietats_dinamiques.xml` | Layout base amb un contenidor `LinearLayout` |

## 🔑 Conceptes clau

### Creació programàtica de vistes

```kotlin
// Crear un botó dinàmicament
val boto = Button(this)
boto.text = "Sergi Grau"

// Crear un LinearLayout dinàmicament
val ll = LinearLayout(this)
ll.orientation = LinearLayout.VERTICAL
```

### LayoutParams – Definir dimensions i marges
`LayoutParams` defineix com una vista s'ha de col·locar dins del seu contenidor pare:

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
    ├── LinearLayout.LayoutParams  (afegeix gravity, weight)
    ├── RelativeLayout.LayoutParams (afegeix regles relatives)
    ├── ConstraintLayout.LayoutParams
    └── FrameLayout.LayoutParams
```

> ⚠️ Cal usar el tipus de `LayoutParams` del **pare**, no de la vista filla.

### Conversió dp → px
Els marges i dimensions sempre s'especifiquen en **dp** però els mètodes Android esperen **píxels**:

```kotlin
fun dpToPx(dp: Int): Int {
    return (dp * resources.displayMetrics.density).toInt()
}
```

### Afegir vistes al layout

```kotlin
val contingut: LinearLayout = findViewById(R.id.contingut)

// Afegir el LinearLayout al contenidor existent
contingut.addView(ll)

// Afegir el botó al nou LinearLayout amb LayoutParams propis
val btnLp = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
ll.addView(boto, btnLp)
```

### Gestió d'events en vistes dinàmiques
Igual que en XML, es poden afegir listeners programàticament:

```kotlin
boto.setOnClickListener {
    Toast.makeText(this, "Botó clicat: Sergi Grau", Toast.LENGTH_SHORT).show()
}
```

## 📊 Comparativa: XML vs Programàtic

| Aspecte | XML | Programàtic |
|---------|-----|-------------|
| Llegibilitat | ✅ Alta | ⚠️ Mitjana |
| Rendiment | Similar | Similar |
| Dinamisme | ❌ Estàtic | ✅ Dinàmic |
| Refactorització | ✅ Fàcil | ⚠️ Pot ser complex |
| Ús recomanat | Layouts fixes | Llistes, builders, vistes condicionals |

## 📌 Casos d'ús pràctics
- Generar formularis dinàmics basats en dades d'un servidor.
- Crear targetes (`CardView`) per cada element d'una llista.
- Afegir/eliminar files a una taula en temps real.
- Crear layouts adaptatius per a tauletes/mòbils.

---
[← M08](M08-Alarmes) | [Inici](Home) | [M10 →](M10-Animacio-Basica)

