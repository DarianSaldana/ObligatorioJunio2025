package dominio;

import java.io.Serializable;

/**
 *
 * @author dariansaldana
 */
public class Empleado implements Serializable {

    private String nombre;
    private String cedula;
    private String direccion;
    private String numeroEmpleado;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNumeroEmpleado() {
        return numeroEmpleado;
    }

    public void setNumeroEmpleado(String numeroEmpleado) {
        this.numeroEmpleado = numeroEmpleado;
    }

    public Empleado(String nombre, String cedula, String direccion, String numeroEmpleado) {
        this.nombre = nombre;
        this.cedula = cedula;
        this.direccion = direccion;
        this.numeroEmpleado = numeroEmpleado;
     
    }

    public String toString() {
        return cedula + ", " + nombre;
    }
}
