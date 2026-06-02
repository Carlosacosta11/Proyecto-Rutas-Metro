package metro.estructuras;

/**
 * Árbol Binario de Búsqueda (BST) genérico.
 * Las claves deben implementar Comparable.
 * No usa java.util.TreeMap ni similares.
 *
 * @param <K> Tipo de clave comparable.
 * @param <V> Tipo de valor asociado.
 */
public class ArbolBinarioBusqueda<K extends Comparable<K>, V> {

    // ── Nodo interno ──────────────────────────────────────────────────────────

    /**
     * Nodo del árbol con clave, valor e hijos izquierdo/derecho.
     */
    private static class Nodo<K, V> {
        K clave;
        V valor;
        Nodo<K, V> izquierdo;
        Nodo<K, V> derecho;

        Nodo(K clave, V valor) {
            this.clave = clave;
            this.valor = valor;
            this.izquierdo = null;
            this.derecho = null;
        }
    }

    // ── Atributos ─────────────────────────────────────────────────────────────

    private Nodo<K, V> raiz;
    private int tamanio;

    // ── Constructor ───────────────────────────────────────────────────────────

    public ArbolBinarioBusqueda() {
        this.raiz = null;
        this.tamanio = 0;
    }

    // ── Operaciones ───────────────────────────────────────────────────────────

    /**
     * Inserta o actualiza una clave-valor en el árbol.
     *
     * @param clave Clave comparable.
     * @param valor Valor asociado.
     */
    public void insertar(K clave, V valor) {
        if (clave == null) throw new IllegalArgumentException("La clave no puede ser null.");
        raiz = insertarRecursivo(raiz, clave, valor);
    }

    private Nodo<K, V> insertarRecursivo(Nodo<K, V> nodo, K clave, V valor) {
        if (nodo == null) {
            tamanio++;
            return new Nodo<>(clave, valor);
        }
        int cmp = clave.compareTo(nodo.clave);
        if (cmp < 0) {
            nodo.izquierdo = insertarRecursivo(nodo.izquierdo, clave, valor);
        } else if (cmp > 0) {
            nodo.derecho = insertarRecursivo(nodo.derecho, clave, valor);
        } else {
            nodo.valor = valor; // actualizar valor existente
        }
        return nodo;
    }

    /**
     * Busca el valor asociado a la clave dada.
     *
     * @param clave Clave a buscar.
     * @return Valor encontrado, o null si no existe.
     */
    public V buscar(K clave) {
        if (clave == null) return null;
        Nodo<K, V> nodo = buscarNodo(raiz, clave);
        return nodo != null ? nodo.valor : null;
    }

    private Nodo<K, V> buscarNodo(Nodo<K, V> nodo, K clave) {
        if (nodo == null) return null;
        int cmp = clave.compareTo(nodo.clave);
        if (cmp < 0) return buscarNodo(nodo.izquierdo, clave);
        if (cmp > 0) return buscarNodo(nodo.derecho, clave);
        return nodo;
    }

    /**
     * Elimina la entrada con la clave dada.
     *
     * @param clave Clave a eliminar.
     */
    public void eliminar(K clave) {
        if (clave == null) return;
        boolean[] eliminado = {false};
        raiz = eliminarRecursivo(raiz, clave, eliminado);
        if (eliminado[0]) tamanio--;
    }

    private Nodo<K, V> eliminarRecursivo(Nodo<K, V> nodo, K clave, boolean[] eliminado) {
        if (nodo == null) return null;
        int cmp = clave.compareTo(nodo.clave);
        if (cmp < 0) {
            nodo.izquierdo = eliminarRecursivo(nodo.izquierdo, clave, eliminado);
        } else if (cmp > 0) {
            nodo.derecho = eliminarRecursivo(nodo.derecho, clave, eliminado);
        } else {
            eliminado[0] = true;
            // Caso 1: Sin hijos
            if (nodo.izquierdo == null && nodo.derecho == null) return null;
            // Caso 2: Un solo hijo
            if (nodo.izquierdo == null) return nodo.derecho;
            if (nodo.derecho == null) return nodo.izquierdo;
            // Caso 3: Dos hijos → sucesor inorden
            Nodo<K, V> sucesor = minimoNodo(nodo.derecho);
            nodo.clave = sucesor.clave;
            nodo.valor = sucesor.valor;
            nodo.derecho = eliminarRecursivo(nodo.derecho, sucesor.clave, new boolean[]{false});
        }
        return nodo;
    }

    private Nodo<K, V> minimoNodo(Nodo<K, V> nodo) {
        while (nodo.izquierdo != null) nodo = nodo.izquierdo;
        return nodo;
    }

    /**
     * Indica si la clave existe en el árbol.
     */
    public boolean contiene(K clave) {
        return buscar(clave) != null;
    }

    /** Número de entradas en el árbol. */
    public int tamanio() {
        return tamanio;
    }

    /** Indica si el árbol está vacío. */
    public boolean estaVacio() {
        return tamanio == 0;
    }

    // ── Recorridos ────────────────────────────────────────────────────────────

    /**
     * Recorre el árbol en orden (inorden) y ejecuta la acción sobre cada entrada.
     * Produce las claves en orden ascendente.
     *
     * @param accion Acción a ejecutar con cada clave y valor.
     */
    public void inorden(AccionEntrada<K, V> accion) {
        inordenRecursivo(raiz, accion);
    }

    private void inordenRecursivo(Nodo<K, V> nodo, AccionEntrada<K, V> accion) {
        if (nodo == null) return;
        inordenRecursivo(nodo.izquierdo, accion);
        accion.ejecutar(nodo.clave, nodo.valor);
        inordenRecursivo(nodo.derecho, accion);
    }

    // ── Interfaz funcional ────────────────────────────────────────────────────

    /**
     * Interfaz funcional para procesar entradas del árbol
     * sin depender de java.util.function.
     */
    public interface AccionEntrada<K, V> {
        void ejecutar(K clave, V valor);
    }

    @Override
    public String toString() {
        if (estaVacio()) return "Árbol vacío";
        StringBuilder sb = new StringBuilder("BST (inorden): [");
        inorden((k, v) -> sb.append(k).append(": ").append(v).append(", "));
        // Quitar la última coma
        if (sb.length() > 14) sb.setLength(sb.length() - 2);
        sb.append("]");
        return sb.toString();
    }
}
