package dominio;

import java.io.Serializable;

/**
 *
 * @author dariansaldana 230846
 */
public class Cliente implements Serializable {
    
    private String nombre;
    private String direccion;
    private String celular;
    private String cedula;
    private String año;

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

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getAño() {
        return año;
    }

    public void setAño(String año) {
        this.año = año;
    }

    public Cliente(String nombre, String direccion, String celular, String cedula, String año) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.celular = celular;
        this.cedula = cedula;
        this.año = año;
    }

    public String toString() {
        return cedula + ", " + nombre;
    }

}
