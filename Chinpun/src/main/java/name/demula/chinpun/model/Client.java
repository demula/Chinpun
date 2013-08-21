package name.demula.chinpun.model;

/**
 * Created by Jesus on 12/08/13.
 */
public class Client {
    private long idCliente;
    private String nombre;
    private String abreviatura;

    public Client(){}

    public long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(long idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }
}
