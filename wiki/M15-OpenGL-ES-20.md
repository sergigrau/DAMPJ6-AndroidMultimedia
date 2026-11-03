# M15 – OpenGL ES 2.0

## Objectiu
Introduir la programació de gràfics 3D a Android amb **OpenGL ES 2.0**, aprenent a configurar un `GLSurfaceView`, crear un `Renderer` i dibuixar formes geomètriques (triangle i quadrat) amb **shaders GLSL**. Inclou rotació interactiva mitjançant gestos de tacte.

## Fitxers involucrats
| Fitxer | Descripció |
|--------|------------|
| `M15_OpenGLES20Activity.kt` | Activitat principal |
| `M15_OpenGLES20GLSurfaceView.kt` | Vista OpenGL amb esdeveniments de tacte |
| `M15_OpenGLES20Renderer.kt` | Renderitzador (lògica de dibuix) |
| `M15_OpenGLES20Triangle.kt` | Geometria del triangle |
| `M15_OpenGLES20Quadrat.kt` | Geometria del quadrat |

## Arquitectura d'OpenGL ES a Android

```
Activity
    +-- GLSurfaceView  (contenidor de la vista OpenGL)
            +-- Renderer  (bucle de renderitzat en un fil separat)
                    +-- onSurfaceCreated()   -> inicialitzar shaders i formes
                    +-- onSurfaceChanged()   -> ajustar viewport i projecció
                    +-- onDrawFrame()        -> dibuixar cada fotograma
```

## Conceptes clau

### GLSurfaceView – Configuració
```kotlin
class M15_OpenGLES20GLSurfaceView(context: Context) : GLSurfaceView(context) {
    private val renderer: M15_OpenGLES20Renderer

    init {
        setEGLContextClientVersion(2)       // OpenGL ES 2.0
        renderer = M15_OpenGLES20Renderer()
        setRenderer(renderer)
        renderMode = RENDERMODE_WHEN_DIRTY  // Renderitzar només quan calgui
    }
}
```

**Modes de renderitzat:**
| Mode | Descripció |
|------|------------|
| `RENDERMODE_CONTINUOUSLY` | Renderitza a ~60 fps (per a animacions) |
| `RENDERMODE_WHEN_DIRTY` | Renderitza només quan es crida `requestRender()` |

### Shaders GLSL
Els **shaders** són programes que s'executen a la GPU. N'hi ha de dos tipus:

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
uniform vec4 vColor;         // Color uniforme
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
val program = GLES20.glCreateProgram()
GLES20.glAttachShader(program, vertexShader)
GLES20.glAttachShader(program, fragmentShader)
GLES20.glLinkProgram(program)

// 3. Utilitzar el programa i dibuixar
GLES20.glUseProgram(program)
GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount)           // Triangle
GLES20.glDrawElements(GLES20.GL_TRIANGLES, count, GLES20.GL_UNSIGNED_SHORT, buffer)  // Quadrat
```

### Matrius de transformació MVP
La matriu **MVP** (Model-View-Projection) transforma les coordenades 3D en coordenades de pantalla:

```kotlin
// Matriu View: posició de la càmera
Matrix.setLookAtM(viewMatrix, 0,
    0f, 0f, -3f,  // Ull (posició de la càmera)
    0f, 0f, 0f,   // Centre (punt on mira)
    0f, 1f, 0f    // Vector cap amunt
)

// Matriu Projection: perspectiva
Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 7f)

// Combinar: MVP = Projection x View
Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, viewMatrix, 0)

// Rotació
Matrix.setRotateM(rotationMatrix, 0, angle, 0f, 0f, 1f)
Matrix.multiplyMM(scratch, 0, mvpMatrix, 0, rotationMatrix, 0)
```

### Buffers de vèrtexs (FloatBuffer)
Les coordenades dels vèrtexs s'han d'emmagatzemar en buffers natius:

```kotlin
// Triangle (3 vèrtexs x 3 coordenades)
val triangleCoords = floatArrayOf(
     0.0f,  0.622f, 0.0f,   // a dalt
    -0.5f, -0.311f, 0.0f,   // a baix a l'esquerra
     0.5f, -0.311f, 0.0f    // a baix a la dreta
)

val vertexBuffer: FloatBuffer = ByteBuffer
    .allocateDirect(triangleCoords.size * 4)  // 4 bytes per float
    .order(ByteOrder.nativeOrder())
    .asFloatBuffer()
    .apply { put(triangleCoords); position(0) }
```

### Rotació per tacte
```kotlin
override fun onTouchEvent(e: MotionEvent): Boolean {
    if (e.action == MotionEvent.ACTION_MOVE) {
        var dx = e.x - prevX
        var dy = e.y - prevY

        // Invertir direcció al punt mig de la pantalla
        if (e.y > height / 2) dx *= -1
        if (e.x < width / 2)  dy *= -1

        renderer.setAngle(renderer.getAngle() + (dx + dy) * TOUCH_SCALE_FACTOR)
        requestRender()
    }
    // ...
}
```

## Sistema de coordenades OpenGL
```
       +Y
        |
   -X --+-- +X
        |
       -Y
        Z (cap a l'observador)
```

Les coordenades van des de **-1 fins a +1** a cada eix (Normalized Device Coordinates - NDC).

## Consideracions de rendiment
- Minimitzar les crides `glDraw*()` per fotograma.
- Reutilitzar els shaders compilats (no recompilar a cada fotograma).
- Utilitzar `RENDERMODE_WHEN_DIRTY` per estalviar bateria quan no hi hagi animació.

---
[M14](M14-Imatge-SD) | [Home](Home) | [M16](M16-Lectura-XML)
