package dominio;

/**
 *
 * @author dariansaldana
 */
import java.io.Serializable;

public class Contrato implements Serializable {

    private static int ultimoId = 0;

    private int id;
    private Cliente cliente;
    private Vehiculo vehiculo;
    private Empleado empleado;
    private double valorMensual;

    public Contrato(Cliente cliente, Vehiculo vehiculo, Empleado empleado, double valorMensual) {
        this.id = ++ultimoId;
        this.cliente = cliente;
        this.vehiculo = vehiculo;
        this.empleado = empleado;
        this.valorMensual = valorMensual;
    }

    public int getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public double getValorMensual() {
        return valorMensual;
    }

    @Override
    public String toString() {
        return "Contrato #" + id + " - Cliente: " + cliente.getNombre() + " - Vehículo: " + vehiculo.getMatricula();
    }

}
