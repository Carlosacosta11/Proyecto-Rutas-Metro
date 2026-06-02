# 🚆 MetroTransit – Sistema de Simulación de Metro

##  Nombre del proyecto
**MetroTransit** – Aplicación de escritorio para la simulación de un sistema de metro.

---

##  Descripción general
MetroTransit permite gestionar estaciones, líneas de metro y pasajeros, modelando la red de transporte mediante estructuras de datos implementadas **totalmente a mano** (sin usar ninguna clase de `java.util`). Ofrece cálculo de rutas óptimas con el algoritmo de Dijkstra, simulación de espera en andenes (cola FIFO), historial de consultas (pila LIFO), registro de pasajeros con tabla hash (colisiones por encadenamiento), catálogo de líneas con árbol binario de búsqueda (BST) y una interfaz gráfica moderna en Java Swing con mapa interactivo (zoom y arrastre).

---

##  Objetivo académico
- Aplicar los conceptos fundamentales de estructuras de datos en un caso de uso real.
- Implementar manualmente listas enlazadas, colas, pilas, tablas hash, árboles BST y grafos.
- Integrar todas las estructuras en una aplicación funcional con interfaz gráfica.
- Practicar Programación Orientada a Objetos (POO) y Clean Code.

---

##  Tecnologías utilizadas
| Tecnología | Versión | Propósito |
|------------|---------|------------|
| Java | 21 | Lenguaje base |
| Swing | - | Interfaz gráfica de usuario |
| Graphics2D | - | Dibujo del mapa interactivo |
| POO | - | Organización del código |
| Clean Code | - | Mantenibilidad y claridad |

---

##  Arquitectura general
El proyecto sigue una arquitectura multicapa:

- **Capa de estructuras**: implementaciones manuales de listas, colas, pilas, tabla hash, BST y grafo.
- **Capa de modelo**: entidades del dominio (Estación, Línea, Pasajero, ConsultaRuta).
- **Capa de servicio**: lógica de negocio centralizada (`MetroService`) y carga de datos (`DatosDemo`).
- **Capa de interfaz de usuario**: paneles Swing independientes, mapa interactivo y ventana principal con navegación tipo dashboard.

---

##  Estructuras de datos implementadas manualmente
| Estructura | Clase | Aplicación en el sistema |
|------------|-------|--------------------------|
| Lista Enlazada | `ListaEnlazada<T>` | Almacena las estaciones de cada línea de metro |
| Cola FIFO | `Cola<T>` | Pasajeros esperando en el andén |
| Pila LIFO | `Pila<T>` | Historial de consultas de rutas |
| Tabla Hash | `TablaHash<K,V>` | Registro de pasajeros por ID (colisiones por encadenamiento) |
| Árbol BST | `ArbolBinarioBusqueda<K,V>` | Catálogo de líneas ordenado por código |
| Grafo ponderado | `Grafo<T>` | Red de conexiones entre estaciones con tiempos de viaje |

---

## Algoritmos utilizados
- **Dijkstra** – Cálculo de la ruta más corta entre dos estaciones, implementado manualmente sin `PriorityQueue`.
- **Recorrido inorden** – Para listar las líneas en orden ascendente desde el BST.
- **Función hash** – `Math.abs(clave.hashCode()) % capacidad` con resolución de colisiones por encadenamiento.
- **Transformaciones gráficas** – Zoom y arrastre del mapa mediante `AffineTransform`.

---

##  Funcionalidades principales
- Gestión de líneas y estaciones (agregar, eliminar, listar) mediante lista enlazada.
- Simulación de pasajeros en el andén (encolar, desencolar, ver frente).
- Historial de rutas consultadas (apilar, desapilar, ver cima).
- Registro de pasajeros con tabla hash y demostración de colisiones.
- Catálogo de líneas ordenado (buscar, insertar, listar todo).
- Cálculo de ruta más corta con Dijkstra (origen, destino, tiempo total, ruta detallada).
- Mapa interactivo con zoom (rueda del mouse), arrastre (clic y mover), resaltado de la ruta activa.
- Dashboard con indicadores en tiempo real (estaciones, pasajeros, etc.).
- Casos borde: estación inexistente, cola vacía, pila vacía, ID no registrado.

---

##  Restricciones cumplidas
**No se utilizaron en ningún momento las siguientes clases de Java Collections Framework:**
- `ArrayList`
- `LinkedList`
- `Stack`
- `Queue`
- `HashMap`
- `Hashtable`
- `TreeMap`
- `PriorityQueue`
- Cualquier otra estructura de `java.util` (como `HashSet`, `TreeSet`, `ArrayDeque`, etc.)

Todas las estructuras fueron implementadas desde cero con nodos enlazados y arreglos propios.

---

##  Estructura de carpetas del proyecto

metro/
├── Main.java
├── estructuras/
│ ├── ListaEnlazada.java
│ ├── Cola.java
│ ├── Pila.java
│ ├── TablaHash.java
│ ├── ArbolBinarioBusqueda.java
│ └── Grafo.java
├── modelo/
│ ├── Estacion.java
│ ├── LineaMetro.java
│ ├── Pasajero.java
│ └── ConsultaRuta.java
├── servicio/
│ ├── MetroService.java
│ └── DatosDemo.java
└── ui/
├── EstiloUI.java
├── VentanaPrincipal.java
├── VistaMapaExpandida.java
└── paneles/
├── PanelDashboard.java
├── PanelListaEnlazada.java
├── PanelColaPila.java
├── PanelTablaHash.java
├── PanelBSTGrafo.java
└── PanelMapa.java


---

##  Instrucciones de compilación y ejecución
### Requisitos
- Java JDK 21 o superior
- Terminal (Linux/macOS) o CMD/PowerShell (Windows)

### Compilar
```bash
javac -d out $(find src -name "*.java")
Nota: si se usa una estructura diferente, ajustar la ruta de src.

Ejecutar
bash
java -cp out metro.Main
Alternativa (desde IDE)
Abrir el proyecto en IntelliJ IDEA, Eclipse o VS Code y ejecutar la clase Main.java.

 Integrantes del grupo
N°	Nombre
1	Carlos Daniel Acosta Prieto
2	Andres Felipe Realpe Ceron
3	Haver Fernando Jimenez Muñoz
4	Juan Camilo Camayo
5	Daniel Alexis Alvarado Hoyos
6	Sebastian Cadena Alava
