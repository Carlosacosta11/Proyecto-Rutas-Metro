package metro.estructuras;

public class Cola<T> {

    private static class Nodo<T> {
        T dato;
        Nodo<T> siguiente;
        Nodo(T dato) {
            this.dato = dato;
            this.siguiente = null;
        }
    }

    private Nodo<T> frente;
    private Nodo<T> final_;
    private int tamanio;

    public Cola() {
        this.frente = null;
        this.final_ = null;
        this.tamanio = 0;
    }

    public void encolar(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        if (estaVacia()) {
            frente = nuevo;
            final_ = nuevo;
        } else {
            final_.siguiente = nuevo;
            final_ = nuevo;
        }
        tamanio++;
    }

    public T desencolar() {
        if (estaVacia()) {
            throw new IllegalStateException("La cola está vacía. No hay pasajeros en el andén.");
        }
        T dato = frente.dato;
        frente = frente.siguiente;
        if (frente == null) final_ = null;
        tamanio--;
        return dato;
    }

    public T verFrente() {
        if (estaVacia()) {
            throw new IllegalStateException("La cola está vacía.");
        }
        return frente.dato;
    }

    public boolean estaVacia() {
        return tamanio == 0;
    }

    public int tamanio() {
        return tamanio;
    }

    public ListaEnlazada<T> listarElementos() {
        ListaEnlazada<T> elementos = new ListaEnlazada<>();
        Nodo<T> actual = frente;
        while (actual != null) {
            elementos.agregar(actual.dato);
            actual = actual.siguiente;
        }
        return elementos;
    }

    public void limpiar() {
        frente = null;
        final_ = null;
        tamanio = 0;
    }

    @Override
    public String toString() {
        if (estaVacia()) return "Cola vacía []";
        StringBuilder sb = new StringBuilder("Frente -> [");
        Nodo<T> actual = frente;
        while (actual != null) {
            sb.append(actual.dato);
            if (actual.siguiente != null) sb.append(", ");
            actual = actual.siguiente;
        }
        sb.append("] <- Final");
        return sb.toString();
    }
}