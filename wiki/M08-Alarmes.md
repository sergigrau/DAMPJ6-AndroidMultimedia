# M08 – Alarms with AlarmManager

## Objective
Learn how to schedule timed actions with `AlarmManager`: both one-shot alarms and repeating alarms. When the alarm fires, the system launches a `BroadcastReceiver` that vibrates the device and plays a sound.

## Files Involved
| File | Description |
|------|-------------|
| `M08_AlarmaActivity.kt` | Activity with fields to schedule alarms |
| `M08_AlarmaReceiver.kt` | BroadcastReceiver executed when the alarm fires |
| `res/layout/m08_alarma.xml` | Layout with EditText and buttons |

## Key Concepts

### AlarmManager
`AlarmManager` is a system service that allows scheduling code to run at a future time, even if the application is not active:

```kotlin
val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
```

### One-Shot Alarm
```kotlin
fun iniciarAlarma(view: View) {
    val seconds = Integer.parseInt(editText.text.toString())

    val intent = Intent(this, M08_AlarmaReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        applicationContext,
        234324243,         // Unique alarm ID
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    alarmManager.set(
        AlarmManager.RTC_WAKEUP,
        System.currentTimeMillis() + (seconds * 1000L),  // Time in ms
        pendingIntent
    )
}
```

### Repeating Alarm
```kotlin
alarmManager.setRepeating(
    AlarmManager.RTC_WAKEUP,
    System.currentTimeMillis(),    // First trigger
    1000L,                          // Interval in ms (1 second)
    pendingIntent
)
```

### Timer Types
| Constant | Description |
|----------|-------------|
| `RTC` | Real clock time, does not wake the device |
| `RTC_WAKEUP` | Real clock time, **wakes the device** |
| `ELAPSED_REALTIME` | Time since boot, does not wake |
| `ELAPSED_REALTIME_WAKEUP` | Time since boot, **wakes the device** |

### PendingIntent – FLAG_IMMUTABLE (API 31+)
Since Android 12 (API 31), it is **mandatory** to specify `FLAG_IMMUTABLE` or `FLAG_MUTABLE`:

```kotlin
PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
```

| Flag | Description |
|------|-------------|
| `FLAG_UPDATE_CURRENT` | Updates extras if it already existed |
| `FLAG_IMMUTABLE` | The PendingIntent cannot be modified |
| `FLAG_CANCEL_CURRENT` | Cancels the previous one and creates a new one |

### BroadcastReceiver – M08_AlarmaReceiver

```kotlin
class M08_AlarmaReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Vibrate
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        val vibrator = vibratorManager.defaultVibrator
        val effect = VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE)
        vibrator.vibrate(effect)

        // Play alarm sound
        val alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val ringtone = RingtoneManager.getRingtone(context, alarmUri)
        ringtone?.play()
    }
}
```

### VibratorManager (API 31+)
The direct `Vibrator` was deprecated in API 31. `VibratorManager` must be used instead:

```kotlin
// Deprecated (up to API 30)
val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

// New (API 31+)
val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
val vibrator = vibratorManager.defaultVibrator
```

### Manifest Declaration
```xml
<receiver android:name=".M08_AlarmaReceiver"
          android:exported="false" />

<uses-permission android:name="android.permission.VIBRATE" />
```

## AlarmManager Limitations
- From **API 31 (Android 12)**, exact alarms require the `SCHEDULE_EXACT_ALARM` permission.
- Alarms are **not restored** after a device reboot (you need to listen for `BOOT_COMPLETED`).
- `setRepeating()` with an interval < 1 minute may be ignored by the system (`@SuppressLint("ShortAlarm")`).

---
[M07](M07-Canvas-Path) | [Home](Home) | [M09](M09-Propietats-Dinamiques)
