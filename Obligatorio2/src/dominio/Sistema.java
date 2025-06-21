package dominio;

import java.io.*;
import java.util.*;

public class Sistema implements Serializable {

    private ArrayList<Cliente> listaClientes;
    private ArrayList<Vehiculo> listaVehiculos;
    private ArrayList<Empleado> listaEmpleados;
    private ArrayList<Contrato> listaContratos;
    private ArrayList<Vehiculo> vehiculosEnParking;
    private ArrayList<EntradaVehiculo> historialEntradas;
    private ArrayList<SalidaVehiculo> historialSalidas;
    private ArrayList<ServicioAdicional> serviciosAdicionales;

    public Sistema() {
        listaClientes = new ArrayList<>();
        listaVehiculos = new ArrayList<>();
        listaEmpleados = new ArrayList<>();
        listaContratos = new ArrayList<>();
        vehiculosEnParking = new ArrayList<>();
        historialEntradas = new ArrayList<>();
        historialSalidas = new ArrayList<>();
        serviciosAdicionales = new ArrayList<>();
    }

    // CLIENTES
    public void agregarCliente(Cliente cli) {
        listaClientes.add(cli);
    }

    public ArrayList<Cliente> getListaClientes() {
        return listaClientes;
    }

    public Cliente buscarClientePorCedula(String cedulaBuscada) {
        for (Cliente cli : listaClientes) {
            if (cli.getCedula().equals(cedulaBuscada)) {
                return cli;
            }
        }
        return null;
    }

    public boolean eliminarClientePorCedula(String cedula) {
        Cliente cliente = buscarClientePorCedula(cedula);
        if (cliente != null) {
            listaClientes.remove(cliente);
            // Eliminar todos los contratos asociados a ese cliente
            listaContratos.removeIf(c -> c.getCliente().equals(cliente));
            return true;
        }
        return false;
    }

    // VEHÍCULOS
    public void agregarVehiculo(Vehiculo vehi) {
        listaVehiculos.add(vehi);
    }

    public ArrayList<Vehiculo> getListaVehiculos() {
        return listaVehiculos;
    }

    public Vehiculo buscarVehiculoPorMatricula(String matriculaBuscada) {
        for (Vehiculo v : listaVehiculos) {
            if (v.getMatricula().equalsIgnoreCase(matriculaBuscada)) {
                return v;
            }
        }
        return null;
    }

    // EMPLEADOS
    public void agregarEmpleado(Empleado e) {
        if (buscarEmpleadoPorCedula(e.getCedula()) == null) {
            listaEmpleados.add(e);
        }
    }

    public ArrayList<Empleado> getListaEmpleados() {
        return listaEmpleados;
    }

    public Empleado buscarEmpleadoPorCedula(String cedulaBuscada) {
        for (Empleado emp : listaEmpleados) {
            if (emp.getCedula().equals(cedulaBuscada)) {
                return emp;
            }
        }
        return null;
    }

    //CONTRATOS
    public void agregarContrato(Contrato contrato) {
        listaContratos.add(contrato);
    }

    public ArrayList<Contrato> getListaContratos() {
        return listaContratos;
    }

    public boolean vehiculoYaContratado(Vehiculo vehiculo) {
        for (Contrato contrato : listaContratos) {
            if (contrato.getVehiculo().equals(vehiculo)) {
                return true;
            }
        }
        return false;
    }

    //VEHICULOS EN PARKING
    public boolean vehiculoEstaEnParking(Vehiculo vehiculo) {
        for (EntradaVehiculo entrada : historialEntradas) {
            if (entrada.getVehiculo().equals(vehiculo) && !entrada.estaFinalizada()) {
                return true;
            }
        }
        return false;
    }

    public boolean vehiculoTieneContrato(Vehiculo v) {
        for (Contrato c : listaContratos) {
            if (c.getVehiculo().equals(v)) {
                return true;
            }
        }
        return false;
    }

    //ENTRADAS VEHICULOS
    public void registrarEntradaVehiculo(Vehiculo v, Empleado e, String fecha, String hora, String notas) {
        EntradaVehiculo entrada = new EntradaVehiculo(v, e, fecha, hora, notas);
        historialEntradas.add(entrada);
        vehiculosEnParking.add(v);
    }

    public ArrayList<EntradaVehiculo> getHistorialEntradas() {
        return historialEntradas;
    }

    public ArrayList<SalidaVehiculo> getHistorialSalidas() {
        return historialSalidas;
    }

    public void registrarSalidaVehiculo(EntradaVehiculo entrada, SalidaVehiculo salida) {
        entrada.marcarComoFinalizada();
        historialSalidas.add(salida);
    }

    //SERVICIOS ADICIONALES
    public void registrarServicioAdicional(ServicioAdicional servicio) {
        serviciosAdicionales.add(servicio);
    }

    public ArrayList<ServicioAdicional> getServiciosAdicionales() {
        return serviciosAdicionales;
    }

}
