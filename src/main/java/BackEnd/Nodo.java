package BackEnd;

/**
Barrutieta Nulutagua Juan Alexadner
Alcala Bejarano Jennifer Paola
 */
public class Nodo {

    private final Proceso proceso; // el dato (record) que guarda este nodo
    private Nodo siguiente;        // en el anillo nunca queda en null
    private Nodo anterior;         // en el anillo nunca queda en null

    public Nodo(Proceso proceso) {
        this.proceso = proceso;
        // Un nodo solo forma un anillo consigo mismo
        this.siguiente = this;
        this.anterior = this;
    }

    public Proceso getProceso() {
        return proceso;
    }

    public Nodo getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(Nodo siguiente) {
        this.siguiente = siguiente;
    }

    public Nodo getAnterior() {
        return anterior;
    }

    public void setAnterior(Nodo anterior) {
        this.anterior = anterior;
    }
}
