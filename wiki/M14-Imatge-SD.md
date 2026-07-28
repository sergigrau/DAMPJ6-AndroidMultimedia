# M14 – Carregar imatge de la galeria

## 🎯 Objectiu
Aprendre a seleccionar una imatge de la galeria del dispositiu usant `ContentResolver` i `Uri`, seguint les bones pràctiques d'Android 13+ (API 33) que eviten l'accés directe a rutes de fitxers del sistema.

## 📄 Fitxers implicats
| Fitxer | Descripció |
|--------|-----------|
| `M14_ImatgeDeSDActivity.kt` | Activity principal |
| `res/layout/m14_imatges_sd.xml` | Layout amb ImageView |

## 🔑 Conceptes clau

### ActivityResultLauncher per seleccionar imatge

```kotlin
private val seleccioImatgeLauncher: ActivityResultLauncher<Intent> =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            val seleccio: Uri? = result.data!!.data

            // Obrir un InputStream a partir de l'Uri (no de la ruta de fitxer)
            val inputStream = seleccio?.let { contentResolver.openInputStream(it) }
            val imatge = BitmapFactory.decodeStream(inputStream)
            imageView.setImageBitmap(imatge)
        }
    }

override fun onCreate(savedInstanceState: Bundle?) {
    // ...
    val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
    seleccioImatgeLauncher.launch(intent)
}
```

### Per què Uri en lloc de ruta de fitxer?
A partir d'Android 10 (API 29) i consolidat a API 33, l'accés directe a fitxers externs (`/storage/emulated/0/...`) va ser **restringit**. Cal usar `ContentResolver` amb `Uri`:

```kotlin
// ❌ Deprecated / no funciona a API 29+
val path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
val bitmap = BitmapFactory.decodeFile(path)

// ✅ Correcte (API 29+)
val inputStream = contentResolver.openInputStream(uri)
val bitmap = BitmapFactory.decodeStream(inputStream)
```

### Intent ACTION_PICK
Obre el selector de la galeria del sistema:

```kotlin
val intent = Intent(
    Intent.ACTION_PICK,
    MediaStore.Images.Media.EXTERNAL_CONTENT_URI  // Galeria d'imatges externes
)
```

**Alternatives:**
| Intent Action | Descripció |
|--------------|-----------|
| `ACTION_PICK` | Selector de galeria natiu |
| `ACTION_GET_CONTENT` | Selector genèric de fitxers |
| `ACTION_OPEN_DOCUMENT` | Accés persistent a documents |

### ContentResolver – Accés als proveïdors de contingut
`ContentResolver` és la porta d'accés als `ContentProvider` del sistema (galeria, contactes, calendari...):

```kotlin
// Llegir una imatge
val inputStream = contentResolver.openInputStream(uri)

// Llegir metadades
val cursor = contentResolver.query(uri, null, null, null, null)

// Eliminar
contentResolver.delete(uri, null, null)
```

### BitmapFactory
Decodifica imatges de diverses fonts:

```kotlin
// Des de InputStream (recomanat per Uri)
BitmapFactory.decodeStream(inputStream)

// Des de fitxer (deprecated per a fitxers externs)
BitmapFactory.decodeFile(path)

// Des de recursos
BitmapFactory.decodeResource(resources, R.drawable.imatge)

// Amb opcions per reduir la mida (evitar OOM)
val options = BitmapFactory.Options().apply {
    inSampleSize = 4  // Llegeix 1 de cada 4 píxels
}
BitmapFactory.decodeStream(inputStream, null, options)
```

## ⚠️ Permisos necessaris (API 33+)
```xml
<!-- Per a Android 13+ (API 33) -->
<uses-permission android:name="android.permission.READ_MEDIA_IMAGES" />

<!-- Per a Android 12 i inferior -->
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"
    android:maxSdkVersion="32" />
```

## 📌 Bones pràctiques
- Sempre tancar l'`InputStream` després d'usar-lo (usant `use { }` a Kotlin).
- Per imatges molt grans, usar `BitmapFactory.Options.inSampleSize` per reduir la resolució i evitar `OutOfMemoryError`.
- No fer la decodificació del Bitmap al fil principal si la imatge pot ser gran.

---
[← M13](M13-Camera-API) | [Inici](Home) | [M15 →](M15-OpenGL-ES-20)

