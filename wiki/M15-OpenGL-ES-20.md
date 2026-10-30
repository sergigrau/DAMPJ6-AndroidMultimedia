# M15 – OpenGL ES 2.0

## Objective
Introduce 3D graphics programming on Android with **OpenGL ES 2.0**, learning how to configure a `GLSurfaceView`, create a `Renderer`, and draw geometric shapes (triangle and square) with **GLSL shaders**. Includes interactive rotation via touch.

## Files Involved
| File | Description |
|------|-------------|
| `M15_OpenGLES20Activity.kt` | Main activity |
| `M15_OpenGLES20GLSurfaceView.kt` | OpenGL view with touch events |
| `M15_OpenGLES20Renderer.kt` | Renderer (drawing logic) |
| `M15_OpenGLES20Triangle.kt` | Triangle geometry |
| `M15_OpenGLES20Quadrat.kt` | Square geometry |

## OpenGL ES Architecture in Android

```
Activity
    +-- GLSurfaceView  (OpenGL view container)
            +-- Renderer  (render loop on a separate thread)
                    +-- onSurfaceCreated()   -> initialise shaders, shapes
                    +-- onSurfaceChanged()   -> adjust viewport and projection
                    +-- onDrawFrame()        -> draw each frame
```

## Key Concepts

### GLSurfaceView – Setup
```kotlin
class M15_OpenGLES20GLSurfaceView(context: Context) : GLSurfaceView(context) {
    private val renderer: M15_OpenGLES20Renderer

    init {
        setEGLContextClientVersion(2)       // OpenGL ES 2.0
        renderer = M15_OpenGLES20Renderer()
        setRenderer(renderer)
        renderMode = RENDERMODE_WHEN_DIRTY  // Only render when needed
    }
}
```

**Render modes:**
| Mode | Description |
|------|-------------|
| `RENDERMODE_CONTINUOUSLY` | Renders ~60 fps (for animations) |
| `RENDERMODE_WHEN_DIRTY` | Renders only when `requestRender()` is called |

### GLSL Shaders
**Shaders** are programs that run on the GPU. There are two types:

**Vertex Shader** – Computes the position of each vertex:
```glsl
uniform mat4 uMVPMatrix;     // Transformation matrix
attribute vec4 vPosition;    // Input vertex position
void main() {
    gl_Position = uMVPMatrix * vPosition;  // Final position
}
```

**Fragment Shader** – Computes the colour of each pixel:
```glsl
precision mediump float;
uniform vec4 vColor;         // Uniform colour
void main() {
    gl_FragColor = vColor;   // Output colour
}
```

### Rendering Pipeline

```kotlin
// 1. Compile shaders
val vertexShader   = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER)
GLES20.glShaderSource(vertexShader, vertexShaderCode)
GLES20.glCompileShader(vertexShader)

// 2. Create program and link shaders
val program = GLES20.glCreateProgram()
GLES20.glAttachShader(program, vertexShader)
GLES20.glAttachShader(program, fragmentShader)
GLES20.glLinkProgram(program)

// 3. Use the program and draw
GLES20.glUseProgram(program)
GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount)           // Triangle
GLES20.glDrawElements(GLES20.GL_TRIANGLES, count, GLES20.GL_UNSIGNED_SHORT, buffer)  // Square
```

### MVP Transformation Matrices
The **MVP** matrix (Model-View-Projection) transforms 3D coordinates into screen coordinates:

```kotlin
// View matrix: camera position
Matrix.setLookAtM(viewMatrix, 0,
    0f, 0f, -3f,  // Eye (camera position)
    0f, 0f, 0f,   // Center (look-at point)
    0f, 1f, 0f    // Up vector
)

// Projection matrix: perspective
Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 7f)

// Combine: MVP = Projection x View
Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, viewMatrix, 0)

// Rotation
Matrix.setRotateM(rotationMatrix, 0, angle, 0f, 0f, 1f)
Matrix.multiplyMM(scratch, 0, mvpMatrix, 0, rotationMatrix, 0)
```

### Vertex Buffers (FloatBuffer)
Vertex coordinates must be stored in native buffers:

```kotlin
// Triangle (3 vertices x 3 coordinates)
val triangleCoords = floatArrayOf(
     0.0f,  0.622f, 0.0f,   // top
    -0.5f, -0.311f, 0.0f,   // bottom left
     0.5f, -0.311f, 0.0f    // bottom right
)

val vertexBuffer: FloatBuffer = ByteBuffer
    .allocateDirect(triangleCoords.size * 4)  // 4 bytes per float
    .order(ByteOrder.nativeOrder())
    .asFloatBuffer()
    .apply { put(triangleCoords); position(0) }
```

### Touch Rotation
```kotlin
override fun onTouchEvent(e: MotionEvent): Boolean {
    if (e.action == MotionEvent.ACTION_MOVE) {
        var dx = e.x - prevX
        var dy = e.y - prevY

        // Reverse direction at screen midpoint
        if (e.y > height / 2) dx *= -1
        if (e.x < width / 2)  dy *= -1

        renderer.setAngle(renderer.getAngle() + (dx + dy) * TOUCH_SCALE_FACTOR)
        requestRender()
    }
    // ...
}
```

## OpenGL Coordinate System
```
       +Y
        |
   -X --+-- +X
        |
       -Y
        Z (towards the viewer)
```

Coordinates range from **-1 to +1** on each axis (Normalized Device Coordinates - NDC).

## Performance Considerations
- Minimise `glDraw*()` calls per frame.
- Reuse compiled shaders (do not recompile every frame).
- Use `RENDERMODE_WHEN_DIRTY` to save battery when there is no animation.

---
[M14](M14-Imatge-SD) | [Home](Home) | [M16](M16-Lectura-XML)
