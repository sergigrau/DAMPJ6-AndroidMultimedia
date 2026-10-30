# M04 – Notifications and SnackBar

## Objective
Learn how to show informational messages to the user with `Snackbar` (a non-intrusive notification at the bottom of the screen) and `Toast`, and understand when each one is appropriate.

## Files Involved
| File | Description |
|------|-------------|
| `M04_Notificacions_SnackBar.kt` | Main activity |
| `res/layout/m04_notificacions_snack_bar.xml` | Layout with CoordinatorLayout and FAB |

## Key Concepts

### Snackbar
`Snackbar` is a Material Design component that appears at the bottom of the screen for a limited time. Unlike `Toast`, it can include an **interactive action**:

```kotlin
Snackbar.make(view, "Playing...", Snackbar.LENGTH_LONG)
    .setAction("View") {
        Toast.makeText(applicationContext, "View more action", Toast.LENGTH_LONG).show()
    }
    .show()
```

**Available durations:**
| Constant | Duration |
|----------|----------|
| `LENGTH_SHORT` | ~2 seconds |
| `LENGTH_LONG` | ~3.5 seconds |
| `LENGTH_INDEFINITE` | Until dismissed by user or code |

### Toast
A simple pop-up message with no interaction:

```kotlin
Toast.makeText(context, "Message text", Toast.LENGTH_SHORT).show()
```

### Snackbar vs Toast Comparison
| Feature | Snackbar | Toast |
|---------|----------|-------|
| Position | Bottom (over UI) | Centre/bottom |
| Action | Yes | No |
| Dismissable | Yes (swipe) | No |
| Context needed | View | Context |
| Material Design | Yes | No |
| Recommended for | Action feedback | Simple messages |

### CoordinatorLayout
For the `Snackbar` to display correctly and the FAB to slide up when it appears, the root layout must be a `CoordinatorLayout`:

```xml
<androidx.coordinatorlayout.widget.CoordinatorLayout
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <com.google.android.material.floatingactionbutton.FloatingActionButton
        android:id="@+id/fab"
        ... />

</androidx.coordinatorlayout.widget.CoordinatorLayout>
```

### FloatingActionButton (FAB)
A circular floating button that triggers a primary action:

```kotlin
val fab: FloatingActionButton = findViewById(R.id.fab)
fab.setOnClickListener { view ->
    Snackbar.make(view, "Message", Snackbar.LENGTH_LONG).show()
}
```

## When to Use Each Element
- **Snackbar:** Action confirmation (file saved, email sent, item deleted with undo option).
- **Toast:** Quick informational messages that require no user response.
- **Dialog/AlertDialog:** When explicit user confirmation is required.
- **Notification:** For notifications that must persist outside the app.

---
[M03](M03-ExecutorService) | [Home](Home) | [M05](M05-Canvas-Basic)
