package name.demula.chinpun.model;

import java.util.Calendar;

/**
 * Created by Jesus on 12/08/13.
 */
public class Project {
    private long idProyecto;
    private String nombre;
    private String descripcion;
    private String observaciones;
    private Calendar fechaInicio;
    private Calendar fechaFin;
    private boolean tengoPermisos;
    private boolean soyMiembro;
    private Client cliente;
    private String codigo;
    private String tipo;
    private Calendar estimado;
    private String estado;
    private long orden;
    private boolean imputable;
    private boolean parte;
    private long numPedido;
    private Calendar horasOfertadas;
    private Calendar horasAcumuladas;
    private Calendar horasProyecto;

    public Project() {
    }

    public long getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(long idProyecto) {
        this.idProyecto = idProyecto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Calendar getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Calendar fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Calendar getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Calendar fechaFin) {
        this.fechaFin = fechaFin;
    }

    public boolean isTengoPermisos() {
        return tengoPermisos;
    }

    public void setTengoPermisos(boolean tengoPermisos) {
        this.tengoPermisos = tengoPermisos;
    }

    public boolean isSoyMiembro() {
        return soyMiembro;
    }

    public void setSoyMiembro(boolean soyMiembro) {
        this.soyMiembro = soyMiembro;
    }

    public Client getCliente() {
        return cliente;
    }

    public void setCliente(Client cliente) {
        this.cliente = cliente;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Calendar getEstimado() {
        return estimado;
    }

    public void setEstimado(Calendar estimado) {
        this.estimado = estimado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public long getOrden() {
        return orden;
    }

    public void setOrden(long orden) {
        this.orden = orden;
    }

    public boolean isImputable() {
        return imputable;
    }

    public void setImputable(boolean imputable) {
        this.imputable = imputable;
    }

    public boolean isParte() {
        return parte;
    }

    public void setParte(boolean parte) {
        this.parte = parte;
    }

    public long getNumPedido() {
        return numPedido;
    }

    public void setNumPedido(long numPedido) {
        this.numPedido = numPedido;
    }

    public Calendar getHorasOfertadas() {
        return horasOfertadas;
    }

    public void setHorasOfertadas(Calendar horasOfertadas) {
        this.horasOfertadas = horasOfertadas;
    }

    public Calendar getHorasAcumuladas() {
        return horasAcumuladas;
    }

    public void setHorasAcumuladas(Calendar horasAcumuladas) {
        this.horasAcumuladas = horasAcumuladas;
    }

    public Calendar getHorasProyecto() {
        return horasProyecto;
    }

    public void setHorasProyecto(Calendar horasProyecto) {
        this.horasProyecto = horasProyecto;
    }
}
