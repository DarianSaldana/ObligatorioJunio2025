package obligatorio2;

import dominio.*;
import interfaz.*;

/**
 *
 * @author dariansaldana 230846
 */
public class Obligatorio2 {

    public static void main(String[] args) {
        Sistema sistema = new Sistema();

        sistema.agregarCliente(new Cliente("Fer", "Joaquin Requena 1655", "96430256", "19382312", "1969"));
        sistema.agregarCliente(new Cliente("Darian", "Joaquin Requena 1656", "96430256", "54821260", "1999"));
        sistema.agregarCliente(new Cliente("Devin", "Joaquin Requena 1657", "96430256", "57030372", "2008"));

        sistema.agregarVehiculo(new Vehiculo("ABC1234", "Toyota", "Corolla", "Disponible"));
        sistema.agregarVehiculo(new Vehiculo("DEF5678", "Honda", "Civic", "Ocupado"));
        sistema.agregarVehiculo(new Vehiculo("GHI9012", "Ford", "Focus", "Disponible"));
        sistema.agregarVehiculo(new Vehiculo("JKL3456", "Chevrolet", "Onix", "Mantenimiento"));
        sistema.agregarVehiculo(new Vehiculo("MNO7890", "Volkswagen", "Gol", "Disponible"));

        sistema.agregarEmpleado(new Empleado("Carlos Méndez", "12345678", "Av. Italia 1234", "E001"));
        sistema.agregarEmpleado(new Empleado("Lucía Pérez", "23456789", "Bulevar Artigas 4321", "E002"));
        sistema.agregarEmpleado(new Empleado("María González", "34567890", "Av. Rivera 5678", "E003"));

        // Referencias
        Cliente cli1 = sistema.getListaClientes().get(0);
        Cliente cli2 = sistema.getListaClientes().get(1);
        Vehiculo v1 = sistema.getListaVehiculos().get(0);
        Vehiculo v2 = sistema.getListaVehiculos().get(1);
        Vehiculo v3 = sistema.getListaVehiculos().get(2);
        Empleado e1 = sistema.getListaEmpleados().get(0);
        Empleado e2 = sistema.getListaEmpleados().get(1);

        // Contratos
        sistema.agregarContrato(new Contrato(cli1, v1, e1, 15000));
        sistema.agregarContrato(new Contrato(cli2, v2, e2, 18000));

        // Entradas
        sistema.registrarEntradaVehiculo(v1, e1, "20/06/2025", "08:00", "Llegó puntual");
        sistema.registrarEntradaVehiculo(v2, e2, "20/06/2025", "14:30", "Cliente frecuente");
        sistema.registrarEntradaVehiculo(v3, e1, "21/06/2025", "09:15", "Por contrato nuevo");

        // Salidas (solo se pueden registrar si hay entradas sin finalizar)
        EntradaVehiculo entrada1 = sistema.getHistorialEntradas().get(0);
        EntradaVehiculo entrada2 = sistema.getHistorialEntradas().get(1);

        // Servicios adicionales
        sistema.registrarServicioAdicional(new ServicioAdicional("Lavado", "20/06/2025", "10:00", v1, e1, 800));
        sistema.registrarServicioAdicional(new ServicioAdicional("Cambio ruedas", "21/06/2025", "15:00", v2, e2, 1500));
        sistema.registrarServicioAdicional(new ServicioAdicional("Tapizado", "21/06/2025", "18:00", v3, e1, 1200));

        VentanaMenuPrincipal vent = new VentanaMenuPrincipal(sistema);
        vent.setVisible(true);
    }

}
