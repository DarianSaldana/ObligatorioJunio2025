package dominio;

import java.io.*;

/**
 *
 * @author dariansaldana 230846
 */
public class EntradaVehiculo implements Serializable {

    private Vehiculo vehiculo;
    private Empleado empleado;
    private String fecha;
    private String hora;
    private String notas;
    private boolean finalizada;
    private SalidaVehiculo salida;

    public EntradaVehiculo(Vehiculo vehiculo, Empleado empleado, String fecha, String hora, String notas) {
        this.vehiculo = vehiculo;
        this.empleado = empleado;
        this.fecha = fecha;
        this.hora = hora;
        this.notas = notas;
        this.finalizada = false;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public String getFecha() {
        return fecha;
    }

    public String getHora() {
        return hora;
    }

    public String getNotas() {
        return notas;
    }

    public boolean estaFinalizada() {
        return finalizada;
    }

    public void marcarComoFinalizada() {
        this.finalizada = true;
    }

    public SalidaVehiculo getSalida() {
        return salida;
    }

    public void setSalida(SalidaVehiculo salida) {
        this.salida = salida;
    }

    @Override
    public String toString() {
        return vehiculo.getMatricula() + " (" + vehiculo.getMarca() + ")"
                + " - Recibido por " + " " + empleado.getNombre() + " el día: " + fecha + " a las " + hora;
    }

}
