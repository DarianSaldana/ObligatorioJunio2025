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

        sistema.agregarEmpleado(new Empleado("Carlos Méndez", "12345678", "Av. Italia 1234", "E001"));
        sistema.agregarEmpleado(new Empleado("Lucía Pérez", "23456789", "Bulevar Artigas 4321", "E002"));

        // Obtener entidades desde las listas
        Cliente cliente = sistema.getListaClientes().get(0);
        Vehiculo vehiculo = sistema.getListaVehiculos().get(0);
        Empleado empleado = sistema.getListaEmpleados().get(0);
        Cliente cliente2 = sistema.getListaClientes().get(1);
        Vehiculo vehiculo2 = sistema.getListaVehiculos().get(1);
        Empleado empleado2 = sistema.getListaEmpleados().get(1);

        // Crear contrato
        Contrato contratoEjemplo = new Contrato(cliente, vehiculo, empleado, 15000.0);
        Contrato contratoEjemplo2 = new Contrato(cliente2, vehiculo2, empleado2, 15000.0);

        // Agregarlo al sistema
        sistema.agregarContrato(contratoEjemplo);
        sistema.agregarContrato(contratoEjemplo2);

        VentanaMenuPrincipal vent = new VentanaMenuPrincipal(sistema);
        vent.setVisible(true);
    }

}
