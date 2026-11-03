# M08 – Alarmes amb AlarmManager

## Objectiu
Aprendre a programar accions temporitzades amb `AlarmManager`: tant alarmes d'un sol tret com alarmes periòdiques. Quan salt l'alarma, el sistema llança un `BroadcastReceiver` que fa vibrar el dispositiu i reprodueix un so.

## Fitxers Involucrats
| Fitxer | Descripció |
|--------|------------|
| `M08_AlarmaActivity.kt` | Activity amb camps per programar alarmes |
| `M08_AlarmaReceiver.kt` | BroadcastReceiver executat quan salt l'alarma |
| `res/layout/m08_alarma.xml` | Layout amb EditText i botons |

## Conceptes Clau

### AlarmManager
`AlarmManager` és un servei del sistema que permet programar codi perquè s'executi en un moment futur, fins i tot si l'aplicació no està activa:

```kotlin
val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
```

### Alarma d'un sol tret (One-Shot)
```kotlin
fun iniciarAlarma(view: View) {
    val seconds = Integer.parseInt(editText.text.toString())

    val intent = Intent(this, M08_AlarmaReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        applicationContext,
        234324243,         // ID únic de l'alarma
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    alarmManager.set(
        AlarmManager.RTC_WAKEUP,
        System.currentTimeMillis() + (seconds * 1000L),  // Temps en ms
        pendingIntent
    )
}
```

### Alarma Periòdica
```kotlin
alarmManager.setRepeating(
    AlarmManager.RTC_WAKEUP,
    System.currentTimeMillis(),    // Primera execució
    1000L,                          // Interval en ms (1 segon)
    pendingIntent
)
```

### Tipus de Temporitzadors
| Constant | Descripció |
|----------|------------|
| `RTC` | Hora real del rellotge, no desperta el dispositiu |
| `RTC_WAKEUP` | Hora real del rellotge, **desperta el dispositiu** |
| `ELAPSED_REALTIME` | Temps des de l'inici, no desperta |
| `ELAPSED_REALTIME_WAKEUP` | Temps des de l'inici, **desperta el dispositiu** |

### PendingIntent – FLAG_IMMUTABLE (API 31+)
Des d'Android 12 (API 31), és **obligatori** especificar `FLAG_IMMUTABLE` o `FLAG_MUTABLE`:

```kotlin
PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
```

| Flag | Descripció |
|------|------------|
| `FLAG_UPDATE_CURRENT` | Actualitza els extres si ja existia |
| `FLAG_IMMUTABLE` | El PendingIntent no es pot modificar |
| `FLAG_CANCEL_CURRENT` | Cancel·la l'anterior i en crea un de nou |

### BroadcastReceiver – M08_AlarmaReceiver

```kotlin
class M08_AlarmaReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Vibració
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        val vibrator = vibratorManager.defaultVibrator
        val effect = VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE)
        vibrator.vibrate(effect)

        // So d'alarma
        val alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val ringtone = RingtoneManager.getRingtone(context, alarmUri)
        ringtone?.play()
    }
}
```

### VibratorManager (API 31+)
El `Vibrator` directe va ser deprecat a l'API 31. S'ha d'utilitzar `VibratorManager`:

```kotlin
// Deprecat (fins a l'API 30)
val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

// Nou (API 31+)
val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
val vibrator = vibratorManager.defaultVibrator
```

### Declaració al Manifest
```xml
<receiver android:name=".M08_AlarmaReceiver"
          android:exported="false" />

<uses-permission android:name="android.permission.VIBRATE" />
```

## Limitacions d'AlarmManager
- Des de l'**API 31 (Android 12)**, les alarmes exactes requereixen el permís `SCHEDULE_EXACT_ALARM`.
- Les alarmes **no es restablesquen** després d'un reinici del dispositiu (cal escoltar `BOOT_COMPLETED`).
- `setRepeating()` amb un interval < 1 minut pot ser ignorat pel sistema (`@SuppressLint("ShortAlarm")`).

---
[M07](M07-Canvas-Path) | [Home](Home) | [M09](M09-Propietats-Dinamiques)
