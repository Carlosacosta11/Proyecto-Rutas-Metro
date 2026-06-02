package metro.estructuras;

/**
 * Tabla hash genérica con encadenamiento por lista enlazada para resolver colisiones.
 * No usa java.util.HashMap ni java.util.Hashtable.
 *
 * @param <K> Tipo de clave.
 * @param <V> Tipo de valor.
 */
public class TablaHash<K, V> {

    // ── Par clave-valor (entrada) ─────────────────────────────────────────────

    /**
     * Representa un par clave-valor almacenado en un bucket.
     */
    private static class Entrada<K, V> {
        K clave;
        V valor;
        Entrada<K, V> siguiente; // para el encadenamiento

        Entrada(K clave, V valor) {
            this.clave = clave;
            this.valor = valor;
            this.siguiente = null;
        }
    }

    // ── Atributos ─────────────────────────────────────────────────────────────

    /** Capacidad por defecto de la tabla. */
    private static final int CAPACIDAD_DEFAULT = 16;

    /** Umbral de carga antes de redimensionar. */
    private static final double FACTOR_CARGA = 0.75;

    /** Arreglo de buckets (cada uno encabeza una lista enlazada). */
    private Entrada<K, V>[] tabla;

    /** Número de entradas almacenadas. */
    private int tamanio;

    /** Capacidad actual de la tabla. */
    private int capacidad;

    // ── Constructor ───────────────────────────────────────────────────────────

    @SuppressWarnings("unchecked")
    public TablaHash() {
        this.capacidad = CAPACIDAD_DEFAULT;
        this.tabla = new Entrada[capacidad];
        this.tamanio = 0;
    }

    @SuppressWarnings("unchecked")
    public TablaHash(int capacidadInicial) {
        this.capacidad = capacidadInicial;
        this.tabla = new Entrada[capacidad];
        this.tamanio = 0;
    }

    // ── Función hash ──────────────────────────────────────────────────────────

    /**
     * Calcula el índice del bucket para la clave dada.
     * Usa Math.abs para evitar índices negativos.
     *
     * @param clave Clave a hashear.
     * @return Índice en el arreglo de buckets.
     */
    private int calcularIndice(K clave) {
        return Math.abs(clave.hashCode()) % capacidad;
    }

    // ── Operaciones CRUD ──────────────────────────────────────────────────────

    /**
     * Inserta o actualiza una entrada clave-valor.
     * Si la clave ya existe, actualiza el valor.
     * Tiempo promedio O(1).
     *
     * @param clave Clave única.
     * @param valor Valor asociado.
     */
    public void insertar(K clave, V valor) {
        if (clave == null) throw new IllegalArgumentException("La clave no puede ser null.");

        // Redimensionar si se supera el factor de carga
        if ((double) tamanio / capacidad >= FACTOR_CARGA) {
            redimensionar();
        }

        int indice = calcularIndice(clave);
        Entrada<K, V> actual = tabla[indice];

        // Buscar si la clave ya existe en el bucket (actualizar)
        while (actual != null) {
            if (actual.clave.equals(clave)) {
                actual.valor = valor; // actualización
                return;
            }
            actual = actual.siguiente;
        }

        // Insertar al inicio del bucket (encadenamiento)
        Entrada<K, V> nueva = new Entrada<>(clave, valor);
        nueva.siguiente = tabla[indice];
        tabla[indice] = nueva;
        tamanio++;
    }

    /**
     * Busca el valor asociado a la clave dada.
     * Tiempo promedio O(1).
     *
     * @param clave Clave a buscar.
     * @return Valor encontrado, o null si no existe.
     */
    public V buscar(K clave) {
        if (clave == null) return null;
        int indice = calcularIndice(clave);
        Entrada<K, V> actual = tabla[indice];
        while (actual != null) {
            if (actual.clave.equals(clave)) return actual.valor;
            actual = actual.siguiente;
        }
        return null;
    }

    /**
     * Elimina la entrada con la clave dada.
     *
     * @param clave Clave a eliminar.
     * @return true si se eliminó, false si no existía.
     */
    public boolean eliminar(K clave) {
        if (clave == null) return false;
        int indice = calcularIndice(clave);
        Entrada<K, V> actual = tabla[indice];
        Entrada<K, V> anterior = null;

        while (actual != null) {
            if (actual.clave.equals(clave)) {
                if (anterior == null) {
                    tabla[indice] = actual.siguiente;
                } else {
                    anterior.siguiente = actual.siguiente;
                }
                tamanio--;
                return true;
            }
            anterior = actual;
            actual = actual.siguiente;
        }
        return false;
    }

    /**
     * Indica si la clave existe en la tabla.
     */
    public boolean contiene(K clave) {
        return buscar(clave) != null;
    }

    /**
     * Lista todos los valores almacenados en la tabla.
     */
    public ListaEnlazada<V> listarValores() {
        ListaEnlazada<V> valores = new ListaEnlazada<>();
        for (int i = 0; i < capacidad; i++) {
            Entrada<K, V> actual = tabla[i];
            while (actual != null) {
                valores.agregar(actual.valor);
                actual = actual.siguiente;
            }
        }
        return valores;
    }

    /**
     * Recorre todos los buckets y entrega la primera entrada de cada bucket.
     */
    public void recorrerBuckets(BucketVisitor<K, V> visitor) {
        for (int i = 0; i < capacidad; i++) {
            if (tabla[i] != null) {
                visitor.visitar(i, tabla[i]);
            }
        }
    }

    public interface BucketVisitor<K, V> {
        void visitar(int indice, Entrada<K, V> entrada);
    }

    /** Número de entradas almacenadas. */
    public int tamanio() {
        return tamanio;
    }

    /** Indica si la tabla está vacía. */
    public boolean estaVacia() {
        return tamanio == 0;
    }

    // ── Redimensionamiento ────────────────────────────────────────────────────

    /**
     * Duplica la capacidad y reinserta todas las entradas.
     * Se activa cuando se supera el factor de carga.
     */
    @SuppressWarnings("unchecked")
    private void redimensionar() {
        int nuevaCapacidad = capacidad * 2;
        Entrada<K, V>[] nuevaTabla = new Entrada[nuevaCapacidad];

        for (int i = 0; i < capacidad; i++) {
            Entrada<K, V> actual = tabla[i];
            while (actual != null) {
                Entrada<K, V> siguiente = actual.siguiente;
                int nuevoIndice = Math.abs(actual.clave.hashCode()) % nuevaCapacidad;
                actual.siguiente = nuevaTabla[nuevoIndice];
                nuevaTabla[nuevoIndice] = actual;
                actual = siguiente;
            }
        }
        tabla = nuevaTabla;
        capacidad = nuevaCapacidad;
    }

    // ── Diagnóstico de colisiones ─────────────────────────────────────────────

    /**
     * Devuelve el índice de bucket donde se almacenaría una clave.
     * Útil para demostrar colisiones en el menú de pruebas.
     *
     * @param clave Clave a inspeccionar.
     * @return Índice del bucket.
     */
    public int obtenerBucket(K clave) {
        return calcularIndice(clave);
    }

    /**
     * Muestra el contenido de todos los buckets ocupados.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TablaHash {\n");
        for (int i = 0; i < capacidad; i++) {
            if (tabla[i] != null) {
                sb.append("  Bucket[").append(i).append("]: ");
                Entrada<K, V> actual = tabla[i];
                while (actual != null) {
                    sb.append("[").append(actual.clave).append(" -> ").append(actual.valor).append("]");
                    if (actual.siguiente != null) sb.append(" -> ");
                    actual = actual.siguiente;
                }
                sb.append("\n");
            }
        }
        sb.append("}");
        return sb.toString();
    }
}
