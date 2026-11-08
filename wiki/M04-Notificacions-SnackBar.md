# M04 – Notificacions amb NotificationCompat i SnackBar

## Objectiu
Aprendre a mostrar notificacions del sistema utilitzant **`NotificationCompat`** i **`NotificationManagerCompat`** de Jetpack Core KTX, conjuntament amb el component `Snackbar` de Material Design 3.

## Fitxers Involucrats
| Fitxer | Descripció |
|--------|------------|
| `M04_Notificacions_SnackBar.kt` | Activity principal amb integració de notificacions Jetpack |

## Conceptes Clau

### NotificationCompat & NotificationManagerCompat
`NotificationCompat.Builder` garanteix la compatibilitat de les notificacions en diferents versions d'Android:

```kotlin
val builder = NotificationCompat.Builder(this, CHANNEL_ID)
    .setSmallIcon(android.R.drawable.ic_dialog_info)
    .setContentTitle("Notificació Jetpack Core")
    .setContentText("Aquesta notificació ha estat creada amb NotificationCompat.")
    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    .setAutoCancel(true)

val notificationManager = NotificationManagerCompat.from(this)
notificationManager.notify(1001, builder.build())
```

---
[M03](M03-ExecutorService) | [Home](Home) | [M05](M05-Canvas-Basic)
