package metro.estructuras;

public class Grafo<T> {

    private static class Arista<T> {
        T destino;
        int peso;
        Arista<T> siguiente;
        Arista(T destino, int peso) {
            this.destino = destino;
            this.peso = peso;
            this.siguiente = null;
        }
    }

    private static class NodoGrafo<T> {
        T dato;
        Arista<T> primeraArista;
        NodoGrafo<T> siguiente;
        NodoGrafo(T dato) {
            this.dato = dato;
            this.primeraArista = null;
            this.siguiente = null;
        }
    }

    private NodoGrafo<T> cabeza;
    private int cantidadNodos;

    public interface AccionAdyacencia<T> {
        void ejecutar(T origen, T destino, int peso);
    }

    public interface AccionNodo<T> {
        void ejecutar(T nodo);
    }

    public Grafo() {
        this.cabeza = null;
        this.cantidadNodos = 0;
    }

    public ListaEnlazada<T> obtenerNodos() {
        ListaEnlazada<T> nodos = new ListaEnlazada<>();
        NodoGrafo<T> actual = cabeza;
        while (actual != null) {
            nodos.agregar(actual.dato);
            actual = actual.siguiente;
        }
        return nodos;
    }

    public ListaEnlazada<T> obtenerVecinos(T origen) {
        ListaEnlazada<T> vecinos = new ListaEnlazada<>();
        NodoGrafo<T> nodo = buscarNodo(origen);
        if (nodo == null) return vecinos;
        Arista<T> arista = nodo.primeraArista;
        while (arista != null) {
            vecinos.agregar(arista.destino);
            arista = arista.siguiente;
        }
        return vecinos;
    }

    public void recorrerNodos(AccionNodo<T> accion) {
        NodoGrafo<T> actual = cabeza;
        while (actual != null) {
            accion.ejecutar(actual.dato);
            actual = actual.siguiente;
        }
    }

    public void recorrerAdyacencias(AccionAdyacencia<T> accion) {
        NodoGrafo<T> actual = cabeza;
        while (actual != null) {
            Arista<T> arista = actual.primeraArista;
            while (arista != null) {
                accion.ejecutar(actual.dato, arista.destino, arista.peso);
                arista = arista.siguiente;
            }
            actual = actual.siguiente;
        }
    }

    public void agregarNodo(T dato) {
        if (buscarNodo(dato) == null) {
            NodoGrafo<T> nuevo = new NodoGrafo<>(dato);
            nuevo.siguiente = cabeza;
            cabeza = nuevo;
            cantidadNodos++;
        }
    }

    private NodoGrafo<T> buscarNodo(T dato) {
        NodoGrafo<T> actual = cabeza;
        while (actual != null) {
            if (actual.dato.equals(dato)) return actual;
            actual = actual.siguiente;
        }
        return null;
    }

    public void agregarArista(T origen, T destino, int peso) {
        agregarNodo(origen);
        agregarNodo(destino);
        agregarAristaUnidireccional(origen, destino, peso);
        agregarAristaUnidireccional(destino, origen, peso);
    }

    private void agregarAristaUnidireccional(T origen, T destino, int peso) {
        NodoGrafo<T> nodo = buscarNodo(origen);
        Arista<T> nueva = new Arista<>(destino, peso);
        nueva.siguiente = nodo.primeraArista;
        nodo.primeraArista = nueva;
    }

    /**
     * DIJKSTRA - CORREGIDO Y FUNCIONAL
     */
    public ResultadoRuta<T> dijkstra(T origen, T destino) {
        // 1. Verificar que ambos nodos existan
        NodoGrafo<T> nodoOrigen = buscarNodo(origen);
        NodoGrafo<T> nodoDestino = buscarNodo(destino);
        
        if (nodoOrigen == null || nodoDestino == null) {
            return null;
        }

        // 2. Obtener todos los nodos en una lista
        ListaEnlazada<T> todosNodos = new ListaEnlazada<>();
        NodoGrafo<T> actual = cabeza;
        while (actual != null) {
            todosNodos.agregar(actual.dato);
            actual = actual.siguiente;
        }

        int n = todosNodos.tamanio();
        
        // 3. Crear arreglos para Dijkstra
        int[] dist = new int[n];
        int[] prev = new int[n];
        boolean[] visitado = new boolean[n];
        int INFINITO = 999999;
        
        // 4. Inicializar
        for (int i = 0; i < n; i++) {
            dist[i] = INFINITO;
            prev[i] = -1;
            visitado[i] = false;
        }
        
        // 5. Encontrar índice del origen
        int idxOrigen = -1;
        for (int i = 0; i < n; i++) {
            if (todosNodos.obtener(i).equals(origen)) {
                idxOrigen = i;
                break;
            }
        }
        
        if (idxOrigen == -1) return null;
        dist[idxOrigen] = 0;
        
        // 6. Algoritmo de Dijkstra
        for (int i = 0; i < n; i++) {
            // Encontrar nodo no visitado con distancia mínima
            int u = -1;
            int minDist = INFINITO;
            for (int j = 0; j < n; j++) {
                if (!visitado[j] && dist[j] < minDist) {
                    minDist = dist[j];
                    u = j;
                }
            }
            
            if (u == -1) break;
            visitado[u] = true;
            
            // Relajar los vecinos de u
            T nodoU = todosNodos.obtener(u);
            NodoGrafo<T> nodoGrafoU = buscarNodo(nodoU);
            Arista<T> arista = nodoGrafoU.primeraArista;
            
            while (arista != null) {
                // Encontrar índice del destino
                int v = -1;
                for (int j = 0; j < n; j++) {
                    if (todosNodos.obtener(j).equals(arista.destino)) {
                        v = j;
                        break;
                    }
                }
                
                if (v != -1 && !visitado[v]) {
                    int nuevaDist = dist[u] + arista.peso;
                    if (nuevaDist < dist[v]) {
                        dist[v] = nuevaDist;
                        prev[v] = u;
                    }
                }
                arista = arista.siguiente;
            }
        }
        
        // 7. Encontrar índice del destino
        int idxDestino = -1;
        for (int i = 0; i < n; i++) {
            if (todosNodos.obtener(i).equals(destino)) {
                idxDestino = i;
                break;
            }
        }
        
        if (idxDestino == -1 || dist[idxDestino] == INFINITO) {
            return null;
        }
        
        // 8. Reconstruir el camino (desde destino hacia origen)
        ListaEnlazada<T> caminoReves = new ListaEnlazada<>();
        int actualIdx = idxDestino;
        while (actualIdx != -1) {
            caminoReves.agregar(todosNodos.obtener(actualIdx));
            actualIdx = prev[actualIdx];
        }
        
        // 9. Invertir el camino (usando otra lista)
        ListaEnlazada<T> camino = new ListaEnlazada<>();
        for (int i = caminoReves.tamanio() - 1; i >= 0; i--) {
            camino.agregar(caminoReves.obtener(i));
        }
        
        return new ResultadoRuta<>(camino, dist[idxDestino]);
    }

    public static class ResultadoRuta<T> {
        private final ListaEnlazada<T> camino;
        private final int tiempoTotal;

        public ResultadoRuta(ListaEnlazada<T> camino, int tiempoTotal) {
            this.camino = camino;
            this.tiempoTotal = tiempoTotal;
        }

        public ListaEnlazada<T> getCamino() {
            return camino;
        }

        public int getTiempoTotal() {
            return tiempoTotal;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < camino.tamanio(); i++) {
                if (i > 0) sb.append(" → ");
                sb.append(camino.obtener(i));
            }
            sb.append(" | ").append(tiempoTotal).append(" min");
            return sb.toString();
        }
    }

    public int cantidadNodos() {
        return cantidadNodos;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("=== LISTA DE ADYACENCIA ===\n");
        NodoGrafo<T> nodo = cabeza;
        while (nodo != null) {
            sb.append("  ").append(nodo.dato).append(": ");
            Arista<T> arista = nodo.primeraArista;
            while (arista != null) {
                sb.append("[").append(arista.destino).append(", ").append(arista.peso).append("min]");
                if (arista.siguiente != null) sb.append(" → ");
                arista = arista.siguiente;
            }
            sb.append("\n");
            nodo = nodo.siguiente;
        }
        return sb.toString();
    }
}