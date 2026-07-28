# M15 – OpenGL ES 2.0

## 🎯 Objectiu
Introduir la programació gràfica 3D en Android amb **OpenGL ES 2.0**, aprenent a configurar un `GLSurfaceView`, crear un `Renderer`, i dibuixar formes geomètriques (triangle i quadrat) amb **shaders** GLSL. Inclou rotació interactiva per tàctil.

## 📄 Fitxers implicats
| Fitxer | Descripció |
|--------|-----------|
| `M15_OpenGLES20Activity.kt` | Activity principal |
| `M15_OpenGLES20GLSurfaceView.kt` | Vista OpenGL amb events tàctils |
| `M15_OpenGLES20Renderer.kt` | Renderitzador (lògica de dibuix) |
| `M15_OpenGLES20Triangle.kt` | Geometria del triangle |
| `M15_OpenGLES20Quadrat.kt` | Geometria del quadrat |

## 🔑 Arquitectura OpenGL ES a Android

```
Activity
    └─► GLSurfaceView  (contenidor de la vista OpenGL)
            └─► Renderer  (bucle de renderitzat en fil separat)
                    ├─► onSurfaceCreated()   → inicialitzar shaders, formes
                    ├─► onSurfaceChanged()   → ajustar viewport i projecció
                    └─► onDrawFrame()        → dibuixar cada fotograma
```

## 🔑 Conceptes clau

### GLSurfaceView – Configuració
```kotlin
class M15_OpenGLES20GLSurfaceView(context: Context) : GLSurfaceView(context) {
    private val renderer: M15_OpenGLES20Renderer

    init {
        setEGLContextClientVersion(2)       // OpenGL ES 2.0
        renderer = M15_OpenGLES20Renderer()
        setRenderer(renderer)
        renderMode = RENDERMODE_WHEN_DIRTY  // Renderitza només quan cal
    }
}
```

**Modes de renderitzat:**
| Mode | Descripció |
|------|-----------|
| `RENDERMODE_CONTINUOUSLY` | Renderitza ~60 fps (per animacions) |
| `RENDERMODE_WHEN_DIRTY` | Renderitza quan es crida `requestRender()` |

### Shaders GLSL
Els **shaders** són programes que s'executen a la GPU. N'hi ha dos tipus:

**Vertex Shader** – Calcula la posició de cada vèrtex:
```glsl
uniform mat4 uMVPMatrix;     // Matriu de transformació
attribute vec4 vPosition;    // Posició del vèrtex d'entrada
void main() {
    gl_Position = uMVPMatrix * vPosition;  // Posició final
}
```

**Fragment Shader** – Calcula el color de cada píxel:
```glsl
precision mediump float;
uniform vec4 vColor;         // Color uniform
void main() {
    gl_FragColor = vColor;   // Color de sortida
}
```

### Pipeline de renderitzat

```kotlin
// 1. Compilar shaders
val vertexShader   = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER)
GLES20.glShaderSource(vertexShader, vertexShaderCode)
GLES20.glCompileShader(vertexShader)

// 2. Crear programa i enllaçar shaders
val programa = GLES20.glCreateProgram()
GLES20.glAttachShader(programa, vertexShader)
GLES20.glAttachShader(programa, fragmentShader)
GLES20.glLinkProgram(programa)

// 3. Usar el programa i dibuixar
GLES20.glUseProgram(programa)
GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount)     // Triangle
GLES20.glDrawElements(GLES20.GL_TRIANGLES, count, GLES20.GL_UNSIGNED_SHORT, buffer)  // Quadrat
```

### Matrius de transformació MVP
La matriu **MVP** (Model-View-Projection) transforma les coordenades 3D en coordenades de pantalla:

```kotlin
// Matriu Vista: posició de la càmera
Matrix.setLookAtM(mMatriuVista, 0,
    0f, 0f, -3f,  // Eye (posició càmera)
    0f, 0f, 0f,   // Center (punt que mira)
    0f, 1f, 0f    // Up (vector amunt)
)

// Matriu Projecció: perspectiva
Matrix.frustumM(mMatriuProjeccio, 0, -ratio, ratio, -1f, 1f, 3f, 7f)

// Combinació: MVP = Projecció × Vista
Matrix.multiplyMM(mMVPMatrix, 0, mMatriuProjeccio, 0, mMatriuVista, 0)

// Rotació
Matrix.setRotateM(mMatriuRotacio, 0, angle, 0f, 0f, 1f)
Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mMatriuRotacio, 0)
```

### Buffers de vèrtexs (FloatBuffer)
Les coordenades dels vèrtexs s'han d'emmagatzemar en buffers natius:

```kotlin
// Triangle (3 vèrtexs × 3 coordenades)
val triangleCoords = floatArrayOf(
     0.0f,  0.622f, 0.0f,   // cim
    -0.5f, -0.311f, 0.0f,   // inferior esquerre
     0.5f, -0.311f, 0.0f    // inferior dret
)

val vertexBuffer: FloatBuffer = ByteBuffer
    .allocateDirect(triangleCoords.size * 4)  // 4 bytes per float
    .order(ByteOrder.nativeOrder())
    .asFloatBuffer()
    .apply { put(triangleCoords); position(0) }
```

### Rotació per tàctil
```kotlin
override fun onTouchEvent(e: MotionEvent): Boolean {
    if (e.action == MotionEvent.ACTION_MOVE) {
        var dx = e.x - previaX
        var dy = e.y - previaY

        // Invertir direcció a la meitat de la pantalla
        if (e.y > height / 2) dx *= -1
        if (e.x < width / 2)  dy *= -1

        renderer.setAngle(renderer.getAngle() + (dx + dy) * TOUCH_ESCALA_FACTOR)
        requestRender()
    }
    // ...
}
```

## 📐 Sistema de coordenades OpenGL
```
       +Y
        │
   -X ──┼── +X
        │
       -Y
        Z (surt cap a l'espectador)
```

Les coordenades de OpenGL van de **-1 a +1** en cada eix (coordenades normalitzades del dispositiu - NDC).

## ⚠️ Consideracions de rendiment
- Minimitzar les crides a `glDraw*()` per fotograma.
- Reutilitzar shaders compilats (no recompilar cada fotograma).
- Usar `RENDERMODE_WHEN_DIRTY` per estalviar bateria quan no hi ha animació.

---
[← M14](M14-Imatge-SD) | [Inici](Home) | [M16 →](M16-Lectura-XML)

