# M17 – Fragments amb Jetpack Fragment KTX

## Objectiu
Aprendre a utilitzar **Fragments** per crear interfícies modulars de llista-detall que s'adapten a diferents orientacions i mides de pantalla, utilitzant la sintaxi moderna de **Jetpack Fragment KTX** (`supportFragmentManager.commit {}`).

## Fitxers Involucrats
| Fitxer | Descripció |
|--------|------------|
| `M17_FragmentsActivity.kt` | Activity principal amb Fragment KTX |
| `M17_LlistaFragmentActivity.kt` | Fragment de llista |
| `M17_DetallFragmentActivity.kt` | Fragment de detall |

## Conceptes Clau

### Fragment Transactions amb Fragment KTX
S'elimina la necessitat de cridar `beginTransaction()` i `commit()` manualment gràcies al bloc d'extensió `commit {}`:

```kotlin
supportFragmentManager.commit {
    replace(R.id.contenidorFragmentLlista, M17_LlistaFragmentActivity())
}
```

---
[M16](M16-Lectura-XML) | [Home](Home) | [M18](M18-Firebase)
