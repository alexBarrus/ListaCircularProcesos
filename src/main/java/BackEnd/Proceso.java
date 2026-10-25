package BackEnd;

/**
Barrutieta Nulutagua Juan Alexadner
Alcala Bejarano Jennifer Paola
 */

/**
 * Entidad de dominio: un proceso que espera su turno de CPU (Round-Robin).
 * Es un record, asi que es inmutable: Java genera el constructor,
 * los metodos de lectura pid(), nombre(), rafaga(), equals y hashCode.
 */
public record Proceso(int pid, String nombre, int rafaga) {

    // Constructor compacto: ultima defensa por si alguien crea un Proceso invalido.
    public Proceso {
        if (pid <= 0) {
            throw new IllegalArgumentException("El PID debe ser mayor que 0.");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        if (rafaga <= 0) {
            throw new IllegalArgumentException("La ráfaga debe ser mayor que 0.");
        }
    }

    @Override
    public String toString() {
        return "PID: " + pid + "\nNombre: " + nombre + "\nRáfaga: " + rafaga + " ms";
    }
}
