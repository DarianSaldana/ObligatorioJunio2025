package dominio;

import java.io.Serializable;

/**
 *
 * @author dariansaldana 230846
 */
public class Vehiculo  implements Serializable  {

    private String matricula;
    private String marca;
    private String modelo;
    private String estado;

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Vehiculo(String matricula, String marca, String modelo, String estado) {
        this.matricula = matricula;
        this.marca = marca;
        this.modelo = modelo;
        this.estado = estado;
    }

    public String toString() {
        return matricula + ", " + marca;
    }

}
