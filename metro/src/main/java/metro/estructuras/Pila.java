package metro.estructuras;

/**
 * Pila genérica con comportamiento LIFO (Last In, First Out).
 * Implementada con nodos enlazados; no usa java.util.Stack ni similares.
 *
 * @param <T> Tipo de dato almacenado.
 */
public class Pila<T> {

    // ── Nodo interno ──────────────────────────────────────────────────────────

    private static class Nodo<T> {
        T dato;
        Nodo<T> siguiente;

        Nodo(T dato) {
            this.dato = dato;
            this.siguiente = null;
        }
    }

    // ── Atributos ─────────────────────────────────────────────────────────────

    /** Nodo en la cima de la pila. */
    private Nodo<T> cima;

    /** Número de elementos actuales. */
    private int tamanio;

    // ── Constructor ───────────────────────────────────────────────────────────

    public Pila() {
        this.cima = null;
        this.tamanio = 0;
    }

    // ── Operaciones ───────────────────────────────────────────────────────────

    /**
     * Agrega un elemento en la cima de la pila (push).
     *
     * @param dato Elemento a apilar.
     */
    public void apilar(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        nuevo.siguiente = cima;
        cima = nuevo;
        tamanio++;
    }

    /**
     * Elimina y devuelve el elemento de la cima (pop).
     *
     * @return Elemento desapilado.
     * @throws IllegalStateException si la pila está vacía.
     */
    public T desapilar() {
        if (estaVacia()) {
            throw new IllegalStateException("La pila está vacía. No hay historial de consultas.");
        }
        T dato = cima.dato;
        cima = cima.siguiente;
        tamanio--;
        return dato;
    }

    /**
     * Devuelve el elemento de la cima sin eliminarlo (peek).
     *
     * @return Elemento en la cima.
     * @throws IllegalStateException si la pila está vacía.
     */
    public T verCima() {
        if (estaVacia()) {
            throw new IllegalStateException("La pila está vacía.");
        }
        return cima.dato;
    }

    /** Indica si la pila no tiene elementos. */
    public boolean estaVacia() {
        return tamanio == 0;
    }

    /** Devuelve el número de elementos en la pila. */
    public int tamanio() {
        return tamanio;
    }

    public ListaEnlazada<T> listarElementos() {
        ListaEnlazada<T> elementos = new ListaEnlazada<>();
        Nodo<T> actual = cima;
        while (actual != null) {
            elementos.agregar(actual.dato);
            actual = actual.siguiente;
        }
        return elementos;
    }

    /** Elimina todos los elementos. */
    public void limpiar() {
        cima = null;
        tamanio = 0;
    }

    /**
     * Muestra todos los elementos desde la cima hasta el fondo.
     */
    @Override
    public String toString() {
        if (estaVacia()) return "Pila vacía []";
        StringBuilder sb = new StringBuilder("Cima -> [");
        Nodo<T> actual = cima;
        while (actual != null) {
            sb.append(actual.dato);
            if (actual.siguiente != null) sb.append(" | ");
            actual = actual.siguiente;
        }
        sb.append("]");
        return sb.toString();
    }
}
