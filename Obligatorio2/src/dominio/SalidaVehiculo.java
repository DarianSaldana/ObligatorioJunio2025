package dominio;

import java.io.*;

/**
 *
 * @author dariansaldana 230846
 */
public class SalidaVehiculo implements Serializable {

    private Vehiculo vehiculo;
    private Empleado empleado;
    private String fecha;
    private String hora;
    private String comentario;

    public SalidaVehiculo(Vehiculo vehiculo, Empleado empleado, String fecha, String hora, String comentario) {
        this.vehiculo = vehiculo;
        this.empleado = empleado;
        this.fecha = fecha;
        this.hora = hora;
        this.comentario = comentario;
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

    public String getComentario() {
        return comentario;
    }

    @Override
    public String toString() {
        return vehiculo.getMatricula() + " (" + vehiculo.getMarca() + ")"
                + " - Entregado por " + " " + empleado.getNombre() + " el día: " + fecha + " a las " + hora;
    }

}
