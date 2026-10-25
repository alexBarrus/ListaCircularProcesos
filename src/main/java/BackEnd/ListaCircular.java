package BackEnd;

/**
Barrutieta Nulutagua Juan Alexadner
Alcala Bejarano Jennifer Paola
 */

/**
 * TDA Lista Circular doblemente enlazada de procesos.
 * ultimo.siguiente siempre es primero y primero.anterior siempre es ultimo,
 * asi que ninguna referencia de enlace queda en null mientras haya nodos.
 * Ademas guarda un nodo "actual" para navegar con siguiente/anterior.
 */
public class ListaCircular {

    /** Resultado de buscar por valor: el proceso encontrado y su posicion. */
    public record ResultadoBusqueda(Proceso proceso, int posicion) {
    }

    private Nodo primero;      // posicion 0 (null si esta vacia)
    private Nodo ultimo;       // posicion tamanio - 1 (null si esta vacia)
    private Nodo actual;       // nodo que se esta mostrando al navegar
    private int posicionActual;
    private int tamanio;

    public ListaCircular() {
        vaciar();
    }

    // ===================== INSERTAR =====================

    public void insertarAlPrincipio(Proceso proceso) {
        Nodo nuevo = new Nodo(proceso);
        if (estaVacia()) {
            primero = nuevo;           // el unico nodo se apunta a si mismo
            ultimo = nuevo;
            actual = nuevo;
            posicionActual = 0;
        } else {
            nuevo.setSiguiente(primero); // el nuevo va antes del primero
            nuevo.setAnterior(ultimo);   // y hacia atras cierra con el ultimo
            primero.setAnterior(nuevo);
            ultimo.setSiguiente(nuevo);  // el ultimo ahora salta al nuevo
            primero = nuevo;
            posicionActual++;            // el actual se recorrio un lugar
        }
        tamanio++;
    }

    public void insertarAlFinal(Proceso proceso) {
        Nodo nuevo = new Nodo(proceso);
        if (estaVacia()) {
            primero = nuevo;
            ultimo = nuevo;
            actual = nuevo;
            posicionActual = 0;
        } else {
            nuevo.setAnterior(ultimo);   // el nuevo va despues del ultimo
            nuevo.setSiguiente(primero); // y hacia adelante cierra con el primero
            ultimo.setSiguiente(nuevo);
            primero.setAnterior(nuevo);  // el primero ahora regresa al nuevo
            ultimo = nuevo;
        }
        tamanio++;
    }

    /** Valido de 0 a tamanio (tamanio equivale a insertar al final). */
    public void insertarEnPosicion(Proceso proceso, int posicion) {
        if (posicion < 0 || posicion > tamanio) {
            throw new IndexOutOfBoundsException("Posición " + posicion
                    + " fuera de rango. Use de 0 a " + tamanio + ".");
        }
        if (posicion == 0) {
            insertarAlPrincipio(proceso);
        } else if (posicion == tamanio) {
            insertarAlFinal(proceso);
        } else {
            Nodo despues = nodoEn(posicion); // el que hoy ocupa ese lugar
            Nodo antes = despues.getAnterior();
            Nodo nuevo = new Nodo(proceso);
            nuevo.setAnterior(antes);
            nuevo.setSiguiente(despues);
            antes.setSiguiente(nuevo);
            despues.setAnterior(nuevo);
            if (posicionActual >= posicion) {
                posicionActual++;
            }
            tamanio++;
        }
    }

    // ===================== REMOVER =====================

    public Proceso removerAlPrincipio() {
        validarNoVacia();
        return desenlazar(primero, 0);
    }

    public Proceso removerAlFinal() {
        validarNoVacia();
        return desenlazar(ultimo, tamanio - 1);
    }

    public Proceso removerEnPosicion(int posicion) {
        validarNoVacia();
        validarPosicion(posicion);
        return desenlazar(nodoEn(posicion), posicion);
    }

    /** Saca un nodo del anillo uniendo directamente a sus dos vecinos. */
    private Proceso desenlazar(Nodo nodo, int posicion) {
        if (tamanio == 1) {
            Proceso dato = nodo.getProceso();
            vaciar();
            return dato;
        }
        Nodo antes = nodo.getAnterior();
        Nodo despues = nodo.getSiguiente();
        antes.setSiguiente(despues);
        despues.setAnterior(antes);
        if (nodo == primero) {
            primero = despues;  // el nuevo primero ya apunta atras al ultimo
        }
        if (nodo == ultimo) {
            ultimo = antes;     // el nuevo ultimo ya apunta adelante al primero
        }
        tamanio--;

        // Mantener el cursor de navegacion en un nodo que siga en el anillo
        if (nodo == actual) {
            actual = despues; // si era el ultimo, despues es primero (posicion 0)
            posicionActual = (despues == primero) ? 0 : posicion;
        } else if (posicionActual > posicion) {
            posicionActual--;
        }
        nodo.setSiguiente(nodo); // el nodo sacado ya no apunta al anillo
        nodo.setAnterior(nodo);
        return nodo.getProceso();
    }

    // ===================== MOSTRAR =====================

    public Proceso mostrarAlPrincipio() {
        validarNoVacia();
        actual = primero;
        posicionActual = 0;
        return primero.getProceso();
    }

    public Proceso mostrarAlFinal() {
        validarNoVacia();
        actual = ultimo;
        posicionActual = tamanio - 1;
        return ultimo.getProceso();
    }

    public Proceso mostrarEnPosicion(int posicion) {
        validarNoVacia();
        validarPosicion(posicion);
        actual = nodoEn(posicion);
        posicionActual = posicion;
        return actual.getProceso();
    }

    /** Lee una posicion sin mover el cursor de navegacion (para la tabla). */
    public Proceso obtener(int posicion) {
        validarNoVacia();
        validarPosicion(posicion);
        return nodoEn(posicion).getProceso();
    }

    // ===================== NAVEGAR =====================

    /** Desde ultimo, el siguiente es primero porque ultimo.siguiente == primero. */
    public Proceso siguiente() {
        validarNoVacia();
        actual = actual.getSiguiente();
        posicionActual = (posicionActual + 1) % tamanio;
        return actual.getProceso();
    }

    /** Desde primero, el anterior es ultimo porque primero.anterior == ultimo. */
    public Proceso anterior() {
        validarNoVacia();
        actual = actual.getAnterior();
        posicionActual = (posicionActual - 1 + tamanio) % tamanio;
        return actual.getProceso();
    }

    public Proceso getActual() {
        return actual == null ? null : actual.getProceso();
    }

    public int getPosicionActual() {
        return actual == null ? -1 : posicionActual;
    }

    // ===================== BUSCAR =====================

    /**
     * Da una sola vuelta al anillo: se detiene al regresar a primero,
     * asi nunca cae en un ciclo infinito. Devuelve null si no lo encuentra.
     */
    public ResultadoBusqueda buscarPorValor(int pid) {
        validarNoVacia();
        Nodo aux = primero;
        int posicion = 0;
        do {
            if (aux.getProceso().pid() == pid) {
                return new ResultadoBusqueda(aux.getProceso(), posicion);
            }
            aux = aux.getSiguiente();
            posicion++;
        } while (aux != primero);
        return null;
    }

    public Proceso buscarPorPosicion(int posicion) {
        return obtener(posicion);
    }

    /** Igual que buscarPorValor pero sin exigir que haya nodos. */
    public boolean contiene(int pid) {
        return !estaVacia() && buscarPorValor(pid) != null;
    }

    // ===================== AUXILIARES =====================

    public final void vaciar() {
        primero = null;
        ultimo = null;
        actual = null;
        posicionActual = -1;
        tamanio = 0;
    }

    public boolean estaVacia() {
        return primero == null;
    }

    public int getTamanio() {
        return tamanio;
    }

    /** Proceso al que apunta ultimo.siguiente (debe ser el primero). */
    public Proceso siguienteDelUltimo() {
        validarNoVacia();
        return ultimo.getSiguiente().getProceso();
    }

    /** Proceso al que apunta primero.anterior (debe ser el ultimo). */
    public Proceso anteriorDelPrimero() {
        validarNoVacia();
        return primero.getAnterior().getProceso();
    }

    /** Recorre desde el extremo mas cercano para llegar a la posicion. */
    private Nodo nodoEn(int posicion) {
        Nodo aux;
        if (posicion < tamanio / 2) {
            aux = primero;
            for (int i = 0; i < posicion; i++) {
                aux = aux.getSiguiente();
            }
        } else {
            aux = ultimo;
            for (int i = tamanio - 1; i > posicion; i--) {
                aux = aux.getAnterior();
            }
        }
        return aux;
    }

    private void validarNoVacia() {
        if (estaVacia()) {
            throw new IllegalStateException("La lista está vacía.");
        }
    }

    private void validarPosicion(int posicion) {
        if (posicion < 0 || posicion >= tamanio) {
            throw new IndexOutOfBoundsException("Posición " + posicion
                    + " fuera de rango. Use de 0 a " + (tamanio - 1) + ".");
        }
    }
}
