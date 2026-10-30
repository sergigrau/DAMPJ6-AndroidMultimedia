# M14 – Load Image from Gallery

## Objective
Learn how to select an image from the device gallery using `ContentResolver` and `Uri`, following Android 13+ (API 33) best practices that avoid direct access to file system paths.

## Files Involved
| File | Description |
|------|-------------|
| `M14_ImatgeDeSDActivity.kt` | Main activity |
| `res/layout/m14_imatges_sd.xml` | Layout with ImageView |

## Key Concepts

### ActivityResultLauncher for Image Selection

```kotlin
private val imagePickerLauncher: ActivityResultLauncher<Intent> =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            val uri: Uri? = result.data!!.data

            // Open an InputStream from the Uri (not from a file path)
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

### Why Uri Instead of a File Path?
From Android 10 (API 29) onwards, and fully enforced in API 33, direct access to external files (`/storage/emulated/0/...`) was **restricted**. `ContentResolver` with `Uri` must be used instead:

```kotlin
// Deprecated / does not work from API 29+
val path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
val bitmap = BitmapFactory.decodeFile(path)

// Correct (API 29+)
val inputStream = contentResolver.openInputStream(uri)
val bitmap = BitmapFactory.decodeStream(inputStream)
```

### ACTION_PICK Intent
Opens the system gallery selector:

```kotlin
val intent = Intent(
    Intent.ACTION_PICK,
    MediaStore.Images.Media.EXTERNAL_CONTENT_URI  // External image gallery
)
```

**Alternatives:**
| Intent Action | Description |
|--------------|-------------|
| `ACTION_PICK` | Native gallery selector |
| `ACTION_GET_CONTENT` | Generic file selector |
| `ACTION_OPEN_DOCUMENT` | Persistent document access |

### ContentResolver – Access to Content Providers
`ContentResolver` is the gateway to system `ContentProvider` instances (gallery, contacts, calendar...):

```kotlin
// Read an image
val inputStream = contentResolver.openInputStream(uri)

// Read metadata
val cursor = contentResolver.query(uri, null, null, null, null)

// Delete
contentResolver.delete(uri, null, null)
```

### BitmapFactory
Decodes images from various sources:

```kotlin
// From InputStream (recommended for Uri)
BitmapFactory.decodeStream(inputStream)

// From file (deprecated for external files)
BitmapFactory.decodeFile(path)

// From resources
BitmapFactory.decodeResource(resources, R.drawable.image)

// With options to reduce size (avoid OOM)
val options = BitmapFactory.Options().apply {
    inSampleSize = 4  // Reads 1 out of every 4 pixels
}
BitmapFactory.decodeStream(inputStream, null, options)
```

## Required Permissions (API 33+)
```xml
<!-- For Android 13+ (API 33) -->
<uses-permission android:name="android.permission.READ_MEDIA_IMAGES" />

<!-- For Android 12 and below -->
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"
    android:maxSdkVersion="32" />
```

## Best Practices
- Always close the `InputStream` after use (using `use { }` in Kotlin).
- For large images, use `BitmapFactory.Options.inSampleSize` to reduce resolution and avoid `OutOfMemoryError`.
- Do not decode the Bitmap on the main thread if the image can be large.

---
[M13](M13-Camera-API) | [Home](Home) | [M15](M15-OpenGL-ES-20)
