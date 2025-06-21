package dominio;

import java.io.Serializable;

/**
 *
 * @author dariansaldana 230846
 */
public class ServicioAdicional implements Serializable {

    private String tipoServicio;
    private String fecha;           
    private String hora;             
    private Vehiculo vehiculo;
    private Empleado empleado;
    private double costo;

    public ServicioAdicional(String tipoServicio, String fecha, String hora, Vehiculo vehiculo, Empleado empleado, double costo) {
        this.tipoServicio = tipoServicio;
        this.fecha = fecha;
        this.hora = hora;
        this.vehiculo = vehiculo;
        this.empleado = empleado;
        this.costo = costo;
    }

    public String getTipoServicio() {
        return tipoServicio;
    }

    public String getFecha() {
        return fecha;
    }

    public String getHora() {
        return hora;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public double getCosto() {
        return costo;
    }

    @Override
    public String toString() {
        return tipoServicio + " - " + fecha + " " + hora + " - " + vehiculo.getMatricula() + " (" + vehiculo.getMarca() + ")";
    }
}
