package name.demula.chinpun.model;

import java.util.Calendar;

/**
 * Created by Jesus on 12/08/13.
 */
public class Task {
  private long idTarea;
  private String nombre;
  private String descripcion;
  private Project proyecto;
  private Task tareaPadre;
  private TaskType tipoTarea;
  private Calendar horasRealizadas;
  private Calendar primerFichaje;
  private Calendar ultimoFichaje;
  private Calendar horasOfertadas;
  private Calendar horasAcumuladas;
  private TaskState estadoTarea;
  private Calendar fechaCreacion;
  private Calendar fechaCierre;

  public Task() {
  }

  public long getIdTarea() {
    return idTarea;
  }

  public void setIdTarea(long idTarea) {
    this.idTarea = idTarea;
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

  public Project getProyecto() {
    return proyecto;
  }

  public void setProyecto(Project proyecto) {
    this.proyecto = proyecto;
  }

  public Task getTareaPadre() {
    return tareaPadre;
  }

  public void setTareaPadre(Task tareaPadre) {
    this.tareaPadre = tareaPadre;
  }

  public TaskType getTipoTarea() {
    return tipoTarea;
  }

  public void setTipoTarea(TaskType tipoTarea) {
    this.tipoTarea = tipoTarea;
  }

  public Calendar getHorasRealizadas() {
    return horasRealizadas;
  }

  public void setHorasRealizadas(Calendar horasRealizadas) {
    this.horasRealizadas = horasRealizadas;
  }

  public Calendar getPrimerFichaje() {
    return primerFichaje;
  }

  public void setPrimerFichaje(Calendar primerFichaje) {
    this.primerFichaje = primerFichaje;
  }

  public Calendar getUltimoFichaje() {
    return ultimoFichaje;
  }

  public void setUltimoFichaje(Calendar ultimoFichaje) {
    this.ultimoFichaje = ultimoFichaje;
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

  public TaskState getEstadoTarea() {
    return estadoTarea;
  }

  public void setEstadoTarea(TaskState estadoTarea) {
    this.estadoTarea = estadoTarea;
  }

  public Calendar getFechaCreacion() {
    return fechaCreacion;
  }

  public void setFechaCreacion(Calendar fechaCreacion) {
    this.fechaCreacion = fechaCreacion;
  }

  public Calendar getFechaCierre() {
    return fechaCierre;
  }

  public void setFechaCierre(Calendar fechaCierre) {
    this.fechaCierre = fechaCierre;
  }
}
