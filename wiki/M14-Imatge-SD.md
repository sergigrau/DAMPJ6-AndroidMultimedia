# M14 – Carregar imatge de la galeria

## Objectiu
Aprendre a seleccionar una imatge de la galeria del dispositiu utilitzant `ContentResolver` i `Uri`, seguint les bones pràctiques d'Android 13+ (API 33) que eviten l'accés directe a rutes de fitxers.

## Fitxers involucrats
| Fitxer | Descripció |
|--------|------------|
| `M14_ImatgeDeSDActivity.kt` | Activitat principal |
| `res/layout/m14_imatges_sd.xml` | Disseny amb ImageView |

## Conceptes clau

### ActivityResultLauncher per a la selecció d'imatges

```kotlin
private val imagePickerLauncher: ActivityResultLauncher<Intent> =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            val uri: Uri? = result.data!!.data

            // Obrir un InputStream des de l'Uri (no pas des d'una ruta de fitxer)
            val inputStream = uri?.let { contentResolver.openInputStream(it) }
            val bitmap = BitmapFactory.decodeStream(inputStream)
            imageView.setImageBitmap(bitmap)
        }
    }

override fun onCreate(savedInstanceState: Bundle?) {
    // ...
    val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
    imagePickerLauncher.launch(intent)
}
```

### Per què Uri en lloc d'una ruta de fitxer?
A partir d'Android 10 (API 29) i de forma estricta des de l'API 33, l'accés directe als fitxers externs (`/storage/emulated/0/...`) està **restringit**. S'ha d'utilitzar `ContentResolver` amb `Uri`:

```kotlin
// Obsolet / no funciona a partir de l'API 29+
val path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
val bitmap = BitmapFactory.decodeFile(path)

// Correcte (API 29+)
val inputStream = contentResolver.openInputStream(uri)
val bitmap = BitmapFactory.decodeStream(inputStream)
```

### Intent ACTION_PICK
Obre el selector de la galeria del sistema:

```kotlin
val intent = Intent(
    Intent.ACTION_PICK,
    MediaStore.Images.Media.EXTERNAL_CONTENT_URI  // Galeria d'imatges externa
)
```

**Alternatives:**
| Acció Intent | Descripció |
|--------------|------------|
| `ACTION_PICK` | Selector de galeria natiu |
| `ACTION_GET_CONTENT` | Selector de fitxers genèric |
| `ACTION_OPEN_DOCUMENT` | Accés persistent a documents |

### ContentResolver – Accés a Content Providers
`ContentResolver` és la porta d'accés a les instàncies de `ContentProvider` del sistema (galeria, contactes, calendari...):

```kotlin
// Llegir una imatge
val inputStream = contentResolver.openInputStream(uri)

// Llegir metadades
val cursor = contentResolver.query(uri, null, null, null, null)

// Esborrar
contentResolver.delete(uri, null, null)
```

### BitmapFactory
Decodifica imatges des de diverses fonts:

```kotlin
// Des de InputStream (recomanat per a Uri)
BitmapFactory.decodeStream(inputStream)

// Des de fitxer (obsolet per a fitxers externs)
BitmapFactory.decodeFile(path)

// Des de recursos
BitmapFactory.decodeResource(resources, R.drawable.image)

// Amb opcions per reduir la mida (evita OOM)
val options = BitmapFactory.Options().apply {
    inSampleSize = 4  // Llegeix 1 de cada 4 píxels
}
BitmapFactory.decodeStream(inputStream, null, options)
```

## Permisos requerits (API 33+)
```xml
<!-- Per a Android 13+ (API 33) -->
<uses-permission android:name="android.permission.READ_MEDIA_IMAGES" />

<!-- Per a Android 12 i inferiors -->
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"
    android:maxSdkVersion="32" />
```

## Bones pràctiques
- Tancar sempre l'`InputStream` després d'utilitzar-lo (utilitzant `use { }` en Kotlin).
- Per a imatges grans, utilitzar `BitmapFactory.Options.inSampleSize` per reduir la resolució i evitar `OutOfMemoryError`.
- No decodificar el Bitmap al fil principal si la imatge pot ser gran.

---
[M13](M13-Camera-API) | [Home](Home) | [M15](M15-OpenGL-ES-20)
