# M03 – Background Tasks with ExecutorService

## Objective
Learn how to run heavy tasks on a secondary thread without blocking the user interface, using `ExecutorService` (the modern replacement for the `AsyncTask` class deprecated in API 30). Combined with `ObjectAnimator` and `AnimatorSet` animations.

## Files Involved
| File | Description |
|------|-------------|
| `M03_ExecutorServiceActivity.kt` | Main activity |
| `res/layout/m03_asyntask.xml` | Layout with Button and ListView |

## Key Concepts

### Why ExecutorService?
`AsyncTask` was deprecated in **Android 11 (API 30)** due to memory leak issues and inconsistent behaviour on configuration changes. `ExecutorService` is the recommended alternative:

```kotlin
// Create a single-thread executor
private val executor: ExecutorService = Executors.newSingleThreadExecutor()

// Execute a task on a background thread
executor.execute {
    // Code that runs on a secondary thread
    val result = performExpensiveCalculation()

    // Update the UI -> always on the main thread
    runOnUiThread {
        valueList.add(result)
        adapter.notifyDataSetChanged()
    }
}
```

### Golden rule: UI always on the main thread
Android does not allow modifying the UI from secondary threads. Any visual update must be done with `runOnUiThread { }`:

```kotlin
executor.execute {
    val result = counter++.toString()   // secondary thread
    runOnUiThread {
        values.add(result)              // main thread
        adapter.notifyDataSetChanged()  // main thread
    }
}
```

### WindowMetrics (API 30+)
To get screen dimensions without the deprecated `getDefaultDisplay()` method:

```kotlin
val bounds: Rect = windowManager.currentWindowMetrics.bounds
val width  = bounds.width()
val height = bounds.height()
```

### AnimatorSet – Sequential button animation
```kotlin
val anim1 = ObjectAnimator.ofFloat(button, "x", 0f, width.toFloat())
val anim2 = ObjectAnimator.ofFloat(button, "y", 0f, height.toFloat())
// ...

val animSet = AnimatorSet()
animSet.playSequentially(anim1, anim2, anim3, anim4)
animSet.addListener(object : AnimatorListenerAdapter() {
    override fun onAnimationEnd(a: Animator) {
        runTask()      // New task when the animation ends
        animSet.start() // Restart the animation in a loop
    }
})
animSet.start()
```

### Proper executor shutdown
```kotlin
override fun onDestroy() {
    super.onDestroy()
    executor.shutdown()  // Release thread pool resources
}
```

## Execution Flow

```
Button click
    |
    v
Start AnimatorSet (4 sequential animations)
    |
    v
onAnimationEnd()
    |
    +-- executor.execute {           <- secondary thread
    |       val result = counter++
    |       runOnUiThread {
    |           list.add(result)     <- UI thread
    |           adapter.notifyDataSetChanged()
    |       }
    |   }
    |
    +-- animSet.start()  <- restart
```

## Available Executor Types
| Factory | Description |
|---------|-------------|
| `newSingleThreadExecutor()` | Single thread, tasks queued |
| `newFixedThreadPool(n)` | Pool of n threads |
| `newCachedThreadPool()` | Dynamic pool, creates threads as needed |
| `newScheduledThreadPool(n)` | For scheduled tasks |

---
[M02](M02-Audio-Service) | [Home](Home) | [M04](M04-Notificacions-SnackBar)
